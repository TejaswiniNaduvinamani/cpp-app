import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DecimalPipe } from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';
import { Renderer2 } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ActivatedRouteStub } from '../../../../test/unit-testing/mock/activated-route-stub';
import { ContractInformationDetails, SessionStorageInterface } from 'app/contract/contract-information/contract-information.model';
import { RouterStub } from '../../../../test/unit-testing/mock/router-stub';
import { SplitCaseFees } from './split-case-fee.model';
import { SplitCaseComponent } from './split-case.component';
import { SplitCaseFeeService, StepperService, AuthorizationService, AuthorizationDetails, FurtheranceService } from '../../shared';

describe('Splitcase component', () => {
  let component: SplitCaseComponent;
  let fixture: ComponentFixture<SplitCaseComponent>;
  let router: Router;
  let route: ActivatedRoute;
  let splitCaseFeeService: SplitCaseFeeService;
  let authService: AuthorizationService;
  let furtheranceService: FurtheranceService;

   const SPLIT_CASE_ROW_1: SplitCaseFees = new SplitCaseFees('GROCERY', 35, '%', new Date('2020-02-01'), new Date('2020-02-01'),
   '1', false, false);
   const SPLIT_CASE_ROW_2: SplitCaseFees = new SplitCaseFees('FROZEN', 35, '%', new Date('2020-02-01'), new Date('2020-02-01')
   , '1', false, false);
   const AUTH_DETAILS: AuthorizationDetails = new AuthorizationDetails(true, true, true, true, 'draft');

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

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SplitCaseComponent],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        DecimalPipe,
        Renderer2,
        StepperService,
        SplitCaseFeeService,
        AuthorizationService,
        FurtheranceService
      ]
    }).overrideTemplate(SplitCaseComponent, '')

    fixture = TestBed.createComponent(SplitCaseComponent);
    component = fixture.componentInstance;
    splitCaseFeeService = TestBed.get(SplitCaseFeeService);
    furtheranceService = TestBed.get(FurtheranceService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
    authService = TestBed.get(AuthorizationService);
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create the splitcase fee component', () => {
    // THEN
    expect(component).toBeTruthy();
  })

  it('should get the Product Types', () => {

    // GIVEN
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of([]));
    spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(splitCaseFeeService, 'fetchProductTypes').and.returnValue(Observable.of([SPLIT_CASE_ROW_1]));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(component.rows).toBeTruthy();
    expect(component.showSpinner).toBeFalsy();
  })

  it('should build save splitCaseFee object for pricing information flow', () => {
    // GIVEN
    component.isFurtheranceMode = false;
    component.splitCaseList.contractPriceProfileSeq = 123;
    component.rows.push({
      'productType': 'GROCERY',
      'splitCaseFee': 35.00,
      'unit': '%',
      'effectiveDate': new Date('1/08/2018'),
      'expirationDate': new Date('30/08/2018'),
      'itemPriceId': '1',
      'lessCaseRuleId': 2
    });

    // WHEN
    component.buildSplitCaseFees();

    // THEN
    expect(component.feeTypeValue).toBe(2);
    expect(component.splitCaseList.splitCaseFeeValues).toBeTruthy();
    expect(component.splitCaseList.splitCaseFeeValues[0].productType).toBe('GROCERY');
    expect(component.splitCaseList.splitCaseFeeValues[0].splitCaseFee).toBe(35.00);
    expect(component.splitCaseList.splitCaseFeeValues[0].unit).toBe('%');
    expect(component.splitCaseList.splitCaseFeeValues[0].itemPriceId).toBe('1');
    expect(component.splitCaseList.splitCaseFeeValues[0].effectiveDate.toDateString).toBe(new Date('1/08/2018').toDateString);
    expect(component.splitCaseList.splitCaseFeeValues[0].expirationDate.toDateString).toBe(new Date('30/08/2018').toDateString);

    // GIVEN - should build save splitCaseFee object for furtherance flow
    component.isFurtheranceMode = true;

    // WHEN
    component.buildSplitCaseFees();

    // THEN
    expect(component.feeTypeValue).toBe(2);
    expect(component.splitCaseFurtheranceList.splitCaseFeeValues).toBeTruthy();
    expect(component.splitCaseFurtheranceList.splitCaseFeeValues[0].productType).toBe('GROCERY');
    expect(component.splitCaseFurtheranceList.splitCaseFeeValues[0].splitCaseFee).toBe(35.00);
    expect(component.splitCaseFurtheranceList.splitCaseFeeValues[0].unit).toBe('%');
    expect(component.splitCaseFurtheranceList.splitCaseFeeValues[0].itemPriceId).toBe('1');
    expect(component.splitCaseFurtheranceList.splitCaseFeeValues[0].effectiveDate.toDateString).toBe(new Date('1/08/2018').toDateString);
    expect(component.splitCaseFurtheranceList.splitCaseFeeValues[0].expirationDate.toDateString).toBe(new Date('30/08/2018').toDateString);
  });

  it('should save Splitcase Fees for furtherance', () => {
    // GIVEN
    component.isFurtheranceMode = true;
    component.splitCaseList.contractPriceProfileSeq = 123;
    component.rows.push(SPLIT_CASE_ROW_1);
    spyOn(furtheranceService, 'saveSplitCaseFee').and.returnValue(Observable.of([]));

    // WHEN
    fixture.detectChanges();
    component.saveSplitCaseFee();

    // THEN
    expect(component.showSpinner).toBeFalsy();
    expect(furtheranceService.saveSplitCaseFee).toHaveBeenCalled();
    expect(component.splitCaseFurtheranceList.splitCaseFeeValues).toBeTruthy();
  });

  it('should validate the row data when unrecommended values are entered', () => {

    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': 99,
      'unit': '%',
      'itemPriceId': '2'
    }

    // WHEN
    component.validateData(row);

    // THEN
    expect(component.validateData(row)).toBeTruthy();
  })

  it('should validate the row data when recommended values are entered', () => {

    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': 10,
      'unit': '%',
      'itemPriceId': '2'
    }

    // WHEN
    component.validateData(row);

    // THEN
    expect(component.validateData(row)).toBeFalsy();
  })

  it('should copy row', () => {

    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': 10,
      'unit': '%',
      'itemPriceId': '2',
      'invalidSplitCaseFee': false,
      'isNonNumericSplitCaseFee': false
    };
    const  event  =  { 'target':  { 'value': 10 } };
    component.rows.push(SPLIT_CASE_ROW_1);
    component.rows.push(SPLIT_CASE_ROW_2);
    spyOn(component._renderer, 'removeClass');

    // WHEN
    component.copyRow(event, row);

    // THEN
    expect(component.rows).not.toBeNull();
  })

  it('should copy row and highlight the border if invalid SplitCase has been entered', () => {

    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': 'abcd',
      'unit': '%',
      'itemPriceId': '2',
      'invalidSplitCaseFee': false,
      'isNonNumericSplitCaseFee': true
    };
    const  event  =  { 'target':  { 'value': 'abcd' } };
    component.rows.push(SPLIT_CASE_ROW_1);
    component.rows.push(SPLIT_CASE_ROW_2);
    spyOn(component._renderer, 'addClass');

    // WHEN
    component.copyRow(event, row);

    // THEN
    expect(component._renderer.addClass).toHaveBeenCalled();
  })

  it('should assign the unit value to current row', () => {
    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': 10,
      'unit': '%',
      'itemPriceId': '2'
    }
    const event = { 'target': { 'value': '$' } };

    // WHEN
    component.onUnitClick(event, row);

    // THEN
    expect(row.unit).toBe('$');
  })

  it('should test onBlurSplitcase with value', () => {
    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': 10,
      'unit': '%',
      'itemPriceId': '2',
      'invalidSplitCaseFee': false,
      'isNonNumericSplitCaseFee': false
    }
    const event = { 'target': { 'value': '10' } };
    spyOn(component._renderer, 'removeClass');

    // WHEN
    component.onblurSplitcase(event, row);

    // THEN
    expect(event.target.value).not.toBeNull()
  })

  it('should test onBlurSplitcase with blank entry', () => {
    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': '',
      'unit': '%',
      'itemPriceId': '2',
      'invalidSplitCaseFee': false,
      'isNonNumericSplitCaseFee': false
    }
    const event = { 'target': { 'value': '' } };
    spyOn(component._renderer, 'removeClass');

    // WHEN
    component.onblurSplitcase(event, row);

    // THEN
    expect(event.target.value).toBe('0.00');
  })

  it('should test onBlurSplitcase with Non-numeric value', () => {
    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': 'abcd',
      'unit': '%',
      'itemPriceId': '2',
      'invalidSplitCaseFee': false,
      'isNonNumericSplitCaseFee': true
    }
    const event = { 'target': { 'value': 'abcd' } };
    spyOn(component._renderer, 'addClass');

    // WHEN
    component.onblurSplitcase(event, row);

    // THEN
    expect(component._renderer.addClass).toHaveBeenCalled();
  })

  it('should test onBlurSplitcase with value greater than 99', () => {
    // GIVEN
    const row = {
      'productType': 'FROZEN',
      'splitCaseFee': 100,
      'unit': '%',
      'itemPriceId': '2',
      'invalidSplitCaseFee': true,
      'isNonNumericSplitCaseFee': false
    }
    const event = { 'target': { 'value': '100' } };
    spyOn(component._renderer, 'addClass');

    // WHEN
    component.onblurSplitcase(event, row);

    // THEN
    expect(component._renderer.addClass).toHaveBeenCalled();
  })

  it('should populate View SplitCase Fee Data', () => {
    // GIVEN;
    const splitCaseFeeList = [SPLIT_CASE_ROW_1];
    spyOn(splitCaseFeeList, 'map').and.returnValue(['GROCERY', '35%', '2020-02-01', '2020-02-01', '1']);

     // WHEN
     component.createViewSplitCaseFee(splitCaseFeeList);

     // THEN
     expect(component.viewSplitcCaseRow.length).toBeTruthy();
  });

  it('should check if save split case button is not grayed-out', () => {
    // GIVEN
    component.rows.push(SPLIT_CASE_ROW_1);
    component.rows.push(SPLIT_CASE_ROW_2);

    spyOn(component.rows, 'some').and.returnValue(false);

    fixture.detectChanges();

    // WHEN
    component.isSaveGrayedOut();

    // THEN
    expect(component.isSaveGrayed).not.toBeTruthy();
  });

  it('should check if pricing exhibit is attached', () => {
    // GIVEN
    component.isPricingExhibitAttached = true;

    // WHEN
    component.checkAttachedPricingExhibit();
  });

  it('should proceed to save if pricing exhibit is not attached', () => {
    // GIVEN
    component.isPricingExhibitAttached = false;
    component.splitCaseList.contractPriceProfileSeq = 123;
    component.rows.push(SPLIT_CASE_ROW_1);
    spyOn(splitCaseFeeService, 'saveSplitCaseFee').and.returnValue(Observable.of([]));

    // WHEN
    component.checkAttachedPricingExhibit();

    // THEN
    expect(splitCaseFeeService.saveSplitCaseFee).toHaveBeenCalled();
    expect(component.splitCaseList.splitCaseFeeValues).toBeTruthy();
  });

  it('should proceed to save splitcase fees when exhibit is deleted', () => {
    // GIVEN
    component.splitCaseList.contractPriceProfileSeq = 123;
    component.rows.push(SPLIT_CASE_ROW_1);
    spyOn(splitCaseFeeService, 'saveSplitCaseFee').and.returnValue(Observable.of([]));

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(splitCaseFeeService.saveSplitCaseFee).toHaveBeenCalled();
    expect(component.splitCaseList.splitCaseFeeValues).toBeTruthy();
  });

  it('should delete exhibit', () => {
    // WHEN
    component.onExhibitDeletion(false);
  });

  it('should load splitcase component in furtherance editable mode if canEditFurtherance is true', () => {
    // GIVEN
    component.cppFurtheranceSeq = 123;
    spyOn(furtheranceService, 'canEditFurtherance').and.returnValue(Observable.of({canEditFurtherance: true}));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(component.canEditFurtherance).toBeTruthy();
    expect(furtheranceService.canEditFurtherance).toHaveBeenCalledWith(component.cppFurtheranceSeq);
  })

  it('should load splitcase component in view mode if canEditFurtherance is false', () => {
    // GIVEN
    component.cppFurtheranceSeq = 123;
    spyOn(furtheranceService, 'canEditFurtherance').and.returnValue(Observable.of({canEditFurtherance: false}));

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(component.canEditFurtherance).toBeFalsy();
    expect(furtheranceService.canEditFurtherance).toHaveBeenCalledWith(component.cppFurtheranceSeq);
  })
})

