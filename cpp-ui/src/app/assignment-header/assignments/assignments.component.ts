import { Component, OnInit, OnDestroy, Renderer2, DoCheck } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { AssignmentMarkupModel } from './assignments.model';
import { AssignmentsService, ReviewService, RETURN_TO_CLM, ToasterService, TranslatorService, AuthorizationService,
  CONTRACT_APPROVED } from './../../shared';
import { IMAGE_PATH } from './../../shared';

@Component({
  selector: 'app-assignments',
  templateUrl: './assignments.component.html',
  styleUrls: ['./assignments.component.scss']
})
export class AssignmentsComponent implements OnInit, OnDestroy, DoCheck {
  private subscription: Subscription[] = [];
  public assignmentDetails: AssignmentMarkupModel[];
  public customerTypes = [];
  public imageDir = IMAGE_PATH;
  public contractPriceProfileId: string;
  public agreementId: string;
  public contractTypeName: string;
  public clmHttpLink: string;
  public isViewMode: boolean;
  public showSpinner: boolean;
  public clearAllAssignments = false;
  public disableActivatePriceProfile: boolean;
  public isAmendment: boolean;
  public clmStatus: string;
  public cppStatus: string;

  constructor(
    private assignmentService: AssignmentsService,
    public _authorizationService: AuthorizationService,
    public renderer: Renderer2,
    private _reviewService: ReviewService,
    private _translatorService: TranslatorService,
    private _toaster: ToasterService
  ) {}

  ngOnInit() {
    this.loadPage();
  }

  ngDoCheck() {
    if (this.clearAllAssignments) {
      this.loadPage();
    }
    this.clearAllAssignments = false;
  }

  loadPage() {
    this.showSpinner = true;
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractPriceProfileId = contractDetails.cppseqid;
    this.agreementId = contractDetails.agreementId;
    this.contractTypeName = contractDetails.contractType;
    this.isAmendment = contractDetails.isAmendment;
    this.clmStatus = contractDetails.contractStatus;
    this.subscription.push(this._authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileId),
          contractDetails.isAmendment, contractDetails.contractStatus)
        .subscribe(authorizationDetails => {
          this._authorizationService.setAuthorizationDetails(authorizationDetails);
          this.isViewMode = authorizationDetails.customerAssignmentEditable ? false : true;
          this.cppStatus = authorizationDetails.cppStatus;
          this.subscription.push(
            this.assignmentService
              .fetchAssignmentMarkups(this.contractPriceProfileId)
              .switchMap(assignmentDetails => {
                this.assignmentDetails = assignmentDetails;
                this.fetchCustomerTypes();
                this.showSpinner = false;
                if (!this.isViewMode) {
                  return this.assignmentService.fetchActivatePricingButtonState(
                    this.contractPriceProfileId
                  );
                } else {
                  return Observable.of({'enableActivatePricingButton' : false});
                }
              })
              .subscribe(state => {
                this.disableActivatePriceProfile = !state.enableActivatePricingButton;
              }));
        })
    );
  }

  fetchCustomerTypes() {
    this.subscription.push(
      this.assignmentService.fetchCustomerType().subscribe(customerTypes => {
        this.customerTypes = customerTypes;
      })
    );
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

  detectDefaultAssignmentDelete($event) {
    if ($event) {
      this.clearAllAssignments = true;
    }
  }

  fetchPricingActivateState($event) {
    this.disableActivatePriceProfile = !$event;
  }

  onActivatePricing() {
      this.showSpinner = true;
      const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
      const pricingActivationSuccessMsg = this._translatorService.translate('TOASTER_MESSAGES.PRICING_ACTIVATION_SUCCESS');
      this.subscription.push(this.assignmentService.activatePricing(this.contractPriceProfileId,
         contractDetails.isAmendment, contractDetails.contractStatus)
          .subscribe(() => {
              this._authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileId),
               contractDetails.isAmendment, contractDetails.contractStatus)
              .subscribe(authorizationDetails => {
                this.isViewMode = authorizationDetails.customerAssignmentEditable ? false : true;
                this._toaster.showSuccess(pricingActivationSuccessMsg, '');
                this.showSpinner = false;
                this.disableActivatePriceProfile = true;
              });
            },
            error => {
              this.disableActivatePriceProfile = false;
              this.showSpinner = false;
            }));
  }

  returnToCLM() {
    this.showSpinner = true;
    this.subscription.push(
      this._reviewService.fetchClmUrl().subscribe(response => {
        if (response) {
          this.clmHttpLink = response.clmUrlKey;
          const clmURL =
            this.clmHttpLink +
            this.contractTypeName +
            RETURN_TO_CLM.AND_ID +
            this.agreementId;
            sessionStorage.clear();
            window.open(clmURL, '_self');
        }
      },
      error => {
        this.showSpinner = false;
    })
    );
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }
}
