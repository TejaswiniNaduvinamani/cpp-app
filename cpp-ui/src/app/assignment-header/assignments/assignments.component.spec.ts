import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ElementRef } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { CONTRACT_TYPES, CONTRACT_APPROVED, EXECUTED } from './../../shared/utils/app.constants';

import { AssignmentsComponent } from './assignments.component';
import { AssignmentsService } from './../../shared/services/assignments/assignments.service';
import { AssignmentMarkupModel } from 'app/assignment-header/assignments/assignments.model';
import { ContractInformationDetails } from '../../contract/contract-information/contract-information.model';
import { ItemLevelDisplayModel, MarkupDisplayModel, SubgroupDisplayModel } from 'app/assignment-header';
import { ReviewService } from './../../shared/services/review/review.service';
import { ReturnToCLM } from 'app/contract/review/review.model';
import { RealCustomerModel } from '../../assignment-header/assignments/customer-assignments/customer-assignments.model';
import { StepperService } from './../../shared/services/stepper/stepper.service';
import { TranslatorService, ToasterService, AuthorizationService, AuthorizationDetails } from '../../shared';

describe('AssignmentsComponent', () => {

  const CONTRACT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'), 'Draft',
    new Date('01/26/2020'), new Date('01/30/2020'), '123', 'edit', '2');

  const MARKUP_VALUE: Array<MarkupDisplayModel> = [
    new MarkupDisplayModel('Grocery', 12, '2', '%', 1, new Date('2019/02/20'), new Date('2019/02/26'))
  ];

  const ITEM_VALUE: Array<ItemLevelDisplayModel> = [
    new ItemLevelDisplayModel(null, '100003', 'CKN WINGS', null, '35', '$', 1, new Date('2019/02/20'), new Date('2019/02/26'))
  ];

  const REAL_CUST_VALUE: Array<RealCustomerModel> = [
    new RealCustomerModel('Heinz', '110', '31', 'SMG', true, false, false)
  ];

  const SUBGROUP_VALUE: Array<SubgroupDisplayModel> = [
    new SubgroupDisplayModel('Heinz', '10', '%', 1)
  ];

  const ASSIGNMENT_MARKUP: Array<AssignmentMarkupModel> = [
    new AssignmentMarkupModel('123', 'Contract', 'CMG', 2, MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE,false, false, REAL_CUST_VALUE, 1)
  ];

  const AUTH_DETAILS = {
    priceProfileEditable: true,
    customerAssignmentEditable: true,
    itemAssignmentEditable: true,
    isPowerUser: true,
    cppStatus: 'draft'
  };

  const CUSTOMER_TYPES = ['Family', 'PMG', 'SSM', 'CMG'];

  let component: AssignmentsComponent;
  let fixture: ComponentFixture<AssignmentsComponent>;
  let assignmentService: AssignmentsService;
  let reviewService: ReviewService;
  let mockTranslateService;
  let mockToasterService;
  let authService: AuthorizationService;
  let translatorService: TranslatorService;
  let toaster: ToasterService;

  const RETURN_TO_CLM: ReturnToCLM = new ReturnToCLM('https://gfsdev.icertis.com/Agreement/Details?entityName=');

  beforeEach(() => {

    mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssignmentsComponent],
      providers: [
        AuthorizationService,
        AssignmentsService,
        ReviewService,
        { provide: ToasterService, useValue: mockToasterService },
        { provide: TranslatorService, useValue: mockTranslateService }
      ]
    }).overrideTemplate(AssignmentsComponent, '');

    fixture = TestBed.createComponent(AssignmentsComponent);
    component = fixture.componentInstance;
    assignmentService = TestBed.get(AssignmentsService);
    reviewService = TestBed.get(ReviewService);
    authService = TestBed.get(AuthorizationService);
    translatorService = TestBed.get(TranslatorService);
    toaster = TestBed.get(ToasterService);


    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create Assignment Component', () => {
    expect(component).toBeTruthy();
  });

  it('should load Assignment Page', () => {
    // GIVEN
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of([]));
    spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(assignmentService, 'fetchActivatePricingButtonState').and.returnValue(Observable.of({enableActivatePricingButton: false}));
    const fetchMarkupservice = spyOn(assignmentService, 'fetchAssignmentMarkups').and.returnValue(Observable.of([ASSIGNMENT_MARKUP]));
    const fetchCustomerTypeService = spyOn(assignmentService, 'fetchCustomerType').and.returnValue(Observable.of([CUSTOMER_TYPES]));
    component.contractPriceProfileId = '121';

    // WHEN
    component.ngOnInit();

    // THEN
    expect(component.assignmentDetails).not.toBeNull();
    expect(fetchMarkupservice).toHaveBeenCalled();
    expect(fetchCustomerTypeService).toHaveBeenCalled();
  });

  it('should collapse Markup accordion', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(component.renderer, 'addClass');
    spyOn(component.renderer, 'removeClass');
    const $ele = new ElementRef({
      querySelector: () => {
        return { classList: ['fa', 'fa-angle-down'] }
      },
    });
    // WHEN
    component.toggleCollapse($ele.nativeElement);

    // THEN
    expect(component.renderer.addClass).toHaveBeenCalled();
    expect(component.renderer.removeClass).toHaveBeenCalled();
  });

  it('should expand Markup accordion', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(component.renderer, 'addClass');
    spyOn(component.renderer, 'removeClass');
    const $ele = new ElementRef({
      querySelector: () => {
        return { classList: ['fa', 'fa-angle-right'] }
      },
    });
    // WHEN
    component.toggleCollapse($ele.nativeElement);

    // THEN
    expect(component.renderer.addClass).toHaveBeenCalled();
    expect(component.renderer.removeClass).toHaveBeenCalled();
  });

  it('should return to clm', () => {
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

  it('should invoke activate pricing service and display success message on click of activate pricing button', () => {
    // GIVEN
    component.contractPriceProfileId = '121';
    component.isAmendment = false;
    component.cppStatus = CONTRACT_APPROVED;
    mockTranslateService.translate.and.returnValue('All pricing for the contract was activated successfully.');
    spyOn(assignmentService, 'activatePricing').and.returnValue(Observable.of({}));
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));

    // WHEN
    component.onActivatePricing();

    // THEN
    expect(assignmentService.activatePricing).toHaveBeenCalled();
    expect(mockToasterService.showSuccess).toHaveBeenCalled();
    expect(component.disableActivatePriceProfile).toBeTruthy();
    expect(component.showSpinner).toBeFalsy();
  });

  it('should invoke activate pricing and display success message on click of activate pricing button when clm status is EXECUTED', () => {
    // GIVEN
    component.contractPriceProfileId = '121';
    component.isAmendment = true;
    component.isViewMode = false;
    component.cppStatus = CONTRACT_APPROVED;
    mockTranslateService.translate.and.returnValue('All pricing for the contract was activated successfully.');
    spyOn(assignmentService, 'activatePricing').and.returnValue(Observable.of({}));
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));

    // WHEN
    component.onActivatePricing();

    // THEN
    expect(assignmentService.activatePricing).toHaveBeenCalled();
    expect(mockToasterService.showSuccess).toHaveBeenCalled();
    expect(component.disableActivatePriceProfile).toBeTruthy();
    expect(component.showSpinner).toBeFalsy();
  });

  it('should initiate page load once default concept is deleted', () => {
        // GIVEN
        spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
        spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of([]));
        spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
        spyOn(assignmentService, 'fetchActivatePricingButtonState').and.returnValue(Observable.of({enableActivatePricingButton: false}));
        const fetchMarkupservice = spyOn(assignmentService, 'fetchAssignmentMarkups').and.returnValue(Observable.of([ASSIGNMENT_MARKUP]));
        const fetchCustomerTypeService = spyOn(assignmentService, 'fetchCustomerType').and.returnValue(Observable.of([CUSTOMER_TYPES]));
        component.clearAllAssignments = true;
        component.contractPriceProfileId = '121';

        // WHEN
        component.ngDoCheck();

        // THEN
        expect(component.assignmentDetails).not.toBeNull();
        expect(fetchMarkupservice).toHaveBeenCalled();
        expect(fetchCustomerTypeService).toHaveBeenCalled();
        expect(component.showSpinner).toBeFalsy();
      });

  it('should detect deletion of default concept and clear all assignment mappings', () => {
    // GIVEN
    component.clearAllAssignments = false;

    // WHEN
    component.detectDefaultAssignmentDelete(true);

    // THEN
    expect(component.clearAllAssignments).toBe(true);
  });

  it('should fetch pricing activate state', () => {
    // GIVEN
    component.disableActivatePriceProfile = false;

    // WHEN
    component.fetchPricingActivateState(false);

    // THEN
    expect(component.disableActivatePriceProfile).toBeTruthy();

  });
});