describe('Splitcase Furtherance component', () => {
  let component: SplitCaseComponent;
  let fixture: ComponentFixture<SplitCaseComponent>;
  let router: Router;
  let route: ActivatedRoute;
  let splitCaseFeeService: SplitCaseFeeService;
  let authService: AuthorizationService;
  let furtheranceService: FurtheranceService;

   const SPLIT_CASE_ROW_1: SplitCaseFees = new SplitCaseFees('GROCERY', 35, '%', new Date('2020-02-01'), new Date('2020-02-01'),
   '1', false, false);

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
    "isFurtheranceMode": false,
    "isPricingExhibitAttached": true,
    "penddate": new Date('12/12/2020'),
    "pstdate": new Date('12/12/2019'),
    "versionNumber": 1
   }

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SplitCaseComponent],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        DecimalPipe,
        Renderer2,
        StepperService,
        SplitCaseFeeService,
        AuthorizationService,
        FurtheranceService
      ]
    }).overrideTemplate(SplitCaseComponent, '')

    fixture = TestBed.createComponent(SplitCaseComponent);
    component = fixture.componentInstance;
    authService = TestBed.get(AuthorizationService);
    splitCaseFeeService = TestBed.get(SplitCaseFeeService);
    furtheranceService = TestBed.get(FurtheranceService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should save Splitcase Fees for pricing information flow', () => {
    // GIVEN
    component.splitCaseList.contractPriceProfileSeq = 123;
    component.rows.push(SPLIT_CASE_ROW_1);
    spyOn(splitCaseFeeService, 'saveSplitCaseFee').and.returnValue(Observable.of([]));

    // WHEN
    fixture.detectChanges();
    component.saveSplitCaseFee();

    // THEN
    expect(component.showSpinner).toBeFalsy();
    expect(splitCaseFeeService.saveSplitCaseFee).toHaveBeenCalled();
    expect(component.splitCaseList.splitCaseFeeValues).toBeTruthy();
  });
})
