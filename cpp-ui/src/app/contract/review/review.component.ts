import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Subscription } from 'rxjs/Subscription';

import { apiUrls, FurtheranceService } from 'app/shared';
import { FurtheranceBaseModel } from 'app/furtherance';
import { ReviewService, StepperService, AuthorizationService, RETURN_TO_CLM } from './../../shared';
import { ZERO, ONE, STEPPER_NUMBERS, STEPPER_URL, YES, NO, CONTRACT_TYPES, FURTHERANCE_STEPPER_CODE } from './../../shared';
import { ReviewData, CPPExhibitDownload, ReturnToCLM, PricingExhibitModel } from 'app/contract/review/review.model';


@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit, OnDestroy {
  public contractType: string;
  public contractPriceProfileId: number;
  public alphabetSet = 'B';
  public next_val: string;
  private _subscription: Subscription[] = [];
  public displayViewMode: boolean;
  public showSpinner: boolean;
  public agreementId: string;
  public contractTypeName: string;
  public clmHttpLink: string;
  public isAmendment: boolean;
  public contractStatus: string;
  public pricingExhibitModel = <PricingExhibitModel>{};
  public furtheranceBaseModel = <FurtheranceBaseModel>{};
  public isFurtheranceMode: boolean;
  public cppFurtheranceSeq: number;
  public parentAgreementId: string;
  public parentContractType: string;
  public canEditFurtherance: boolean;
  reviewData: ReviewData;
  dcList = [];
  markupGridData = [];
  labelYes = YES;
  labelNo = NO;
  contractType_IFS = CONTRACT_TYPES.IFS;
  contractType_IFS_AMENDEMENT = CONTRACT_TYPES.IFS_AMENDMENT;


  constructor(
    private _authorizationService: AuthorizationService,
    private _stepperService: StepperService,
    private _reviewService: ReviewService,
    private _furtheranceService: FurtheranceService,
    private _router: Router,
    private _route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.getContractType();
    this.showSpinner = true;
    this._subscription.push(this._authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileId),
    this.isAmendment, this.contractStatus)
      .subscribe(authorizationDetails => {
        this._authorizationService.setAuthorizationDetails(authorizationDetails);
        this._stepperService.currentStep(STEPPER_NUMBERS.REVIEW);
        this.displayViewMode = authorizationDetails.priceProfileEditable ? false : true;
        this._subscription.push(
          this._reviewService
            .fetchReviewData(String(this.contractPriceProfileId))
            .subscribe(response => {
              this.reviewData = response;
              this.dcList = response.distributionCenter;
              this.markupGridData = response.markupReviewDTO.markupGridDTOs;
              if (
                response.contractPricingReviewDTO
                  .formalPriceAuditContractLanguage !== null ||
                response.contractPricingReviewDTO
                  .priceVerificationContractLanguage !== null
              ) {
                this.next_val = this.incrementChar(this.alphabetSet);
              } else {
                this.next_val = this.alphabetSet;
              }
              this.showSpinner = false;
            }));
      }));
  }

  getContractType() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractType = contractDetails.ctype;
    this.contractPriceProfileId = contractDetails.cppseqid;
    this.agreementId = contractDetails.agreementId;
    this.contractTypeName = contractDetails.contractType;
    this.isAmendment = contractDetails.isAmendment;
    this.contractStatus = contractDetails.contractStatus;
    this.cppFurtheranceSeq = contractDetails.cppFurtheranceSeq;
    this.isFurtheranceMode = contractDetails.isFurtheranceMode;
    if (contractDetails.isFurtheranceMode) {
      this._subscription.push(this._furtheranceService.canEditFurtherance(this.cppFurtheranceSeq).subscribe(displayMode => {
        this.canEditFurtherance = displayMode.canEditFurtherance;
      }))
      this.parentAgreementId = contractDetails.parentAgreementId;
      this.parentContractType = contractDetails.parentContractType;
      this._stepperService.determineFurtheranceStepperMode(FURTHERANCE_STEPPER_CODE);
      }
  }

  incrementChar(c) {
    return String.fromCharCode(c.charCodeAt(ZERO) + ONE);
  }

  navigateToContractPricing() {
    this._router.navigate([STEPPER_URL.PRICING_INFORMATION_URL], {
      relativeTo: this._route
    });
  }

  navigateToDistributionCenter() {
    this._router.navigate([STEPPER_URL.DISTRIBUTION_CENTERS_URL], {
      relativeTo: this._route
    });
  }

  navigateToMarkup() {
    this._router.navigate([STEPPER_URL.MARKUP_URL], {
      relativeTo: this._route
    });
  }

  navigateToSplitCase() {
    this._router.navigate([STEPPER_URL.SPLIT_CASE_URL], {
      relativeTo: this._route
    });
  }

  downloadExhibitDoc() {
    let url = apiUrls.createExhibitDocURL;
    if (this.isFurtheranceMode) {
      url = apiUrls.createFurtheranceDocumentURL;
      return url + '?cppFurtheranceSeq=' + String(this.cppFurtheranceSeq);
    } else {
      return url + '?contractPriceProfileSeq=' + String(this.contractPriceProfileId);
    }
  }

  buildPricingExhibit() {
    this.pricingExhibitModel.contractPriceProfileSeq = this.contractPriceProfileId;
    this.pricingExhibitModel.contractAgeementId = this.agreementId;
    this.pricingExhibitModel.contractTypeName = this.contractType;
  }

  buildFurtherancePricing() {
    this.furtheranceBaseModel.cppFurtheranceSeq = this.cppFurtheranceSeq;
    this.furtheranceBaseModel.agreementId = this.parentAgreementId;
    this.furtheranceBaseModel.contractType = this.parentContractType;
  }

  savePricingExhibit() {
    this.showSpinner = true;
    this.buildPricingExhibit();
    this._subscription.push(
      this._reviewService
        .savePricingExhibit(this.pricingExhibitModel)
          .subscribe( response => {
            this.returnToCLM();
        },
        error => {
          this.showSpinner = false;
      }));
  }

  saveFurtherancePricingDocument() {
    this.showSpinner = true;
    this.buildFurtherancePricing();
    this._subscription.push(
      this._reviewService
        .savePricingDocumentForFurtherance(this.furtheranceBaseModel)
          .subscribe(() => {
            this.returnToCLM();
        },
        error => {
          this.showSpinner = false;
      }));
  }

  returnToCLM() {
    this.showSpinner = true;
    this._subscription.push(
      this._reviewService.fetchClmUrl()
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
      }));
  }

  ngOnDestroy() {
    this._subscription.forEach(sub => sub.unsubscribe());
  }
}
