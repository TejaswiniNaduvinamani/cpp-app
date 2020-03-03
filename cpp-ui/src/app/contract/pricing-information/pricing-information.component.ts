import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import * as moment from 'moment';
import { NgbDateStruct, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

import { ContractInformationDetails, ClmContractDetails } from './../contract-information/contract-information.model';
import { DateUtils } from './../../shared/utils/date-utils';
import { ErrorService, PricingInformationService, StepperService, AuthorizationService,
   FurtheranceService, TranslatorService } from 'app/shared';
import { FurtheranceInformationModel } from 'app/furtherance';
import { PricingInformation } from './pricing-information.model';
import { STEPPER_URL, FURTHERANCE_STEPPER_CODE, IMAGE_PATH, DATE_FORMAT_MMDDYYYY, CONTRACT_TYPES,
   TOGGLE_QUESTION, DATE_FORMAT_PIPE, CALENDAR_TYPES, STEPPER_NUMBERS } from 'app/shared';
declare var $;

@Component({
  selector: 'app-pricing-information',
  templateUrl: './pricing-information.component.html',
  styleUrls: ['./pricing-information.component.scss']
})
export class PricingInformationComponent implements OnInit, OnDestroy, DoCheck {

  public priceVerificationLabel: string;
  public priceAuditLabel: string;
  public transferFeeLabel: string;
  public assessmentFeeLabel: string;
  public dateFormat = DATE_FORMAT_PIPE;
  public contractTypeIFS = CONTRACT_TYPES.IFS;
  public imageDir = IMAGE_PATH;
  private subscription: Subscription[] = [];
  public custInfoForm: FormGroup;
  public pricingEffectiveDate: NgbDateStruct;
  public furtheranceEffectiveDate: NgbDateStruct;
  public pricingExpirationDate: Date;
  public contractStartDate: Date;
  public contractEndDate: Date;
  public contractName: string;
  private now = new Date();
  public currentDate = { year: this.now.getFullYear(), month: this.now.getMonth() + 1, day: this.now.getDate() };
  public assessmentFeeVal: boolean;
  public transferFeeVal: boolean;
  public priceAuditVal: boolean;
  public priceVerificationVal: boolean;
  public pricingInformation = <PricingInformation>{};
  public displayViewMode: boolean;
  public contractPriceProfileId: number;
  public contractPriceProfileSeq: number;
  public agreementId: string;
  public contractType: string;
  public contractStatus: string;
  public showSpinner: boolean;
  public pricingExhibitSysId: string;
  public isPricingExhibitAttached: boolean;
  public versionNumber: number;
  public parentAgreementId: string;
  public isFurtheranceMode: boolean;
  public isAmendment: boolean;
  public furtheranceInformation= <FurtheranceInformationModel>{};
  public canEditFurtherance: boolean;

  constructor(
    private _authorizationService: AuthorizationService,
    private _pricingInformationService: PricingInformationService,
    private _furtheranceService: FurtheranceService,
    private _errorService: ErrorService,
    private _stepperService: StepperService,
    private _formBuilder: FormBuilder,
    private _router: Router,
    private _route: ActivatedRoute,
    private _NgbDateParserFormatter: NgbDateParserFormatter,
    private _translateService: TranslatorService
  ) { }

  ngOnInit() {
    this.showSpinner = true;
    this._errorService.checkErrorStatus(false);
    this.loadForm();
    this.agreementId = this._route.snapshot.queryParams['agreementId'];
    this.contractType = this._route.snapshot.queryParams['contractType'];
    if (this.agreementId && this.contractType) {
      this.setAgreementInfo();
    } else {
      this.fetchAgreementInfo();
    }
   this.fetchContractPriceProfileInfo();
  }

  fetchContractPriceProfileInfo() {
    this.subscription.push(this._pricingInformationService.fetchContractPriceProfileInfo(this.agreementId.trim(),
      this.contractType.trim())
      .subscribe(clmContractDetails => {
          this._pricingInformationService.setClmContractDetails(clmContractDetails);
          this.contractPriceProfileId = clmContractDetails.cppInformationDto.contractPriceProfileId;
          this.contractPriceProfileSeq = clmContractDetails.cppInformationDto.contractPriceProfileSeq;
          this.versionNumber = clmContractDetails.cppInformationDto.versionNumber;
          this.contractStatus = clmContractDetails.contractStatus;
          this.contractName = clmContractDetails.contractName;
          this.parentAgreementId = clmContractDetails.parentAgreementId;
          this.contractStartDate = clmContractDetails.contractStartDate;
          this.contractEndDate = clmContractDetails.contractEndDate;
          this.isAmendment = clmContractDetails.isAmendment;
          this.fetchAuthorizationDetails(clmContractDetails);
      }));
  }

  fetchAuthorizationDetails(clmContractDetails: ClmContractDetails) {
    this.subscription.push(this._authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileSeq),
      clmContractDetails.isAmendment, clmContractDetails.contractStatus)
      .subscribe(authorizationDetails => {
        this._authorizationService.setAuthorizationDetails(authorizationDetails);
        this.displayViewMode = authorizationDetails.priceProfileEditable ? false : true;
        this._stepperService.currentStep(STEPPER_NUMBERS.PRICING_INFORMATION);
        this.fetchPricingInformation(clmContractDetails);
      }));
  }

  fetchPricingInformation(clmContractDetails: ClmContractDetails) {
    this.subscription.push(this._pricingInformationService.fetchPricingInformation(
      String(this.contractPriceProfileSeq), this.agreementId.trim(), this.contractStatus)
      .subscribe(pricingInformation => {
        this.pricingEffectiveDate = DateUtils.convertDateToNgbDateStruct(pricingInformation.pricingEffectiveDate);
        this.custInfoForm.get('calendar').setValue(pricingInformation.scheduleForCostChange);
        this.priceVerificationVal = pricingInformation.priceVerificationFlag;
        this.priceAuditVal = pricingInformation.priceAuditFlag;
        this.transferFeeVal = pricingInformation.transferFeeFlag;
        this.assessmentFeeVal = pricingInformation.assessmentFeeFlag;
        this.pricingExhibitSysId = pricingInformation.pricingExhibitSysId;
        this.determineToggleQuestion();
        const contractInformation = JSON.parse(sessionStorage.getItem('contractInfo'));
        if (!pricingInformation.pricingEffectiveDate) {
          this._pricingInformationService.setPricingInformationStatus(false);
          contractInformation.isPricingSaved = false;
          const contractStartDate = moment.utc(this.contractStartDate).startOf('day').toDate();
          const currentDate = moment.utc(DateUtils.formatDate(this.currentDate)).startOf('day').toDate();
          this.pricingEffectiveDate = (contractStartDate > currentDate ) ? DateUtils.convertDateToNgbDateStruct(contractStartDate)
          : DateUtils.convertDateToNgbDateStruct(currentDate);
        } else {
          this._pricingInformationService.setPricingInformationStatus(true);
          contractInformation.isPricingSaved = true;
        }
        this.pricingExpirationDate = pricingInformation.pricingExpirationDate;
        contractInformation.pstdate = moment.utc(DateUtils.parseDate(this.pricingEffectiveDate), 'YYYY-MM-DD');
        contractInformation.penddate = moment.utc(this.pricingExpirationDate, 'YYYY-MM-DD');
        if (this.pricingExhibitSysId) {
          contractInformation.isPricingExhibitAttached = true;
        } else {
          contractInformation.isPricingExhibitAttached = false;
        }
        this.isFurtheranceMode = contractInformation.isFurtheranceMode;
        sessionStorage.setItem('contractInfo', JSON.stringify(contractInformation));
        this.showSpinner = false;
        if (this.isFurtheranceMode) {
          this.fetchFurtheranceInfo(contractInformation.parentAgreementId, contractInformation.cppFurtheranceSeq);
        }
      }));
  }

  fetchFurtheranceInfo(parentAgreementId: string, cppFurtheranceSeq: number) {
    this.showSpinner = true;
    this.subscription.push(this._furtheranceService.canEditFurtherance(cppFurtheranceSeq).subscribe( displayMode => {
      this.canEditFurtherance = displayMode.canEditFurtherance;
    }))

    this.subscription.push(this._furtheranceService.fetchFurtheranceInfo
      (parentAgreementId, cppFurtheranceSeq).subscribe(furtheranceInformation => {
      this.furtheranceInformation = furtheranceInformation;
      this._stepperService.determineFurtheranceStepperMode(this.furtheranceInformation.contractPriceProfileSeq);
      if (furtheranceInformation.furtheranceEffectiveDate) {
        this.furtheranceEffectiveDate = DateUtils.convertDateToNgbDateStruct(furtheranceInformation.furtheranceEffectiveDate);
        this.custInfoForm.get('furtheranceEffectiveDate').setValue(this.furtheranceEffectiveDate);
        const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
        contractInfo.furtherancedate = moment.utc(DateUtils.parseDate(this.furtheranceEffectiveDate), 'YYYY-MM-DD');
        sessionStorage.setItem('contractInfo', JSON.stringify(contractInfo));
      }
      this.custInfoForm.get('reasonForChange').setValue(furtheranceInformation.changeReasonTxt);
      this.custInfoForm.get('contractReference').setValue(furtheranceInformation.contractReferenceTxt);
      this.showSpinner = false
    }))
  }

  determineToggleQuestion() {
    if (this.priceVerificationVal || this.priceAuditVal) {
      this.custInfoForm.get('toggleQuestion').setValue(TOGGLE_QUESTION.YES);
    } else {
      this.custInfoForm.get('toggleQuestion').setValue(TOGGLE_QUESTION.NO);
    }
  }

  ngDoCheck() {
    this.priceAuditLabel = this._translateService.translate('CUST_INFO_LABEL.PRICE_AUDIT_LABEL');
    this.priceVerificationLabel = this._translateService.translate('CUST_INFO_LABEL.PRICE_VERIFICATION_LABEL');
    this.transferFeeLabel = this._translateService.translate('CUST_INFO_LABEL.TRANSFER_FEE_LABEL');
    this.assessmentFeeLabel = this._translateService.translate('CUST_INFO_LABEL.ASSESSMENT_FEE_LABEL');
  }

  loadForm() {
    this.custInfoForm = this._formBuilder.group({
      effectiveDate: new FormControl(null, [Validators.required, this.dateValidator.bind(this)]),
      calendar: new FormControl(CALENDAR_TYPES.FISCAL_CALENDAR),
      toggleQuestion: new FormControl('no'),
      priceVerificationToggle: this._formBuilder.group({
        selectedValue: [],
      }),
      priceAuditToggle: this._formBuilder.group({
        selectedValue: [],
      }),
      transferFeeToggle: this._formBuilder.group({
        selectedValue: [],
      }),
      assessmentFeeToggle: this._formBuilder.group({
        selectedValue: [],
      }),
      furtheranceEffectiveDate: new FormControl(null, [Validators.required, this.furtheranceDateValidator.bind(this)]),
      reasonForChange: new FormControl(null, [Validators.required]),
      contractReference: new FormControl(null, [Validators.required])
    });
  }

  setAgreementInfo() {
    let contractInfo;
    if (sessionStorage.contractInfo) {
      contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
      contractInfo.agreementId = this.agreementId;
      contractInfo.contractType = this.contractType;
    } else {
      contractInfo = {
        agreementId: this.agreementId.trim(),
        contractType: this.contractType.trim(),
        isFurtheranceMode: false
      }
    }
    sessionStorage.setItem('contractInfo', JSON.stringify(contractInfo));
  }

  fetchAgreementInfo() {
    const agreementInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.agreementId = agreementInfo.agreementId;
    this.contractType = agreementInfo.contractType;
  }

  dateValidator(control: FormControl) {
    const input = control.value;
    const pricingDate = moment.utc(DateUtils.formatDate(this.pricingEffectiveDate)).startOf('day').toDate();
    const contractEndDate = moment.utc(this.contractEndDate).endOf('day').toDate();
    const currentDate = moment.utc(DateUtils.formatDate(this.currentDate)).startOf('day').toDate();

    if (input && !this.isDateObject(input)) {
      return { 'invalidDateFormat': true };
    } else if (this.pricingEffectiveDate && this.contractStartDate &&
      this.isDateObject(input) && pricingDate.getTime() > contractEndDate.getTime()) {
      return { 'invalidPricingEffectiveDate': true };
    } else if (this.pricingEffectiveDate && this.currentDate &&
      this.isDateObject(input) && pricingDate.getTime() < currentDate.getTime()) {
      return { 'invalidPricingCurrentDate': true };
    } else {
      return null;
    }
  }

  furtheranceDateValidator(control: FormControl) {
    const input = control.value;
    const furtheranceDate = moment.utc(DateUtils.formatDate(this.furtheranceEffectiveDate)).startOf('day').toDate();
    const contractEndDate = moment.utc(this.contractEndDate).endOf('day').toDate();
    const contractStartDate = moment.utc(this.contractStartDate).startOf('day').toDate();
    const currentDate = moment.utc(DateUtils.formatDate(this.currentDate)).startOf('day').toDate();
    if (input && !this.isDateObject(input)) {
      return { 'invalidDateFormat': true };
    } else if (furtheranceDate.getTime() < currentDate.getTime()) {
      return { 'invalidFurtheranceCurrentDate': true };
    } else if (furtheranceDate.getTime() > contractEndDate.getTime()) {
      return { 'invalidFurtheranceContractDate': true };
    } else if (!this.isAmendment && (furtheranceDate.getTime() < contractStartDate.getTime())) {
      return { 'invalidFurtheranceContractStartDate': true };
    } else if (this.isAmendment && (furtheranceDate.getTime() < contractStartDate.getTime())) {
      return { 'invalidFurtheranceAmendmentStartDate': true };
    } else {
      return null;
    }
 }

  isDateObject(date: NgbDateStruct) {
    const dateString = (this._NgbDateParserFormatter.format(date));
    if (moment.utc(dateString, DATE_FORMAT_MMDDYYYY, true).isValid()) {
      return true;
    } else {
      return false;
    }
  }

  ontoggleQuestionNo() {
    this.priceVerificationVal = false;
    this.priceAuditVal = false;
    this.transferFeeVal = false;
    this.assessmentFeeVal = false;
  }

  onPriceVerificationChange(selectedValue) {
    this.priceVerificationVal = selectedValue;
    if (!this.priceVerificationVal && !this.priceAuditVal) {
      this.transferFeeVal = false;
      this.assessmentFeeVal = false;
    } else {
      this.transferFeeVal = true;
      this.assessmentFeeVal = true;
    }
  }

  validateAllFormFields() {
    Object.keys(this.custInfoForm.controls).forEach(control => {
      const formControl = this.custInfoForm.get(control);
      formControl.markAsTouched({ onlySelf: true });
    });
  }

  onPriceAuditChange(selectedValue) {
    this.priceAuditVal = selectedValue;
    if (!this.priceAuditVal && !this.priceVerificationVal) {
      this.transferFeeVal = false;
      this.assessmentFeeVal = false;
    } else {
      this.transferFeeVal = true;
      this.assessmentFeeVal = true;
    }
  }

  buildPricingInformation() {
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.pricingInformation.contractName = this.contractName.trim();
    this.pricingInformation.contractType = this.contractType.trim();
    this.pricingInformation.contractPriceProfileId = this.contractPriceProfileId;
    this.pricingInformation.pricingEffectiveDate = contractInfo.pstdate;
    this.pricingInformation.pricingExpirationDate = contractInfo.penddate;
    this.pricingInformation.scheduleForCostChange = this.custInfoForm.get('calendar').value;
    this.pricingInformation.priceVerificationFlag = this.priceVerificationVal;
    this.pricingInformation.priceAuditFlag = this.priceAuditVal;
    this.pricingInformation.transferFeeFlag = this.transferFeeVal;
    this.pricingInformation.assessmentFeeFlag = this.assessmentFeeVal;
    this.pricingInformation.contractPriceProfileSeq = this.contractPriceProfileSeq;
    this.pricingInformation.agreementId =  this.agreementId.trim();
    this.pricingInformation.clmContractStartDate = contractInfo.cstdate;
    this.pricingInformation.clmContractEndDate = contractInfo.cenddate;
    this.pricingInformation.versionNbr = this.versionNumber;
    this.pricingInformation.parentAgreementId = this.parentAgreementId;
  }

  savePricingDetails() {
    const contractInformation = JSON.parse(sessionStorage.getItem('contractInfo'));
    contractInformation.pstdate = moment.utc(DateUtils.parseDate(this.pricingEffectiveDate));
    sessionStorage.setItem('contractInfo', JSON.stringify(contractInformation));
  }

  onSubmit() {
    this.showSpinner = true;
    this.savePricingDetails();
    this.buildPricingInformation();
    this.subscription.push(this._pricingInformationService.savePricingInformation(this.pricingInformation)
        .subscribe(() => {
          this._pricingInformationService.setPricingInformationStatus(true);
          const contractInformation = JSON.parse(sessionStorage.getItem('contractInfo'));
          contractInformation.isPricingSaved = true;
          sessionStorage.setItem('contractInfo', JSON.stringify(contractInformation));
          this.showSpinner = false;
          this._router.navigate([STEPPER_URL.DISTRIBUTION_CENTERS_URL], {relativeTo: this._route});
        }, error => {
          this.showSpinner = false;
        }));
    }

    checkAttachedExhibit() {
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.isPricingExhibitAttached = contractInfo.isPricingExhibitAttached;
    if (this.custInfoForm.get('effectiveDate').valid) {
      if (!this.isPricingExhibitAttached) {
        this.onSubmit();
      } else {
        $('#deletePricingExhibit').modal('show');
      }
    } else {
    this.validateAllFormFields();
    }
  }

  onExhibitDeletion(isExhibitDeleted) {
    if (isExhibitDeleted) {
      this.showSpinner = false;
      this.onSubmit();
    } else {
      this.showSpinner = true;
    }
  }

  saveFurtheranceEffectiveDate() {
    const contractInformation = JSON.parse(sessionStorage.getItem('contractInfo'));
    contractInformation.furtherancedate = moment.utc(DateUtils.parseDate(this.furtheranceEffectiveDate));
    sessionStorage.setItem('contractInfo', JSON.stringify(contractInformation));
  }

  saveFurtherance() {
    this.saveFurtheranceEffectiveDate();
    this.showSpinner = true;
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.furtheranceInformation.furtheranceEffectiveDate = contractInfo.furtherancedate;
    this.furtheranceInformation.changeReasonTxt = this.custInfoForm.get('reasonForChange').value.trim();
    this.furtheranceInformation.contractReferenceTxt = this.custInfoForm.get('contractReference').value.trim();
    this.subscription.push(this._furtheranceService.saveFurtheranceInfo(this.furtheranceInformation)
    .subscribe(() => {
      this.showSpinner = false;
      this._router.navigate([STEPPER_URL.MARKUP_URL], { relativeTo: this._route });
      if (!this.furtheranceInformation.contractPriceProfileSeq) {
        this._stepperService.determineFurtheranceStepperMode(FURTHERANCE_STEPPER_CODE);
      }
    }, error => {
      this.showSpinner = false;
    }));
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }
}
