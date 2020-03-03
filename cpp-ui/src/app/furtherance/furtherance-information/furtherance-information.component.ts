import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { Component, OnInit, ElementRef, ViewChild, OnDestroy } from '@angular/core';

import { ErrorService, FurtheranceService, ReviewService, RETURN_TO_CLM } from '../../shared';
declare var $: any;

@Component({
  selector: 'app-furtherance-information',
  templateUrl: './furtherance-information.component.html',
  styleUrls: ['./furtherance-information.component.scss']
})
export class FurtheranceInformationComponent implements OnInit, OnDestroy {

  @ViewChild('furtheranceModalCancelBtn') public furtheranceModalCancelBtn: ElementRef;

  private subscription: Subscription[] = [];
  public parentAgreementId: string;
  public contractType: string;
  public hasInProgressFutherance: boolean;
  public showSpinner: boolean;
  public clmHttpLink: string;
  public agreementId: string;
  public cppFurtheranceSeq: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private errorService: ErrorService,
    private furtheranceService: FurtheranceService,
    private reviewService: ReviewService,
  ) { }

  ngOnInit() {
    this.errorService.checkErrorStatus(true);
    this.showSpinner = true;
    this.parentAgreementId = this.route.snapshot.queryParams['agreementId'];
    this.contractType = this.route.snapshot.queryParams['contractType'];
    if (this.parentAgreementId && this.contractType) {
      this.setFurtheranceAgreementInfo();
    } else {
      this.getFurtheranceAgreementInfo();
    }
    this.validateHasInProgressFurtherance();
  }

  validateHasInProgressFurtherance() {
    this.subscription.push(this.furtheranceService.validateHasInProgressFurtherance(this.parentAgreementId.trim())
      .subscribe(validateFurtheranceModel => {
        this.hasInProgressFutherance = validateFurtheranceModel.hasInProgressFurtherance;
        this.showSpinner = false;
        if (!this.hasInProgressFutherance) {
          $('#furtheranceConfirmationModal').modal('show');
          $('#furtheranceConfirmationModal').on('shown.bs.modal', () => {
            this.furtheranceModalCancelBtn.nativeElement.focus();
          });
        } else {
          this.fetchInProgressFurtheranceInformation();
        }
      }));
  }

  setFurtheranceAgreementInfo() {
    const agreementInfo = {
      parentAgreementId: this.parentAgreementId.trim(),
      parentContractType: this.contractType.trim()
    }
    sessionStorage.setItem('contractInfo', JSON.stringify(agreementInfo));
  }

  getFurtheranceAgreementInfo() {
    const agreementInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.parentAgreementId = agreementInfo.parentAgreementId;
    this.contractType = agreementInfo.parentContractType;
  }

  onCreateNewFurtherance() {
    this.showSpinner = true;
    this.subscription.push(this.furtheranceService.createNewFurtherance(this.parentAgreementId.trim(), this.contractType.trim())
      .subscribe(furtheranceBaseModel => {
        this.navigateToPricingInfo(furtheranceBaseModel);
      }));
  }

  fetchInProgressFurtheranceInformation() {
    this.showSpinner = true;
    this.subscription.push(this.furtheranceService.fetchInProgressFurtheranceInfo(this.parentAgreementId.trim())
      .subscribe(furtheranceBaseModel => {
        this.navigateToPricingInfo(furtheranceBaseModel);
      }));
  }

  navigateToPricingInfo(furtheranceBaseModel) {
    this.agreementId = furtheranceBaseModel.agreementId;
    this.contractType = furtheranceBaseModel.contractType;
    this.cppFurtheranceSeq = furtheranceBaseModel.cppFurtheranceSeq;
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    contractInfo.isFurtheranceMode = true;
    contractInfo.cppFurtheranceSeq = this.cppFurtheranceSeq;
    sessionStorage.setItem('contractInfo', JSON.stringify(contractInfo));
    this.showSpinner = false;
    this.router.navigate(['/pricinginformation'], { relativeTo: this.route,
      queryParams: { agreementId: this.agreementId, contractType: this.contractType }
    });
  }

  onCancelAndGoBack() {
    this.showSpinner = true;
    this.subscription.push(this.reviewService.fetchClmUrl().subscribe(response => {
      this.clmHttpLink = response.clmUrlKey;
      const clmURL = this.clmHttpLink + this.contractType + RETURN_TO_CLM.AND_ID + this.parentAgreementId;
      this.showSpinner = false;
      window.open(clmURL, '_self');
    },
      error => {
        this.showSpinner = false;
      }));
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}
