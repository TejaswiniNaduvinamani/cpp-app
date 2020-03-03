import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Observable';

import { AuthorizationService, CostModelService, ReviewService, TranslatorService, ToasterService,
   AuthorizationDetails, PricingInformationService } from 'app/shared';
import { CostModelGridDetails, CostModelDTO, CostModelComponent } from 'app/cost-model';
import { ReturnToCLM } from 'app/contract/review/review.model';
import { SessionStorageInterface } from 'app/contract';

describe('CostModelComponent', () => {
  let component: CostModelComponent;
  let fixture: ComponentFixture<CostModelComponent>;
  let reviewService: ReviewService;
  let costModelService: CostModelService;
  let authService: AuthorizationService;
  let pricingInformationService: PricingInformationService;
  let mockTranslateService;
  let mockToasterService;

  const AUTH_DETAILS: AuthorizationDetails = new AuthorizationDetails(true, true, true, true, 'draft', false);

  const COST_MODEL_GRID_DETAILS: CostModelGridDetails = new CostModelGridDetails('Grocery', 3, '123', 45, '31', 34, 123, 45);

  const COST_MODEL: CostModelDTO = new CostModelDTO(23, '70-Last Received Cost Plus');

  const RETURN_TO_CLM: ReturnToCLM = new ReturnToCLM('https://gfsdev.icertis.com/Agreement/Details?entityName=');

    // do not remove double quotes in SessionStorageInterface. Setting of session requires double quotes while parsing the string.
    const CONTRACT_DETAILS: SessionStorageInterface = {
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
      "isPricingExhibitAttached": true,
      "penddate": new Date('12/12/2020'),
      "pstdate": new Date('12/12/2019'),
      "versionNumber": 1
     }

  beforeEach(() => {
    mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CostModelComponent],
      providers: [
        AuthorizationService,
        CostModelService,
        PricingInformationService,
        ReviewService,
        {provide: TranslatorService, useValue: mockTranslateService},
        {provide: ToasterService, useValue: mockToasterService}
      ]}).overrideTemplate(CostModelComponent, '')

    fixture = TestBed.createComponent(CostModelComponent);
    component = fixture.componentInstance;
    reviewService = TestBed.get(ReviewService);
    costModelService = TestBed.get(CostModelService);
    authService = TestBed.get(AuthorizationService);
    pricingInformationService = TestBed.get(PricingInformationService);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create the CostModel component', () => {
    // THEN
    expect(component).toBeTruthy();
  });

  it('should fetch authorization details and cost model grid details', () => {
    // GIVEN
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(costModelService, 'fetchCostGridDetails').and.returnValue(Observable.of([COST_MODEL_GRID_DETAILS]));
    spyOn(costModelService, 'fetchCostModelList').and.returnValue(Observable.of([COST_MODEL]));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(authService.fetchAuthorizationDetails).toHaveBeenCalledWith(String(CONTRACT_DETAILS.cppseqid),
       CONTRACT_DETAILS.isAmendment, CONTRACT_DETAILS.contractStatus);
    expect(costModelService.fetchCostGridDetails).toHaveBeenCalledWith(CONTRACT_DETAILS.cppseqid);
    expect(costModelService.fetchCostModelList).toHaveBeenCalled();
    expect(pricingInformationService.setPricingInformationStatus).toBeTruthy();
    expect(component.costModelGridDetails.length).toBeTruthy();
    expect(component.costModelList.length).toBeTruthy();
  });

  it('should return to CLM on click of Return to Contract', () => {
    // GIVEN
    spyOn(reviewService, 'fetchClmUrl').and.returnValue(Observable.of(RETURN_TO_CLM));
    spyOn(window, 'open');
    spyOn(sessionStorage, 'clear');

    // WHEN
    component.returnToCLM();

    // THEN
    expect(reviewService.fetchClmUrl).toHaveBeenCalled();
    expect(sessionStorage.clear).toHaveBeenCalled();
    expect(component.showSpinner).toBeTruthy();
    expect(window.open).toHaveBeenCalled();
  });

  it('should save/update cost model details', () => {
    // GIVEN
    mockTranslateService.translate.and.returnValue('TOASTER_MESSAGES.COST_MODEL_UPDATE_SUCCESS');
    spyOn(costModelService, 'saveCostModel').and.returnValue(Observable.of({}));

    // WHEN
    component.saveCostModel();

    // THEN
    expect(costModelService.saveCostModel).toHaveBeenCalled();
    expect(mockToasterService.showSuccess).toHaveBeenCalled();
    expect(component.showSpinner).toBeFalsy();
  });

  it('should copy costModelId from first row into all the rows', () => {
    // GIVEN
    const row = {
      'groupType': 'Grocery',
      'costModelId': 3
    };
    component.costModelGridDetails = [COST_MODEL_GRID_DETAILS];
    fixture.detectChanges();

    // WHEN
    component.copyRow(row);

    // THEN
    expect(component.costModelGridDetails[0].costModelId).toBe(3);
  });

  it('should display cost Model in view mode if costModelEditable is false', () => {
    // GIVEN
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(costModelService, 'fetchCostGridDetails').and.returnValue(Observable.of([COST_MODEL_GRID_DETAILS]));
    spyOn(costModelService, 'fetchCostModelList').and.returnValue(Observable.of([COST_MODEL]));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(authService.fetchAuthorizationDetails).toHaveBeenCalledWith(String(CONTRACT_DETAILS.cppseqid),
      CONTRACT_DETAILS.isAmendment, CONTRACT_DETAILS.contractStatus);
    expect(costModelService.fetchCostGridDetails).toHaveBeenCalledWith(CONTRACT_DETAILS.cppseqid);
    expect(AUTH_DETAILS.costModelEditable).toBeFalsy();
    expect(component.displayViewMode).toBeTruthy();
  });
});
