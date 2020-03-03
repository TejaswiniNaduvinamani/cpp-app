import { Component, OnInit, Renderer2 } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { Subscription } from 'rxjs/Subscription';
import { Router, ActivatedRoute } from '@angular/router';

import { StepperService, STEPPER_NUMBERS, STEPPER_URL, IMAGE_PATH, UNIT_TYPES, ZERO_VALUE, FURTHERANCE_STEPPER_CODE,
  AuthorizationService, FurtheranceService } from './../../shared';
import { SplitCaseFeeService } from './../../shared';
import { SplitCaseFees, SplitCaseList, SplitCaseViewModel, SaveFurtheranceSplitCaseModel  } from './split-case-fee.model';
declare var $;

const MIN_SPLITCASE_FEE = 5.00;
const MAX_SPLITCASE_FEE = 49.00;
const SPLITCASE_PATTERN = /^([0-9]{0,})(([.]{1})([0-9]{1,}))?$/;

@Component({
  selector: 'app-split-case',
  templateUrl: './split-case.component.html',
  styleUrls: ['./split-case.component.scss']
})
export class SplitCaseComponent implements OnInit {
  public selected = [];
  public rows = [];
  public imageDir = IMAGE_PATH;
  private _subscription: Subscription[] = [];
  public splitCaseFee: SplitCaseFees[] = [];
  public SplitCaseFees = <SplitCaseFees>{};
  public splitCaseList = <SplitCaseList>{};
  public viewSplitcCaseRow: SplitCaseViewModel[] = [];
  public splitCaseFurtheranceList = <SaveFurtheranceSplitCaseModel>{};
  public displayViewMode = false;
  public mode: string;
  public contractPriceProfileSeq: string;
  public effectiveDate: Date;
  public expirationDate: Date;
  public splitCaseNonNumericError: boolean;
  public spitCaseInvalidError: boolean;
  public showSpinner: boolean;
  public isSaveGrayed = false;
  public isPricingExhibitAttached: boolean;
  public feeTypeValue = 2;
  public isFurtheranceMode: boolean;
  public cppFurtheranceSeq: number;
  public canEditFurtherance: boolean;

  constructor(
    private _authorizationService: AuthorizationService,
    private _decimalPipe: DecimalPipe,
    private _stepperService: StepperService,
    public _renderer: Renderer2,
    private _router: Router,
    private _route: ActivatedRoute,
    private _splitCaseService: SplitCaseFeeService,
    private _furtheranceService: FurtheranceService
  ) { }

  ngOnInit() {
    this.showSpinner = true;
    this.fetchSessionInformation();
  }

