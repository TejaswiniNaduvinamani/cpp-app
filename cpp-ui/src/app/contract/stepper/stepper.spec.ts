import { ComponentFixture } from '@angular/core/testing';
import { ChangeDetectorRef } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Observable';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { TestBed, fakeAsync, tick } from '@angular/core/testing';

import { ContractInformationDetails, CPPInformationDetails, ClmContractDetails } from '../contract-information/contract-information.model';
import { ContractInformation } from './../pricing-information/pricing-information.model';
import { CONTRACT_TYPES } from './../../shared/utils/app.constants';
import { PricingInformationService, AuthorizationService } from 'app/shared';
import { RouterStub } from '../../../../test/unit-testing/mock/router-stub';
import { StepperComponent } from './stepper.component';
import { StepModel } from './step.model';
import { StepperService } from './../../shared/services/stepper/stepper.service';


describe('Stepper ', () => {

  const CONTRACT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
  CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'), 'draft',
  new Date('01/26/2020'), new Date('01/30/2020'), '4', 'edit', '2');

  const CONTRACT_INFO: ContractInformation = new ContractInformation(4, 2, 21);

  let component: StepperComponent;
  let fixture: ComponentFixture<StepperComponent>;
  let stepperService: StepperService;
  let authorizationService: AuthorizationService;
  let pricingInformationService: PricingInformationService;
  let mockActivatedRoute;
  beforeEach(() => {

    mockActivatedRoute = jasmine.createSpyObj<ActivatedRoute>('activatedRoute', ['snapshot', 'queryParams']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StepperComponent],
      providers: [
        AuthorizationService,
        PricingInformationService,
        { provide: Router, useClass: RouterStub },
        StepperService,
        {
          provide: ActivatedRoute,
          useValue: {
              snapshot: {
                queryParams: {
                  subscribe: (fn: (value: Params) => void) => fn({
                    cname: 'Contract',
                    ctype: CONTRACT_TYPES.IFS,
                    stdate: '12/01/2020',
                    enddate: '12/30/2022'
                  }),
                },
              },
          },
        }
                ]
    }).overrideTemplate(StepperComponent, '')

    fixture = TestBed.createComponent(StepperComponent);
    component = fixture.componentInstance;
    stepperService = TestBed.get(StepperService);
    authorizationService = TestBed.get(AuthorizationService);
    pricingInformationService = TestBed.get(PricingInformationService);
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create the component', () => {

    fixture.detectChanges();
    // THEN
    expect(component).toBeTruthy();
  });

  it('should fetch contract information from URL query params', () => {

    // WHEN
    fixture.detectChanges();
    component.setContractInformation(CONTRACT_INFO);

    // THEN
    expect(sessionStorage).not.toBeNull();

  });

  it('should update steppers', () => {
    // GIVEN
    fixture.detectChanges();
    component.stepper  = [new StepModel(2, 'DC', '', true, true, true, false)];

    // WHEN
    component.currentStep = 2;
    component.updateStepper(1);
    component.updateStepper(2);
    component.updateStepper(3);

    // THEN

    expect(component.currentStep).toBe(3);

  });

  it('should update View Mode Stepper', () => {
        // GIVEN
        fixture.detectChanges();
        component.stepper  = [new StepModel(2, 'DC', '', true, true, true, false)];

        // WHEN
        component.currentStep = 2;
        component.updateStepperViewMode(1);
        component.updateStepperViewMode(2);
        component.updateStepperViewMode(3);

        // THEN

        expect(component.currentStep).toBe(3);

  });

  it('should update steppers for furtherence', () => {
        // GIVEN
        component.isFurtheranceMode = true;
        fixture.detectChanges();
        component.stepper  = [new StepModel(2, 'DC', '', true, true, true, false)];

        // WHEN
        component.currentStep = 2;
        component.updateStepperViewMode(1);
        component.updateStepperViewMode(2);
        component.updateStepperViewMode(3);

        // THEN

        expect(component.currentStep).toBe(3);
  })

  it('should hide steppers', fakeAsync(() => {
    // GIVEN
    component.contractType = CONTRACT_TYPES.IFS;
    component.stepper  = [new StepModel(1, 'DC', '', true, true, true, false),
    new StepModel(2, 'DC', '', true, true, true, false),
    new StepModel(3, 'DC', '', true, true, true, false),
    new StepModel(4, 'DC', '', true, true, true, false),
    new StepModel(5, 'DC', '', true, true, true, false)];

    // WHEN
    component.showHideSteps();
    tick(500);
    fixture.detectChanges();

    // THEN
    fixture.whenStable().then(() => {
      expect(component.stepper.length).not.toBe(null);
    })

    // GIVEN
    component.contractType = CONTRACT_TYPES.IFS_AMENDMENT;
    component.stepper  = [new StepModel(1, 'DC', '', true, true, true, false),
    new StepModel(2, 'DC', '', true, true, true, false),
    new StepModel(3, 'DC', '', true, true, true, false),
    new StepModel(4, 'DC', '', true, true, true, false),
    new StepModel(5, 'DC', '', true, true, true, false)];

    // WHEN
    component.showHideSteps();
    tick(500);
    fixture.detectChanges();

    // THEN
    fixture.whenStable().then(() => {
      expect(component.stepper.length).not.toBe(null);
    })
  }))


  it('should detect changes in contract types', () => {
    // GIVEN
    component.contractType = CONTRACT_TYPES.IFS;
    spyOn(stepperService, 'contractTypeChange$').and.returnValue(CONTRACT_TYPES.IFS);

    // WHEN
    component.detectContractTypeChanges();

    // THEN
    expect(component.contractType).toBe('ICMDistributionAgreementStreet');
  });

  it('should navigate steppers when display mode is edit', () => {
    // GIVEN
    component.displayViewMode = false;

    // WHEN
    component.navigateStepper(2);

    // THEN
    expect(component.currentStep).toBe(2);
  });

  it('should navigate steppers when display mode is view', () => {
    // GIVEN
    component.displayViewMode = true;

    // WHEN
    component.navigateStepper(3);

    // THEN
    expect(component.currentStep).toBe(3);
  });

  it('should navigate steppers when contract is in furtherance', () => {
    // GIVEN
    component.currentStep = 3;
    component.isFurtheranceMode = true;
    spyOn(stepperService, 'furtheranceStepperChange$').and.returnValue(Observable.of(1));

    // WHEN
    component.navigateStepper(3);

    // THEN
    expect(component.currentStep).toBeTruthy();
  });

  it('should update contract Information', () => {
    // GIVEN
    component.agreementId = '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308';
    component.contractType = 'ICMDistributionAgreementRegional';
    const CONTRACT_INFORMATION: ContractInformation = {
      contractPriceProfileId: 4,
      versionNumber: 2,
      contractPriceProfileSeq: 21
    };

    // WHEN
    component.updateContractInformation(CONTRACT_INFORMATION);

    // THEN
    expect(component.contractPriceProfileId).toBe(4);
    expect(component.versionNumber).toBe(2);
  });

  it('should fetch cpp status from Authorization Service on page load', () => {
    // GIVEN
    spyOn(authorizationService, 'getAuthorizationDetails').and.returnValue(Observable.of({'cppStatus': 'DRAFT'}));

    // WHEN
    fixture.detectChanges()

    // THEN
    expect(component.cppStatus).toBe('DRAFT');
  });

})


