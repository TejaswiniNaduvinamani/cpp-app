import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';
import { Router, ActivatedRoute, Params } from '@angular/router';

import * as moment from 'moment';

import { ActivatedRouteStub } from './../../../../test/unit-testing/mock/activated-route-stub';
import { ContractInformationDetails, CPPInformationDetails, ClmContractDetails } from './../../contract';
import { CONTRACT_TYPES } from './../../shared/utils/app.constants';
import { DateUtils } from './../../shared/utils/date-utils';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { PricingInformationComponent } from './pricing-information.component';
import { PricingInformation } from './pricing-information.model';
import { PricingInformationService, StepperService, AuthorizationService, AuthorizationDetails,
  ErrorService, FurtheranceService} from './../../shared';
import { RouterStub } from './../../../../test/unit-testing/mock/router-stub';
import { TranslatorService } from 'app/shared/translate/translator.service';

class MockNgbDateParserFormatter {

  parse(value) {
    return { year: 2020, month: 3, day: 30 };
  }

  format(date) {
    return '03/30/2020'
  }
}

describe('Pricing-Information-Component', () => {

   // do not remove double quotes in SessionStorageInterface. Setting of session requires double quotes while parsing the string.
   const CONTRACT_DETAILS = {
    "agreementId": '75490bfc-289d-40d3-b644-ba095c2e1cea',
    "cenddate": new Date('12/12/2020'),
    "cname": 'TEST CONTRACT',
    "contractStatus": 'draft',
    "contractType": 'ICMDistributionAgreementRegional',
    "cppid": 12345,
    "cppseqid": 42915,
    "cstdate": new Date('12/12/2019'),
    "ctype": 'ICMDistributionAgreementRegional',
    "isAmendment": false,
    "isFurtheranceMode": true,
    "isPricingExhibitAttached": false,
    "penddate": new Date('12/12/2020'),
    "pstdate": new Date('12/12/2019'),
    "versionNumber": 1,
    "furtherancedate": '2019-07-23T00:00:00.000Z'
   }

  const PRICING_INFORMATION: PricingInformation = new PricingInformation('New Contract', CONTRACT_TYPES.IFS,
    121, new Date('01/01/2017'), new Date('01/31/2017'), 'fiscal', false, false, false, false,
    '200', 21, '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308', new Date('01/01/2017'), new Date('01/31/2017'), null, 1);

  const CONTRACT_INFO: ContractInformationDetails = new ContractInformationDetails('Contract', 'IFS',
  new Date('2019/02/20'), new Date('2019/02/29'), 'Yes', new Date('2019/02/20'), new Date('2019/02/29'),
  '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308', 'IFS', 'yes', true)

  const AUTH_DETAILS: AuthorizationDetails = new AuthorizationDetails(true, true, true, true, 'draft')

  const CPP_INFORMATION_DETAILS: CPPInformationDetails = new CPPInformationDetails(4521, 1, 321);

  const CLM_CONTRACT_DETAILS: ClmContractDetails = new ClmContractDetails('TEST CONTRACT', 'ICMDistributionAgreementRegional',
  new Date('05/07/2019'), new Date('05/07/2020'), 'Draft', 'f7577793-0fb3-45f6-9710-ddb79514c24a', null, false, CPP_INFORMATION_DETAILS);

  let component: PricingInformationComponent;
  let fixture: ComponentFixture<PricingInformationComponent>;
  let pricingInformationService: PricingInformationService;
  let authService: AuthorizationService;
  let furtheranceService: FurtheranceService;
  let stService: StepperService;
  let mockTranslateService;
  let router: Router;
  let route: ActivatedRoute;

  beforeEach(() => {

   mockTranslateService  = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PricingInformationComponent],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        { provide: NgbDateParserFormatter, useClass: MockNgbDateParserFormatter },
        { provide: TranslatorService, useValue: mockTranslateService },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParams: {
                subscribe: (fn: (value: Params) => void) => fn({
                  agreementId: '75490bfc-289d-40d3-b644-ba095c2e1cea',
                  contractType: 'ICMDistributionAgreementRegional'
                }),
              },
            },
          },
        },
        ErrorService,
        FormBuilder,
        FurtheranceService,
        StepperService,
        PricingInformationService,
        AuthorizationService,
      ]
    }).overrideTemplate(PricingInformationComponent, '');

    fixture = TestBed.createComponent(PricingInformationComponent);
    component = fixture.componentInstance;
    pricingInformationService = TestBed.get(PricingInformationService);
    authService = TestBed.get(AuthorizationService);
    stService = TestBed.get(StepperService);
    furtheranceService = TestBed.get(FurtheranceService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });


  it('should create the pricing information component', () => {

    // THEN
    expect(component).toBeTruthy();
  });

  it('should initialize a form group with controls', () => {
    // WHEN
    component.loadForm();
    // THEN
    expect(component.custInfoForm.contains('effectiveDate')).toBeTruthy();
    expect(component.custInfoForm.contains('calendar')).toBeTruthy();
    expect(component.custInfoForm.contains('toggleQuestion')).toBeTruthy();
    expect(component.custInfoForm.contains('priceVerificationToggle')).toBeTruthy();
    expect(component.custInfoForm.contains('priceAuditToggle')).toBeTruthy();
    expect(component.custInfoForm.contains('transferFeeToggle')).toBeTruthy();
    expect(component.custInfoForm.contains('assessmentFeeToggle')).toBeTruthy();
  });

  it('should fetch contract information', () => {

    // GIVEN
    component.loadForm();
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    component.contractType = 'MyContract';
    const CPP_INFORMATION_DTO = {
      'cppInformationDto' : {
        'contractPriceProfileId': 4213,
        'version': 1,
        'contractPriceProfileSeq': 123,
      }
    }
    spyOn(pricingInformationService, 'fetchContractPriceProfileInfo').and.returnValue(Observable.of(CPP_INFORMATION_DTO));
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of([]));
    spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));

    // WHEN
    component.fetchContractPriceProfileInfo();

    // THEN
    expect(pricingInformationService.fetchContractPriceProfileInfo).toHaveBeenCalled();
    expect(component.contractPriceProfileSeq).toBeTruthy();
    expect(component.showSpinner).toBeFalsy();

  });

  it('should validate date validator - required scenario', () => {

    // GIVEN
    component.loadForm();
    component.custInfoForm.get('effectiveDate').setValue('');

    // WHEN
    component.dateValidator(component.custInfoForm.controls['effectiveDate'].value);

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();
  });

  it('should validate date validator - when pricing effective date is greater than contractEnd date', () => {

    // GIVEN
    component.pricingEffectiveDate = { year: 2018, month: 6, day: 14 };
    component.contractEndDate = new Date(1535826600000);
    component.loadForm();
    component.custInfoForm.get('effectiveDate').setValue(component.pricingEffectiveDate);

    // WHEN
    component.dateValidator(component.custInfoForm.controls['effectiveDate'].value);

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();
  });

  it('should validate date validator - when pricing effective date is greater than current date', () => {

    // GIVEN
    component.pricingEffectiveDate = { year: 2018, month: 12, day: 26 };
    component.currentDate = { year: 2018, month: 12, day: 25 };
    component.loadForm();
    component.custInfoForm.get('effectiveDate').setValue(component.pricingEffectiveDate);

    // WHEN
    component.dateValidator(component.custInfoForm.controls['effectiveDate'].value);

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();
  });

  it('should validate furtherance date validator - for furtherance start date ', () => {

    // GIVEN
    component.furtheranceEffectiveDate = { year: 2018, month: 1, day: 31 };
    component.contractStartDate = new Date(1540924200000);
    component.loadForm();
    component.isAmendment = false;
    component.custInfoForm.get('furtheranceEffectiveDate').setValue(component.furtheranceEffectiveDate);

    // WHEN
    component.furtheranceDateValidator(component.custInfoForm.controls['furtheranceEffectiveDate'].value);

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();

    // GIVEN
    component.furtheranceEffectiveDate = { year: 2018, month: 1, day: 31 };
    component.contractStartDate = new Date(1540924200000);
    component.loadForm();
    component.isAmendment = true;
    component.custInfoForm.get('furtheranceEffectiveDate').setValue(component.furtheranceEffectiveDate);

    // WHEN
    component.furtheranceDateValidator(component.custInfoForm.controls['furtheranceEffectiveDate'].value);

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();
  });

  it('should validate toggles when selected as No', () => {

    // GIVEN
    component.priceVerificationVal = false;
    component.priceAuditVal = false;
    component.loadForm();
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(false);
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(false);
    component.custInfoForm.get('transferFeeToggle.selectedValue').setValue(false);
    component.custInfoForm.get('assessmentFeeToggle.selectedValue').setValue(false);

    // WHEN
    component.ontoggleQuestionNo();

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();

  });

  it('should validate PriceVerification', () => {

    // GIVEN
    component.loadForm();
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(false);

    // WHEN
    component.onPriceVerificationChange(false);

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();

  });

  it('should validate PriceVerification for true value', () => {

    // GIVEN
    component.loadForm();
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(true);

    // WHEN
    component.onPriceVerificationChange(true);

    // THEN
    expect(component.transferFeeVal).toBeTruthy();

  });

  it('should validate PriceAuditChange', () => {

    // GIVEN
    component.loadForm();
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(false);

    // WHEN
    component.onPriceAuditChange(false);

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();

  });

  it('should validate PriceAuditChange for true value', () => {

    // GIVEN
    component.loadForm();
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(true);

    // WHEN
    component.onPriceAuditChange(true);

    // THEN
    expect(component.transferFeeVal).toBeTruthy();

  });


  it('should validate pricing form fields', () => {

    // GIVEN
    component.loadForm();
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    component.contractName = 'MyContract';
    component.contractType = 'ICMDistributionAgreementRegional';
    component.pricingEffectiveDate =  { year: 2018, month: 1, day: 31 };

    // WHEN
    component.onSubmit();

    // THEN
    expect(component.custInfoForm.valid).toBeFalsy();

  });


  it('should navigate to distribution page when contract type is  not IFS', () => {

    // GIVEN
    component.loadForm();
    component.contractName = 'contractName';
    component.contractType = 'contractType';
    component.agreementId = 'agreementId';
    component.custInfoForm.get('effectiveDate').setValue('02/01/2020');
    component.custInfoForm.get('calendar').setValue(false);
    component.custInfoForm.get('toggleQuestion').setValue(false);
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(false);
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(false);
    component.custInfoForm.get('transferFeeToggle.selectedValue').setValue(false);
    component.custInfoForm.get('assessmentFeeToggle.selectedValue').setValue(false);
    component.pricingEffectiveDate =  { year: 2018, month: 1, day: 31 };
    spyOn(pricingInformationService, 'savePricingInformation').and.returnValue(Observable.of([PRICING_INFORMATION]));
    let spy = spyOn(router, 'navigate');
    component.pricingEffectiveDate =  { year: 2018, month: 1, day: 31 };

    // WHEN
    component.onSubmit();

    // THEN
    expect(spy).toHaveBeenCalledWith((['/distributioncenters']), { relativeTo: route });

  });

  it('should fetch pricing information for new contract', () => {
    // GIVEN
    component.loadForm();
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    const PRICING_INFORMATION_NEW_CONTRACT = {
      'contractName': '',
      'contractType': '',
      'contractPriceProfileId': 44,
      'pricingEffectiveDate': '',
      'pricingExpirationDate': '9999-01-01',
      'scheduleForCostChange': 'fiscal',
      'priceVerificationFlag': false,
      'priceAuditFlag': false,
      'transferFeeFlag': false,
      'assessmentFeeFlag': false,
      'isExhibitAttached': false,
      'pricingExhibitSysId': ''
    }
    spyOn(pricingInformationService, 'fetchPricingInformation').and.returnValue(Observable.of(PRICING_INFORMATION_NEW_CONTRACT));

    // WHEN
    component.fetchPricingInformation(CLM_CONTRACT_DETAILS);

    // THEN
    expect(pricingInformationService.fetchPricingInformation).toHaveBeenCalled();
    expect(component.showSpinner).toBeTruthy();
  });

  it('should fetch pricing information for existing contract', () => {
    // GIVEN
    component.loadForm();
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    const PRICING_INFORMATION_EXISTING_CONTRACT = {
      'contractName': '',
      'contractType': '',
      'contractPriceProfileId': 44,
      'pricingEffectiveDate': '2018-01-01',
      'pricingExpirationDate': '9999-01-01',
      'scheduleForCostChange': 'fiscal',
      'priceVerificationFlag': false,
      'priceAuditFlag': false,
      'transferFeeFlag': false,
      'assessmentFeeFlag': false,
      'isExhibitAttached': false,
      'pricingExhibitSysId': '120ef-1456f'
    }
    spyOn(pricingInformationService, 'fetchPricingInformation').and.returnValue(Observable.of(PRICING_INFORMATION_EXISTING_CONTRACT));

    // WHEN
    component.fetchPricingInformation(CLM_CONTRACT_DETAILS);

    // THEN
    expect(pricingInformationService.fetchPricingInformation).toHaveBeenCalled();
    expect(component.showSpinner).toBeTruthy();
  });

  it('should set current date as pricing effective date when contract start date is today or before', () => {

    // GIVEN
    component.loadForm();
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    const CLM_CONTRACT_DETAILS_WITH_CURRENT_DATE: ClmContractDetails = {
      contractName: 'name',
      contractType: 'IFS',
      contractStartDate: new Date(1493840000000),
      contractEndDate: new Date(1593840000000),
      contractStatus: 'draft',
      agreementId: '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308',
      parentAgreementId: 'adfsf465-435retfg-4543fgf',
      isAmendment: false,
      cppInformationDto: CPP_INFORMATION_DETAILS
    }
    spyOn(pricingInformationService, 'fetchPricingInformation').and.returnValue(Observable.of([PRICING_INFORMATION]));

    // WHEN
    component.fetchPricingInformation(CLM_CONTRACT_DETAILS_WITH_CURRENT_DATE);

    // THEN
    expect(pricingInformationService.fetchPricingInformation).toHaveBeenCalled();
    expect(moment.utc(DateUtils.parseDate(component.pricingEffectiveDate)).format('YYYY-MM-DD'))
    .toBe(moment.utc(new Date()).format('YYYY-MM-DD'));
  });

  it('should set contract start date as pricing effective date when contract start date is in future', () => {

    // GIVEN
    component.loadForm();
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    const CLM_CONTRACT_DETAILS_WITH_FUTURE_DATE: ClmContractDetails = {
      contractName: 'name',
      contractType: 'IFS',
      contractStartDate: new Date(1543840000000),
      contractEndDate: new Date(1593840000000),
      contractStatus: 'draft',
      agreementId: '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308',
      parentAgreementId: 'adfsf465-435retfg-4543fgf',
      isAmendment: false,
      cppInformationDto: CPP_INFORMATION_DETAILS
    }
    spyOn(pricingInformationService, 'fetchPricingInformation').and.returnValue(Observable.of( { contractName: 'name',
      pricingEffectiveDate: null,
      pricingExpirationDate: new Date(),
      scheduleForCostChange: 'price model',
      priceVerificationFlag: false,
      priceAuditFlag: false,
      transferFeeFlag: false,
      assessmentFeeFlag: false,
      pricingExhibitSysId: 'ac123',
    }));

      component.contractStartDate = new Date(1543840000000);

    // WHEN
    component.fetchPricingInformation(CLM_CONTRACT_DETAILS_WITH_FUTURE_DATE);

    // THEN
    expect(pricingInformationService.fetchPricingInformation).toHaveBeenCalled();
    expect(moment.utc(DateUtils.parseDate(component.pricingEffectiveDate)).format('YYYY-MM-DD'))
    .toBe(moment.utc(new Date(1543840000000)).format('YYYY-MM-DD'));
  });

  it('should build Pricing Information', () => {
    // GIVEN
    component.loadForm();
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    component.contractName = 'MyContract';
    component.contractType = 'ICMDistributionAgreementRegional';

    // WHEN
    component.buildPricingInformation();

    // THEN
    expect(component.buildPricingInformation).toBeTruthy();
  });

  it('should fetch agreement Info', () => {
    // GIVEN
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_INFO));
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    component.contractType = 'ICMDistributionAgreementRegional';


    // WHEN
    component.fetchAgreementInfo();

    // THEN
    expect(component.fetchAgreementInfo).toBeTruthy();
  });


 it('should save Pricing Details', () => {
    // GIVEN
    const AGREEMENT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'),  'draft', new Date('01/30/2020'));
    component.pricingEffectiveDate =  { year: 2018, month: 1, day: 31 };

    sessionStorage.setItem('contractInfo', JSON.stringify(AGREEMENT_DETAILS));

    // WHEN
    component.savePricingDetails();

    // THEN
    expect(sessionStorage).toBeTruthy();
  });

  it('should check ngDoCeck', () => {

    // WHEN
    component.ngDoCheck();

    // THEN
    expect(mockTranslateService.translate).toHaveBeenCalled();

  });

  it('should navigate to distribution center if exhibit not attached', () => {
    // GIVEN
    component.loadForm();
    component.contractName = 'contractName';
    component.contractType = 'contractType';
    component.agreementId = 'agreementId';
    component.custInfoForm.get('effectiveDate').setValue('02/01/2020');
    component.custInfoForm.get('calendar').setValue(false);
    component.custInfoForm.get('toggleQuestion').setValue(false);
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(false);
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(false);
    component.custInfoForm.get('transferFeeToggle.selectedValue').setValue(false);
    component.custInfoForm.get('assessmentFeeToggle.selectedValue').setValue(false);
    component.custInfoForm.get('furtheranceEffectiveDate').setValue('02/01/2020');
    component.custInfoForm.get('reasonForChange').setValue('Reason here');
    component.custInfoForm.get('contractReference').setValue('reference here');

    spyOn(pricingInformationService, 'savePricingInformation').and.returnValue(Observable.of([PRICING_INFORMATION]));
    component.pricingExhibitSysId = '';
    let spy = spyOn(router, 'navigate');
    component.pricingEffectiveDate =  { year: 2018, month: 1, day: 31 };

    // WHEN
    component.checkAttachedExhibit();

    // THEN
    expect(component.showSpinner).toBeFalsy();
    expect(spy).toHaveBeenCalledWith((['/distributioncenters']), { relativeTo: route });
  });

  it('should validate all form fields if cust info form is not valid', () => {
    // GIVEN
    component.loadForm();
    component.contractName = 'contractName';
    component.contractType = 'contractType';
    component.agreementId = 'agreementId';
    component.custInfoForm.get('effectiveDate').setValue('');
    component.custInfoForm.get('calendar').setValue(null);
    component.custInfoForm.get('toggleQuestion').setValue(null);
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(null);
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(null);
    component.custInfoForm.get('transferFeeToggle.selectedValue').setValue(null);
    component.custInfoForm.get('assessmentFeeToggle.selectedValue').setValue(null);
    spyOn(pricingInformationService, 'savePricingInformation').and.returnValue(Observable.of([PRICING_INFORMATION]));
    component.pricingExhibitSysId = '';

    // WHEN
    component.checkAttachedExhibit();

    // THEN
    expect(component.validateAllFormFields).toBeTruthy();
  });

  it('should check if exhibit is attached', () => {
    // GIVEN
    component.loadForm();
    component.contractName = 'contractName';
    component.contractType = 'contractType';
    component.agreementId = 'agreementId';
    component.custInfoForm.get('effectiveDate').setValue('02/01/2020');
    component.custInfoForm.get('calendar').setValue(false);
    component.custInfoForm.get('toggleQuestion').setValue(false);
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(false);
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(false);
    component.custInfoForm.get('transferFeeToggle.selectedValue').setValue(false);
    component.custInfoForm.get('assessmentFeeToggle.selectedValue').setValue(false);
    spyOn(pricingInformationService, 'savePricingInformation').and.returnValue(Observable.of([PRICING_INFORMATION]));
    component.pricingExhibitSysId = '';
    component.pricingEffectiveDate =  { year: 2018, month: 1, day: 31 };

    // WHEN
    component.checkAttachedExhibit();
  });

  it('should fetch exhibit delete response', () => {
    // GIVEN
    component.loadForm();
    component.contractName = 'contractName';
    component.contractType = 'contractType';
    component.agreementId = 'agreementId';
    component.custInfoForm.get('effectiveDate').setValue('02/01/2020');
    component.custInfoForm.get('calendar').setValue(false);
    component.custInfoForm.get('toggleQuestion').setValue(false);
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(false);
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(false);
    component.custInfoForm.get('transferFeeToggle.selectedValue').setValue(false);
    component.custInfoForm.get('assessmentFeeToggle.selectedValue').setValue(false);

    // WHEN
    component.onExhibitDeletion(false);
  });

  it('should fetch exhibit delete response for valid cust info form', () => {
    // GIVEN
    component.loadForm();
    component.contractName = 'contractName';
    component.contractType = 'contractType';
    component.agreementId = 'agreementId';
    component.custInfoForm.get('effectiveDate').setValue('02/01/2020');
    component.custInfoForm.get('calendar').setValue(false);
    component.custInfoForm.get('toggleQuestion').setValue(false);
    component.custInfoForm.get('priceVerificationToggle.selectedValue').setValue(false);
    component.custInfoForm.get('priceAuditToggle.selectedValue').setValue(false);
    component.custInfoForm.get('transferFeeToggle.selectedValue').setValue(false);
    component.custInfoForm.get('assessmentFeeToggle.selectedValue').setValue(false);
    component.pricingEffectiveDate =  { year: 2018, month: 1, day: 31 };

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(component.onSubmit).toBeTruthy();
  });

  it('should fetch furtherance information', () => {
    // GIVEN
    component.loadForm();
    const FURTHERANCE_INFORMATION = {
      cppFurtheranceSeq: 123,
      contractPriceProfileSeq: 234,
      furtheranceStatusCode: 12,
      parentCLMAgreementId: 'f7577793-0fb3-45f6-9710-ddb79514c24a',
      furtheranceEffectiveDate: new Date(1540924200000),
      changeReasonTxt: 'reason',
      contractReferenceTxt: 'reference'
    }
    spyOn(furtheranceService, 'canEditFurtherance').and.returnValue(Observable.of({canEdittFurtherance: true}));
    spyOn(furtheranceService, 'fetchFurtheranceInfo').and.returnValue(Observable.of(FURTHERANCE_INFORMATION ));

    // WHEN
    component.fetchFurtheranceInfo('f7577793-0fb3-45f6-9710-ddb79514c24a', 123);

    // THEN
    expect(component.furtheranceInformation).toBeDefined();
    expect(component.custInfoForm.get('reasonForChange').value).toBe('reason');
    expect(component.custInfoForm.get('contractReference').value).toBe('reference');
    expect(component.custInfoForm.get('furtheranceEffectiveDate').value.year).toBe(2018);
    expect(component.custInfoForm.get('furtheranceEffectiveDate').value.month).toBe(10);
    expect(component.custInfoForm.get('furtheranceEffectiveDate').value.day).toBeGreaterThanOrEqual(11);
    expect(furtheranceService.canEditFurtherance).toHaveBeenCalledWith(FURTHERANCE_INFORMATION.cppFurtheranceSeq);
    expect(component.showSpinner).toBeFalsy();
  });

  it('should save furtherance information', () => {
    // GIVEN
    component.loadForm();
    component.custInfoForm.get('reasonForChange').setValue('reason');
    component.custInfoForm.get('contractReference').setValue('reference');
    const FURTHERANCE_INFORMATION = {
      cppFurtheranceSeq: 123,
      contractPriceProfileSeq: 234,
      furtheranceStatusCode: 12,
      parentCLMAgreementId: 'f7577793-0fb3-45f6-9710-ddb79514c24a',
      furtheranceEffectiveDate: new Date('12/12/2020'),
      changeReasonTxt: 'reason',
      contractReferenceTxt: 'reference'
    }
    let spy = spyOn(router, 'navigate');
    spyOn(furtheranceService, 'saveFurtheranceInfo').and.returnValue(Observable.of(FURTHERANCE_INFORMATION));

    // WHEN
    component.saveFurtherance();

    // THEN
    expect(spy).toHaveBeenCalledWith((['/markup']), { relativeTo: route });
    expect(furtheranceService.saveFurtheranceInfo).toHaveBeenCalled();
  })
})