  fetchSessionInformation() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractPriceProfileSeq = contractDetails.cppseqid;
    this.effectiveDate = contractDetails.pstdate;
    this.expirationDate = contractDetails.penddate;
    this.isPricingExhibitAttached = contractDetails.isPricingExhibitAttached;
    this.isFurtheranceMode = contractDetails.isFurtheranceMode;
    this.getProductTypes(contractDetails);
    if (this.isFurtheranceMode) {
    this.cppFurtheranceSeq = contractDetails.cppFurtheranceSeq;
      this._subscription.push(this._furtheranceService.canEditFurtherance(this.cppFurtheranceSeq).subscribe(displayMode => {
        this.canEditFurtherance = displayMode.canEditFurtherance;
      }))
      this._stepperService.determineFurtheranceStepperMode(FURTHERANCE_STEPPER_CODE)
  }}

  getProductTypes(contractDetails) {
    this._subscription.push(this._authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileSeq),
      contractDetails.isAmendment, contractDetails.contractStatus)
      .subscribe(authorizationDetails => {
        this._authorizationService.setAuthorizationDetails(authorizationDetails);
        this._stepperService.currentStep(STEPPER_NUMBERS.SPLIT_CASE);
        this.displayViewMode = authorizationDetails.priceProfileEditable ? false : true;
        this._subscription.push(this._splitCaseService.fetchProductTypes(this.contractPriceProfileSeq,
          this.effectiveDate, this.expirationDate)
          .subscribe(response => {
            this.rows = response;
            this.feeTypeValue = response[0].lessCaseRuleId;
            if (this.displayViewMode) {
              this.createViewSplitCaseFee(response);
            }
            this.showSpinner = false;
          }));
      }));
  }

  onblurSplitcase(event, row) {
    event.target.value = (event.target.value).trim();
    row.splitCaseFee = event.target.value;
    if (!SPLITCASE_PATTERN.test(event.target.value) && (event.target.value)) {
      this.setSplitCaseFeeValidationFlag(event, row, false, true);
    } else {
      const spliCaseFeeVal = event.target.value ? this._decimalPipe.transform(event.target.value, '1.2-2') : '';
      const splitCaseVal = (spliCaseFeeVal).split('.');
      if (splitCaseVal[0] && splitCaseVal[0].length > 2 ) {
        this.setSplitCaseFeeValidationFlag(event, row, true, false);
      } else {
        this.roundOffSplitCaseFee(event, row);
        if (!event.target.value) {
          row.splitCaseFee = ZERO_VALUE;
          this._renderer.setProperty(event.target, 'value', ZERO_VALUE);
        }
        this.setSplitCaseFeeValidationFlag(event, row, false, false);
      }
    }
    this.splitCaseNonNumericError = this.rows.some(element => element.isNonNumericSplitCaseFee);
    this.spitCaseInvalidError = this.rows.some(element => element.invalidSplitCaseFee);
  }

  isSaveGrayedOut() {
  return this.isSaveGrayed = (this.rows.some(element => element.invalidSplitCaseFee) ||
      this.rows.some(element => element.isNonNumericSplitCaseFee));
  }

  roundOffSplitCaseFee(event, row) {
    const spliCaseFeeVal = event.target.value ? this._decimalPipe.transform(event.target.value, '1.2-2') : '';
    row.splitCaseFee = spliCaseFeeVal.replace(/,/g, '');
    this._renderer.setProperty(event.target, 'value', spliCaseFeeVal.replace(/,/g, ''));
  }

  setSplitCaseFeeValidationFlag(event, row, invalidSplitCaseFee, isNonNumericSplitCaseFee) {
    row.isNonNumericSplitCaseFee = isNonNumericSplitCaseFee;
    row.invalidSplitCaseFee = invalidSplitCaseFee;
    if (invalidSplitCaseFee || isNonNumericSplitCaseFee) {
      this._renderer.addClass(event.target, 'splitcasefee-error');
    } else {
      this._renderer.removeClass(event.target, 'splitcasefee-error');
    }
  }

  validateData(row) {
    if (
      (row.splitCaseFee > MIN_SPLITCASE_FEE && row.unit === UNIT_TYPES.DOLLAR) ||
      (row.splitCaseFee > MAX_SPLITCASE_FEE && row.unit === UNIT_TYPES.PERCENT)
    ) {
      return true;
    } else {
      return false;
    }
  }

  onUnitClick(event, row) {
    row.unit = event.target.value;
  }

  copyRow(event, row) {
    const selectedSplitCaseFee = row.splitCaseFee;
    const selectedUnit = row.unit;
    const unit = row.unit;
    this.rows.forEach((splitCaseRowElement, index) => {
      splitCaseRowElement.splitCaseFee = selectedSplitCaseFee;
      splitCaseRowElement.unit = selectedUnit;
      if (row.invalidSplitCaseFee || row.isNonNumericSplitCaseFee) {
        splitCaseRowElement.invalidSplitCaseFee = row.invalidSplitCaseFee;
        splitCaseRowElement.isNonNumericSplitCaseFee = row.isNonNumericSplitCaseFee;
        this._renderer.addClass(document.getElementsByClassName('splitcase')[index], 'splitcasefee-error');
      } else {
        this._renderer.removeClass(document.getElementsByClassName('splitcase')[index], 'splitcasefee-error');
        splitCaseRowElement.invalidSplitCaseFee = false;
        splitCaseRowElement.isNonNumericSplitCaseFee = false;
      }
    });
    this.splitCaseNonNumericError = this.rows.some(element => element.isNonNumericSplitCaseFee);
    this.spitCaseInvalidError = this.rows.some(element => element.invalidSplitCaseFee);
  }

  buildSplitCaseFees() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    const cppSeqId = Number(contractDetails.cppseqid);
    this.isFurtheranceMode ? this.splitCaseFurtheranceList.contractPriceProfileSeq = cppSeqId
     : this.splitCaseList.contractPriceProfileSeq = cppSeqId;
    this.rows.forEach(splitcaseItem => {
      splitcaseItem.lessCaseRuleId = this.feeTypeValue;
    });
    this.isFurtheranceMode ? this.splitCaseFurtheranceList.splitCaseFeeValues = this.rows :
     this.splitCaseList.splitCaseFeeValues = this.rows;
    if ( this.isFurtheranceMode ) {
      this.splitCaseFurtheranceList.cppFurtheranceSeq = contractDetails.cppFurtheranceSeq;
    }
  }

  checkAttachedPricingExhibit() {
    if (!this.splitCaseNonNumericError && !this.spitCaseInvalidError) {
      if (this.isPricingExhibitAttached  && !this.isFurtheranceMode) {
        $('#deletePricingExhibit').modal('show');
      } else {
        this.saveSplitCaseFee();
      }
    }
  }

  onExhibitDeletion(isExhibitDeleted) {
    if (isExhibitDeleted) {
      this.showSpinner = false;
      this.saveSplitCaseFee();
    } else {
      this.showSpinner = true;
    }
  }

  saveSplitCaseFee() {
    this.showSpinner = true;
    this.buildSplitCaseFees();
    if (this.isFurtheranceMode) {
      this.saveSplitCaseFeeForFurtherance();
      } else {
        this._subscription.push(this._splitCaseService
        .saveSplitCaseFee(this.splitCaseList).subscribe(() => {
          this.navigateToReviewStepper();
      }, error => {
        this.showSpinner = false;
      }));
    }
  }

  saveSplitCaseFeeForFurtherance() {
    this._subscription.push(this._furtheranceService.saveSplitCaseFee(this.splitCaseFurtheranceList)
    .subscribe(() => {
        this.navigateToReviewStepper();
    }, error => {
      this.showSpinner = false;
    }));
  }

  navigateToReviewStepper() {
    this.showSpinner = false;
    this._router.navigate([STEPPER_URL.REVIEW_URL], { relativeTo: this._route});
  }

  createViewSplitCaseFee(splitCaseFeeList) {
    this.viewSplitcCaseRow = splitCaseFeeList.map(splitCaseObj => {
      const viewSplitCaseObj = {};
      viewSplitCaseObj['productType'] = splitCaseObj.productType;
      viewSplitCaseObj['itemPriceId'] = splitCaseObj.itemPriceId;
      viewSplitCaseObj['effectiveDate'] = splitCaseObj.effectiveDate;
      viewSplitCaseObj['expirationDate'] = splitCaseObj.expirationDate;
      if (splitCaseObj.unit === UNIT_TYPES.PERCENT) {
        viewSplitCaseObj['splitCaseFee'] = splitCaseObj.splitCaseFee + splitCaseObj.unit;
      } else {
        viewSplitCaseObj['splitCaseFee'] = splitCaseObj.unit + splitCaseObj.splitCaseFee;
      }
      return viewSplitCaseObj;
    });
  }
}
