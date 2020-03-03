import { ActivatedRoute, Router, Params } from '@angular/router';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';
import * as moment from 'moment';

import { ContractInformationComponent } from './contract-information.component';
import { ContractInformationDetails, CPPInformationDetails, ClmContractDetails } from './contract-information.model';
import { RouterStub } from '../../../../test/unit-testing/mock/router-stub';
import { PricingInformationService, StepperService, ERROR_TYPES } from './../../shared';

import { CONTRACT_TYPES, CONTRACT_TYPE_DISPLAY_NAMES, DATE_FORMAT_MMDDYYYY } from './../../shared/utils/app.constants';

describe('Contract Information Bar', () => {

  const CPP_INFORMATION_DETAILS: CPPInformationDetails = new CPPInformationDetails(4521, 1, 321);

  const CLM_CONTRACT_DETAILS: ClmContractDetails = new ClmContractDetails('TEST CONTRACT', 'ICMDistributionAgreementRegional',
    new Date('05/07/2019'), new Date('05/07/2020'), 'Draft', 'f7577793-0fb3-45f6-9710-ddb79514c24a', null, false, CPP_INFORMATION_DETAILS );

  const CONTRACT_DETAILS = {
      agreementId: '75490bfc-289d-40d3-b644-ba095c2e1cea',
      cenddate: '12/30/2020',
      cname: 'My Contract',
      contractStatus: 'Draft',
      contractType: 'ICMDistributionAgreementStreet',
      cppid: 4,
      cppseqid: 21,
      cstdate: '12/30/2018',
      ctype: 'ICMDistributionAgreementRegional',
      penddate: '12/30/2020',
      pstdate: '12/30/2018',
      isFurtheranceMode: true,
      furtherancedate: '01/01/2021',
      versionNumber: 2
  }

  let component: ContractInformationComponent;
  let fixture: ComponentFixture<ContractInformationComponent>;
  let service: StepperService;
  let pricingInformationService: PricingInformationService;
  let mockActivatedRoute;

  beforeEach(() => {

    mockActivatedRoute = jasmine.createSpyObj<ActivatedRoute>('activatedRoute', ['snapshot', 'queryParams']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContractInformationComponent],
      providers: [
        PricingInformationService,
        { provide: Router, useClass: RouterStub },
        StepperService,
        {
          provide: ActivatedRoute,
          useValue: {
              snapshot: {
                queryParams: {
                  'agreementId': '75490bfc-289d-40d3-b644-ba095c2e1cea',
                  'contractType': 'ICMDistributionAgreementRegional'
                },
              },
          },
        }
      ]
    }).overrideTemplate(ContractInformationComponent, '')

    fixture = TestBed.createComponent(ContractInformationComponent);
    component = fixture.componentInstance;
    service = TestBed.get(StepperService);
    pricingInformationService = TestBed.get(PricingInformationService);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create the Contract Information Component', () => {

    // THEN
    expect(component).toBeTruthy();
  });

  it('should fetch Contract Details', () => {

    // GIVEN
    component.agreementId = '75490bfc-289d-40d3-b644-ba095c2e1cea';
    component.contractType = 'ICMDistributionAgreementRegional';
    spyOn(pricingInformationService, 'getClmContractDetails').and.returnValue(Observable.of(CLM_CONTRACT_DETAILS));

    // WHEN
    component.fetchContractDetails();

    // THEN
    expect(pricingInformationService.getClmContractDetails).toHaveBeenCalled();
  });

  it('should test invalid stepper value ', () => {

    // GIVEN
    spyOn(service, 'getCurrentStep').and.returnValue(Observable.of([0]));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(component.contractName).toBe(undefined);
  });

  it('should test valid stepper value and fetch pricing dates', () => {

    // GIVEN
    spyOn(service, 'getCurrentStep').and.returnValue(Observable.of([2]));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(component.contractInfoInd).toBe(true);
  });

  it('should get CLM Contract Details', () => {

    // GIVEN
    spyOn(pricingInformationService, 'getClmContractDetails').and.returnValue(Observable.of(CLM_CONTRACT_DETAILS));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(pricingInformationService.getClmContractDetails).toHaveBeenCalled();
  });

  it('should fetch Agreement Info', () => {
    // GIVEN
    const AGREEMENT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'), 'draft',
    new Date('01/26/2020'), new Date('01/30/2020'), '123', 'edit', '2');

    sessionStorage.setItem('contractInfo', JSON.stringify(AGREEMENT_DETAILS));

    // WHEN
    component.fetchAgreementInfo();
    component.agreementId = '75490bfc-289d-40d3-b644-ba095c2e1cea';

    // THEN
    expect(component.agreementId).toBe('75490bfc-289d-40d3-b644-ba095c2e1cea');
  });

  it('should update Contract Information', () => {
    // GIVEN
    const CLMCONTRACT_DETAILS: ClmContractDetails = {
      contractName: 'Test Contract',
      contractType: 'ICMDistributionAgreementRegional',
      contractEndDate: new Date('05/07/2020'),
      contractStartDate: new Date('05/07/2019'),
      contractStatus: 'Draft',
      agreementId: '75490bfc-289d-40d3-b644-ba095c2e1cea',
      parentAgreementId: null,
      isAmendment: true,
      cppInformationDto: CPP_INFORMATION_DETAILS
    };
    // WHEN
    component.updateContractInformation(CLMCONTRACT_DETAILS);

    // THEN
    expect(component.contractName).toBe('Test Contract');
    expect(component.contractType).toBe('ICMDistributionAgreementRegional');
  });

  it('should determine contract type', () => {
    // GIVEN
    component.contractType = (CONTRACT_TYPES.DAN || CONTRACT_TYPES.DAN_AMENDMENT);

    // WHEN
    component.determineContractName();

    // THEN
    expect(component.contractTypeDisplayName).toBe(CONTRACT_TYPE_DISPLAY_NAMES.DAN);

     // GIVEN
     component.contractType = (CONTRACT_TYPES.DAR || CONTRACT_TYPES.DAR_AMENDMENT);

     // WHEN
     component.determineContractName();

     // THEN
     expect(component.contractTypeDisplayName).toBe(CONTRACT_TYPE_DISPLAY_NAMES.DAR);

     // GIVEN
     component.contractType = (CONTRACT_TYPES.IFS || CONTRACT_TYPES.IFS_AMENDMENT);

     // WHEN
     component.determineContractName();

     // THEN
     expect(component.contractTypeDisplayName).toBe(CONTRACT_TYPE_DISPLAY_NAMES.IFS);

     // GIVEN
     component.contractType = (CONTRACT_TYPES.GPO || CONTRACT_TYPES.GPO_AMENDMENT);

     // WHEN
     component.determineContractName();

     // THEN
     expect(component.contractTypeDisplayName).toBe(CONTRACT_TYPE_DISPLAY_NAMES.GPO);
  });

  it('should fetch pricing dates', () => {
    // GIVEN
    const AGREEMENT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'), 'draft',
    new Date('01/26/2020'), new Date('01/30/2020'), '123', 'edit', '2');

    sessionStorage.setItem('contractInfo', JSON.stringify(AGREEMENT_DETAILS));

    // WHEN
    component.fetchPricingDates();
    component.priceStartDate = new Date('01/26/2020');

    // THEN
    expect(component.priceStartDate).toEqual(new Date('01/26/2020'));
  });

  it('should fetch furtherance effective dates in case if isFurtheranceMode is true', () => {
    // GIVEN

    fixture.detectChanges();

    // WHEN
    component.fetchPricingDates();

    // THEN
    expect(component.isFurtheranceMode).toBeTruthy();
    expect(pricingInformationService.setPricingInformationStatus).toBeTruthy();
    expect(component.furtheranceEffectiveDate).toEqual(
          new Date(moment.utc('01/01/2021', 'YYYY-MM-DD').format('MM/DD/YYYY')));
  });

  it('should load component', () => {
    // GIVEN
    component.agreementId = '75490bfc-289d-40d3-b644-ba095c2e1cea';
    component.contractType = 'IFS';

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
    spyOn(component.agreementId, 'trim').and.returnValue('75490bfc-289d-40d3-b644-ba095c2e1cea');
    spyOn(component.contractType, 'trim').and.returnValue('IFS');

    // WHEN
    component.ngOnInit();

  });
});