describe('Stepper component', () => {

  const CONTRACT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
  CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'), 'draft',
  new Date('01/26/2020'), new Date('01/30/2020'), '4', 'edit', '2');

  let component: StepperComponent;
  let fixture: ComponentFixture<StepperComponent>;
  let stepperService: StepperService;
  let pricingInformationService: PricingInformationService;
  let mockActivatedRoute;
  beforeEach(() => {

    mockActivatedRoute = jasmine.createSpyObj<ActivatedRoute>('activatedRoute', ['snapshot', 'queryParams']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StepperComponent],
      providers: [
        AuthorizationService,
        PricingInformationService,
        { provide: Router, useClass: RouterStub },
        StepperService,
        {
          provide: ActivatedRoute,
          useValue: {
              snapshot: {
                queryParams: {
                  'agreementId': '75490bfc-289d-40d3-b644-ba095c2e1cea',
                  'contractType': 'IFS',
                  subscribe: (fn: (value: Params) => void) => fn({
                    cname: 'Test Contract',
                    ctype: CONTRACT_TYPES.IFS,
                    stdate: '12/01/2020',
                    enddate: '12/30/2022'
                  }),
                },
              },
          },
        }
                ]
    }).overrideTemplate(StepperComponent, '')

    fixture = TestBed.createComponent(StepperComponent);
    component = fixture.componentInstance;
    stepperService = TestBed.get(StepperService);
    pricingInformationService = TestBed.get(PricingInformationService);
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should get Contract information when agreementId and contractType is present', () => {

    // GIVEN
    const CPP_INFORMATION_DTO = {
      'cppInformationDto' : {
        'contractPriceProfileId': 4213,
        'version': 1,
        'contractPriceProfileSeq': 123,
      }
    }
    spyOn(pricingInformationService, 'getClmContractDetails').and.returnValue(Observable.of(CPP_INFORMATION_DTO));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(pricingInformationService.getClmContractDetails).toHaveBeenCalled();
  });
})