describe('Pricing-Information-Component - Initialization Test', () => {

  let component: PricingInformationComponent;
  let fixture: ComponentFixture<PricingInformationComponent>;
  let pricingInformationService: PricingInformationService;
  let authService: AuthorizationService;
  let mockTranslateService;
  let router: Router;
  let route: ActivatedRoute;

  beforeEach(() => {

  mockTranslateService  = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);

   TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PricingInformationComponent],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        { provide: NgbDateParserFormatter, useClass: MockNgbDateParserFormatter },
        { provide: TranslatorService, useValue: mockTranslateService },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParams: {
                  agreementId: '75490bfc-289d-40d3-b644-ba095c2e1cea',
                  contractType: 'ICMDistributionAgreementStreetAmendment'
              },
            },
          },
        },
        ErrorService,
        FurtheranceService,
        FormBuilder,
        PricingInformationService,
        AuthorizationService,
        StepperService
      ]
    }).overrideTemplate(PricingInformationComponent, '');

    fixture = TestBed.createComponent(PricingInformationComponent);

    component = fixture.componentInstance;
    pricingInformationService = TestBed.get(PricingInformationService);
    authService = TestBed.get(AuthorizationService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);

  });

  it('should check ngOnInit for contract type to be ICMDistributionAgreementStreetAmendment', () => {
    // GIVEN
    component.contractName = 'MyContract';

    // WHEN
    component.ngOnInit();

    // THEN
    expect(component.showSpinner).toBeTruthy();
    expect(component.agreementId).toBe('75490bfc-289d-40d3-b644-ba095c2e1cea');
    expect(component.contractType).toBe('ICMDistributionAgreementStreetAmendment');
  });
})




