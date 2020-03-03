import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';
import { Router, ActivatedRoute } from '@angular/router';

import { ActivatedRouteStub } from '../../../../test/unit-testing/mock/activated-route-stub';
import { ContractInformationDetails } from '../../contract';
import { FurtheranceService, ErrorService, ReviewService, CONTRACT_TYPES } from '../../shared';
import { FurtheranceInformationComponent } from './furtherance-information.component';
import { FurtheranceBaseModel, ValidateFurtheranceModel } from './furtherance-information.model';
import { ReturnToCLM } from '../../contract/review/review.model';
import { RouterStub } from '../../../../test/unit-testing/mock/router-stub';

describe('FurtheranceInformationComponent', () => {

  const CONTRACT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'), 'Draft', new Date('01/30/2020'),
    new Date('01/30/2020'), '123', 'edit', '2', false);

  const VALIDATE_FURTHERANCE_MODEL: ValidateFurtheranceModel = {
    'hasInProgressFurtherance': false
  }
  const RETURN_TO_CLM: ReturnToCLM = new ReturnToCLM('https://gfsdev.icertis.com/Agreement/Details?entityName=');

  const FURTHERANCE_BASE_MODEL: FurtheranceBaseModel = {
    'agreementId': '75490bfc-289d-40d3-b644-ba095c2e1cea',
    'contractType': 'ICMDistributionAgreementRegional',
    'cppFurtheranceSeq': 123
  }

  let component: FurtheranceInformationComponent;
  let fixture: ComponentFixture<FurtheranceInformationComponent>;
  let furtheranceService: FurtheranceService;
  let errorService: ErrorService;
  let reviewService: ReviewService;
  let router: Router;
  let route: ActivatedRoute;
  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FurtheranceInformationComponent],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
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
        FurtheranceService,
        ErrorService,
        ReviewService,
      ]
    }).overrideTemplate(FurtheranceInformationComponent, '');

    fixture = TestBed.createComponent(FurtheranceInformationComponent);
    component = fixture.componentInstance;
    furtheranceService = TestBed.get(FurtheranceService);
    errorService = TestBed.get(ErrorService);
    reviewService = TestBed.get(ReviewService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create furtherance information component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch agreementId and contractType from URL params', () => {
    // GIVEN
    spyOn(errorService, 'checkErrorStatus').and.returnValue(Observable.of());

    // WHEN
    component.ngOnInit();

    // THEN
    expect(component.showSpinner).toBeTruthy();
    expect(component.parentAgreementId).toBe('75490bfc-289d-40d3-b644-ba095c2e1cea');
    expect(component.contractType).toBe('ICMDistributionAgreementStreetAmendment');
  });

  it('should validate if any furtherance is not in progress', () => {

    // GIVEN
    component.parentAgreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308 ';
    component.contractType = 'ICMDistributionAgreementRegional ';
    spyOn(furtheranceService, 'validateHasInProgressFurtherance').and.returnValue(Observable.of(VALIDATE_FURTHERANCE_MODEL));

    // WHEN
    component.validateHasInProgressFurtherance();

    // THEN
    expect(furtheranceService.validateHasInProgressFurtherance).toHaveBeenCalled();
    expect(component.contractType).toBeTruthy();
    expect(component.hasInProgressFutherance).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
  });

  it('should validate if any furtherance is in progress', () => {

    // GIVEN
    component.parentAgreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308 ';
    component.contractType = 'ICMDistributionAgreementRegional ';
    const furtherance_InProgress_DTO = {
      'hasInProgressFurtherance': true
    }
    spyOn(furtheranceService, 'validateHasInProgressFurtherance').and.returnValue(Observable.of(furtherance_InProgress_DTO));

    // WHEN
    component.validateHasInProgressFurtherance();

    // THEN
    expect(furtheranceService.validateHasInProgressFurtherance).toHaveBeenCalled();
    expect(component.contractType).toBeTruthy();
    expect(component.showSpinner).toBeTruthy();
  });

  it('should set furtherance agreement information into sesssion', () => {
    // GIVEN
    component.parentAgreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    component.contractType = 'ICMDistributionAgreementRegional';

    // WHEN
    component.setFurtheranceAgreementInfo();

    // THEN
    expect(component.setFurtheranceAgreementInfo).toBeTruthy();
    expect(sessionStorage.setItem).toBeTruthy();
  });

  it('should get furtherance agreement information from sesssion', () => {
    // GIVEN
    const agreementInfo = {
      parentAgreementId: '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308',
      parentContractType: 'ICMDistributionAgreementRegional'
    };
    sessionStorage.setItem('contractInfo', JSON.stringify(agreementInfo));

    // WHEN
    component.getFurtheranceAgreementInfo();

    // THEN
    expect(component.getFurtheranceAgreementInfo).toBeTruthy();
    expect(sessionStorage.getItem).toBeTruthy();
    expect(component.parentAgreementId).toBe('4e4321ca-3d4d-40d5-a3c4-20b56ba1a308');
    expect(component.contractType).toBe('ICMDistributionAgreementRegional');
  });

  it('should set agreementId, contractType, cppFurtheranceSeq and navigate to pricing information', () => {
    // GIVEN
    let spy = spyOn(router, 'navigate');
    spyOn(sessionStorage, 'setItem');

    // WHEN
    component.navigateToPricingInfo(FURTHERANCE_BASE_MODEL);

    // THEN
    expect(component.agreementId).toBe('75490bfc-289d-40d3-b644-ba095c2e1cea');
    expect(component.contractType).toBe('ICMDistributionAgreementRegional');
    expect(component.cppFurtheranceSeq).toBe(123);
    expect(sessionStorage.setItem).toHaveBeenCalled();
    expect(component.showSpinner).toBeFalsy();
    expect(spy).toHaveBeenCalledWith((['/pricinginformation']), {
      relativeTo: route,
      queryParams: { agreementId: '75490bfc-289d-40d3-b644-ba095c2e1cea', contractType: 'ICMDistributionAgreementRegional' }
    });
  });

  it('should return to CLM on click of Cancel & Go Back', () => {
    // GIVEN
    spyOn(reviewService, 'fetchClmUrl').and.returnValue(Observable.of(RETURN_TO_CLM));
    spyOn(window, 'open');

    // WHEN
    component.onCancelAndGoBack();

    // THEN
    expect(reviewService.fetchClmUrl).toHaveBeenCalled();
    expect(window.open).toHaveBeenCalled();
    expect(component.showSpinner).toBeFalsy();
  });

  it('should create new furtherance in case if no furtherance exists', () => {
    // GIVEN
    component.parentAgreementId = '75490bfc-289d-40d3-b644-ba095c2e1cea';
    component.contractType = 'ICMDistributionAgreementStreetAmendment';
    spyOn(furtheranceService, 'createNewFurtherance').and.returnValue(Observable.of(FURTHERANCE_BASE_MODEL));
    spyOn(component, 'navigateToPricingInfo');

    // WHEN
    component.onCreateNewFurtherance();

    // THEN
    expect(component.showSpinner).toBeTruthy();
    expect(furtheranceService.createNewFurtherance).toHaveBeenCalled();
    expect(component.navigateToPricingInfo).toBeTruthy();
  });

  it('should fetch furtherance base information if furtherance exists', () => {
    // GIVEN
    component.parentAgreementId = '75490bfc-289d-40d3-b644-ba095c2e1cea';
    spyOn(furtheranceService, 'fetchInProgressFurtheranceInfo').and.returnValue(Observable.of(FURTHERANCE_BASE_MODEL));
    spyOn(component, 'navigateToPricingInfo');

    // WHEN
    component.fetchInProgressFurtheranceInformation();

    // THEN
    expect(component.showSpinner).toBeTruthy();
    expect(furtheranceService.fetchInProgressFurtheranceInfo).toHaveBeenCalled();
    expect(component.navigateToPricingInfo).toHaveBeenCalled();
  })
});
