import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Renderer2, ElementRef } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { ItemAssignmentsComponent } from './item-assignments.component';
import { ItemAssignmentService, ReviewService, AuthorizationService, AuthorizationDetails,
  FurtheranceService, TranslatorService, ToasterService } from 'app/shared';
  import { ItemAssignmentModel, FutureItemModel } from './item-assignments.model';
  import { ReturnToCLM } from 'app/contract/review/review.model';
  import { SessionStorageInterface } from 'app/contract';

describe('ItemAssignmentsComponent', () => {

  const ITEM_ASSIGNMENT_LIST: ItemAssignmentModel[] = [new ItemAssignmentModel('166720', 'BLUEBERRY', false, false, false)];

  const FUTURE_ITEM_LIST: FutureItemModel = new FutureItemModel(21, 'Heinz Ketchup', 'Chilli', '150',
     11, ITEM_ASSIGNMENT_LIST, false, false, []);

    // do not remove double quotes in SessionStorageInterface. Setting of session requires double quotes while parsing the string.
    const CONTRACT_DETAILS: SessionStorageInterface = {
      "agreementId": '75490bfc-289d-40d3-b644-ba095c2e1cea',
      "contractStatus": 'Executed',
      "contractType": 'ICMDistributionAgreementRegional',
      "cppseqid": 42915,
      "cppFurtheranceSeq": 123,
      "cstdate": new Date('12/12/2019'),
      "ctype": 'ICMDistributionAgreementRegional',
      'isAmendment': false,
      'isFurtheranceMode': true,
     }

  let component: ItemAssignmentsComponent;
  let fixture: ComponentFixture<ItemAssignmentsComponent>;
  let itemAssignmentService: ItemAssignmentService;
  let authService: AuthorizationService;
  let reviewService: ReviewService;
  let furtheranceService: FurtheranceService;
  let mockTranslateService;
  let mockToasterService;
  const RETURN_TO_CLM: ReturnToCLM = new ReturnToCLM('https://gfsdev.icertis.com/Agreement/Details?entityName=');

  const AUTH_DETAILS =  {
    'priceProfileEditable': true,
    'customerAssignmentEditable': false,
    'itemAssignmentEditable': false,
    'cppStatus': 'Draft',
    'isPowerUser': true
  }
  beforeEach(() => {

    mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ItemAssignmentsComponent],
      providers: [
        AuthorizationService,
        FurtheranceService,
        ItemAssignmentService,
        Renderer2,
        ReviewService,
        { provide: ToasterService, useValue: mockToasterService },
        { provide: TranslatorService, useValue: mockTranslateService }
      ]
    }).overrideTemplate(ItemAssignmentsComponent, '');


    fixture = TestBed.createComponent(ItemAssignmentsComponent);
    component = fixture.componentInstance;
    itemAssignmentService = TestBed.get(ItemAssignmentService);
    authService = TestBed.get(AuthorizationService);
    reviewService = TestBed.get(ReviewService);
    furtheranceService = TestBed.get(FurtheranceService);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create ItemAssignmentsComponent component', () => {
    // THEN
    expect(component).toBeTruthy();
  });

  it('should add a new exception markup ', () => {
    // GIVEN
    component.contractPriceProfileId = '121';
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(itemAssignmentService, 'fetchAllFutureItems').and.returnValue(Observable.of([FUTURE_ITEM_LIST]));
    fixture.detectChanges();

    // WHEN
    component.fetchAllFutureItems();

    // THEN
    expect(itemAssignmentService.fetchAllFutureItems).toHaveBeenCalled();
    expect(component.futureItemAccordions.length).toBeTruthy();
    expect(component.showSpinner).toBeFalsy();
  });

  it('should fetch activate pricing button state for furtherance', () => {
    // GIVEN
    spyOn(furtheranceService, 'enableActivatePricingForFurtherance').and.returnValue(Observable.of({enableActivatePricingButton : true}));

    // WHEN
    component.fetchActivatePricingButtonState(CONTRACT_DETAILS);

    // THEN
    expect(furtheranceService.enableActivatePricingForFurtherance).toHaveBeenCalledWith(CONTRACT_DETAILS.cppFurtheranceSeq,
       CONTRACT_DETAILS.contractStatus);
    expect(component.isPriceProfileBtnEnabled).toBeTruthy();
  });

  it('should collapse future item accordion', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(component.renderer, 'addClass');
    spyOn(component.renderer, 'removeClass');
    const $ele = new ElementRef({
      querySelector: () => {
        return { classList : ['fa', 'fa-angle-down']}  },
      });
    // WHEN
    component.toggleCollapse($ele.nativeElement);

    // THEN
    expect(component.renderer.addClass).toHaveBeenCalled();
    expect(component.renderer.removeClass).toHaveBeenCalled();
  });

  it('should expand future item accordion', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(component.renderer, 'addClass');
    spyOn(component.renderer, 'removeClass');
    const $ele = new ElementRef({
      querySelector: () => {
        return { classList : ['fa', 'fa-angle-right']}  },
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

  it('should invoke activate pricing for furtherance and display success message on click of activate pricing button', () => {
    // GIVEN
    mockTranslateService.translate.and.returnValue('All pricing for the contract was activated successfully.');
    spyOn(furtheranceService, 'activatePricingForFurtherance').and.returnValue(Observable.of({}));

    // WHEN
    component.onActivatePricingForFurtherance();

    // THEN
    expect(furtheranceService.activatePricingForFurtherance).toHaveBeenCalled();
    expect(mockToasterService.showSuccess).toHaveBeenCalled();
    expect(component.isPriceProfileBtnEnabled).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
  });

})
