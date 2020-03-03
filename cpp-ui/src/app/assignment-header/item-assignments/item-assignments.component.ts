import { Component, OnInit, Renderer2, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { AuthorizationService, ItemAssignmentService, IMAGE_PATH, ReviewService,
   RETURN_TO_CLM, TranslatorService, ToasterService, FurtheranceService } from 'app/shared';
import { FutureItemModel } from './item-assignments.model';

@Component({
  selector: 'app-item-assignments',
  templateUrl: './item-assignments.component.html',
  styleUrls: ['./item-assignments.component.scss'],
})
export class ItemAssignmentsComponent implements OnInit, OnDestroy {

  public futureItemAccordions: FutureItemModel[] = [];
  private subscription: Subscription[] = [];
  public contractPriceProfileId: string;
  public disableSaveInd = true;
  public imageDir = IMAGE_PATH;
  public allowClear: boolean;
  public itemMap = new Map();
  public itemMapKeyList = [];
  public itemInd = 1;
  public rows: number;
  public futureItemListLength: number;
  public agreementId: string;
  public contractTypeName: string;
  public clmHttpLink: string;
  public isViewMode: boolean;
  public showSpinner: boolean;
  public isFurtheranceMode: boolean;
  public isPowerUser: boolean;
  public isPriceProfileBtnEnabled: boolean;

  constructor(
    private authorizationService: AuthorizationService,
    private furtheranceService: FurtheranceService,
    private itemAssignmentService: ItemAssignmentService,
    public renderer: Renderer2,
    private reviewService: ReviewService,
    private toaster: ToasterService,
    private translatorService: TranslatorService
  ) { }

  ngOnInit() {
    this.fetchAllFutureItems();
  }

  fetchAllFutureItems() {
    this.showSpinner = true;
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractPriceProfileId = contractDetails.cppseqid;
    this.agreementId = contractDetails.agreementId;
    this.contractTypeName = contractDetails.contractType;
    this.isFurtheranceMode = contractDetails.isFurtheranceMode;
    this.subscription.push(this.authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileId),
      contractDetails.isAmendment, contractDetails.contractStatus)
      .subscribe(authorizationDetails => {
        this.authorizationService.setAuthorizationDetails(authorizationDetails);
        this.isPowerUser = authorizationDetails.isPowerUser;
        this.isViewMode = authorizationDetails.itemAssignmentEditable ? false : true;
        this.subscription.push(this.itemAssignmentService.fetchAllFutureItems(this.contractPriceProfileId)
          .subscribe(futureItemDetails => {
            this.futureItemAccordions = futureItemDetails;
            this.showSpinner = false;
          }));
        if (this.isFurtheranceMode && this.isPowerUser) {
          this.fetchActivatePricingButtonState(contractDetails);
        }
      }));
  }

  fetchActivatePricingButtonState(contractDetails) {
    this.subscription.push(this.furtheranceService.enableActivatePricingForFurtherance(contractDetails.cppFurtheranceSeq,
       contractDetails.contractStatus)
        .subscribe(activatePricingButtonState => {
          this.isPriceProfileBtnEnabled = activatePricingButtonState.enableActivatePricingButton;
    }));
  }

  toggleCollapse($ele) {
    const element = $ele.querySelector('.fa');
    if (element.classList[1] === 'fa-angle-down') {
      this.renderer.removeClass(element, 'fa-angle-down');
      this.renderer.addClass(element, 'fa-angle-right');
    } else {
      this.renderer.removeClass(element, 'fa-angle-right');
      this.renderer.addClass(element, 'fa-angle-down');
    }
  }

  onActivatePricingForFurtherance() {
    this.showSpinner = true;
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    const pricingActivationSuccessMsg = this.translatorService.translate('TOASTER_MESSAGES.PRICING_ACTIVATION_SUCCESS');
    this.subscription.push(this.furtheranceService.activatePricingForFurtherance(contractDetails.cppFurtheranceSeq,
      contractDetails.contractStatus)
        .subscribe(() => {
            this.toaster.showSuccess(pricingActivationSuccessMsg, '');
            this.showSpinner = false;
            this.isPriceProfileBtnEnabled = false;
          },
          error => {
            this.isPriceProfileBtnEnabled = true;
            this.showSpinner = false;
          }));
  }

  returnToCLM() {
    this.showSpinner = true;
      this.subscription.push(
      this.reviewService.fetchClmUrl()
        .subscribe(response => {
          if (response) {
            this.clmHttpLink = response.clmUrlKey;
            const clmURL = this.clmHttpLink + this.contractTypeName + RETURN_TO_CLM.AND_ID + this.agreementId;
            sessionStorage.clear();
            window.open(clmURL, '_self');
          }
        },
        error => {
          this.showSpinner = false;
      })
    )
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}