describe('Contract Information Component', () => {

  const CONTRACT_DETAILS = {
    agreementId: '75490bfc-289d-40d3-b644-ba095c2e1cea',
    cenddate: '12/30/2020',
    cname: 'My Contract',
    contractStatus: 'Draft',
    contractType: 'ICMDistributionAgreementStreet',
    cppid: 4,
    cppseqid: 21,
    cstdate: '12/30/2018',
    ctype: 'ICMDistributionAgreementRegional',
    penddate: '12/30/2020',
    pstdate: '12/30/2018',
    furtherancedate: '01/01/2021',
    versionNumber: 2
  }

  const CPP_INFORMATION_DETAILS: CPPInformationDetails = new CPPInformationDetails(4521, 1, 321);

  const CLM_CONTRACT_DETAILS: ClmContractDetails = new ClmContractDetails('TEST CONTRACT', 'ICMDistributionAgreementRegional',
    new Date('05/07/2019'), new Date('05/07/2020'), 'Draft', 'f7577793-0fb3-45f6-9710-ddb79514c24a', null, false, CPP_INFORMATION_DETAILS);

  let component: ContractInformationComponent;
  let fixture: ComponentFixture<ContractInformationComponent>;
  let service: StepperService;
  let pricingInformationService: PricingInformationService
  let router: Router;
  let route: ActivatedRoute;

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContractInformationComponent],
      providers: [
        PricingInformationService,
        { provide: Router, useClass: RouterStub },
        StepperService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParams: {
                'agreementId': '',
                'contractType': '',
              },
            },
          },
        }
      ]
    }).overrideTemplate(ContractInformationComponent, '')

    fixture = TestBed.createComponent(ContractInformationComponent);
    component = fixture.componentInstance;
    service = TestBed.get(StepperService);
    pricingInformationService = TestBed.get(PricingInformationService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
  });

  it('should navigate to error page if session storage is empty', () => {

    // GIVEN
    sessionStorage.clear();
    let spy = spyOn(router, 'navigate');

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(spy).toHaveBeenCalledWith((['/error']), { relativeTo: route, queryParams: { errorType: ERROR_TYPES.RETURN_TO_CLM }});
  });

  it('should fetch Contract Details if agreementId  and contractType is not in URL params', () => {

    // GIVEN
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
    spyOn(pricingInformationService, 'fetchContractPriceProfileInfo').and.returnValue(Observable.of(CLM_CONTRACT_DETAILS));

    // WHEN
    component.fetchContractDetails();

    // THEN
    expect(pricingInformationService.fetchContractPriceProfileInfo).toHaveBeenCalled();
  });
});
