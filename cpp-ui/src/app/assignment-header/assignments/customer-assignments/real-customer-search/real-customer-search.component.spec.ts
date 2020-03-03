import { By } from '@angular/platform-browser';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';

import { AssignmentsService, TranslatorService, ToasterService } from 'app/shared';
import { AssignmentMarkupModel, MarkupDisplayModel, RealCustomerModel, SubgroupDisplayModel} from 'app/assignment-header';
import { ItemLevelDisplayModel } from '../markup-display/markup-display.model';
import { RealCustomerSearchComponent, DeleteRealCustomerModel } from 'app/assignment-header';

describe('RealCustomerSearchComponent', () => {

   const CONTRACT_DETAILS = {
      agreementId: '75490bfc-289d-40d3-b644-ba095c2e1cea',
      cenddate: '12/30/2020',
      cname: 'My Contract',
      contractStatus: 'Draft',
      contractType: 'ICMDistributionAgreementRegional',
      cppid: 4,
      cppseqid: 21,
      cstdate: '12/30/2018',
      ctype: 'ICMDistributionAgreementRegional',
      penddate: '12/30/2020',
      pstdate: '12/30/2018',
      versionNumber: 2
    }

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

  let component: RealCustomerSearchComponent;
  let fixture: ComponentFixture<RealCustomerSearchComponent>;
  let assignmentService: AssignmentsService;

  beforeEach(() => {
    const mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    const mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RealCustomerSearchComponent],
      providers: [
        {provide: ToasterService, useValue: mockToasterService},
        {provide: TranslatorService, useValue: mockTranslateService},
        AssignmentsService]
    }).overrideTemplate(RealCustomerSearchComponent, '');

    fixture = TestBed.createComponent(RealCustomerSearchComponent);
    component = fixture.componentInstance;
    assignmentService = TestBed.get(AssignmentsService);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create RealCustomerSearch Component', () => {
    expect(component).toBeTruthy();
  });

  it('should display delete icon when search fields are more than 1', () => {
    // GIVEN
    component.realCustomerFields = 2;
    component.customerTypes = ['Family', 'PMG'];

    // WHEN
    component.ngDoCheck();

    // THEN
    expect(component.allowClear).toBeTruthy();
  });

  it('should not display delete icon for one search field', () => {
    // GIVEN
    component.realCustomerFields = 1;
    component.customerTypes = ['Family'];

    // WHEN
    component.ngDoCheck();

    // THEN
    expect(component.allowClear).toBeFalsy();
  });

  it('should assign customer Types', () => {
    // GIVEN
    component.contractDetails = CONTRACT_DETAILS;
    component.realCustomerData = new RealCustomerModel('Heinz', '12345',  'Family', '12', true, false, false);
    component.realCustomerTypes = ['Family', 'CMG', 'Local'];


    // WHEN
    component.ngOnInit();

    // THEN
    expect(component.customerTypes).toBe(component.realCustomerTypes);
  });

  it('should disable Find if no Customer Id and Type is selected', () => {
    // GIVEN
    component.customerId = '';

    // WHEN
    component.isFindGrayedOut();

    // THEN
    expect(component.isFindGrayedOut()).toBeTruthy();
  });

  it('should not disable Find if Customer Id or Type is selected', () => {
    // GIVEN
    component.customerId = '123';
    component.selectedCustomerTypeModel = 1;

    // WHEN
    component.isFindGrayedOut();

    // THEN
    expect(component.isFindGrayedOut()).toBeFalsy();
  });


  it('should find Real Customer if validation code is 200', () => {
    // GIVEN
    component.assignmentDetails = new AssignmentMarkupModel('2', 'Contract', 'CMG', 2,
    MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE, true, false, REAL_CUST_VALUE, 1);
    component.realCustomerData = new RealCustomerModel('Heinz', '12345', 'Family', '12', true, false, false);
    component.selectedCustomerTypeModel = 31;
    component.customerId = '12';
    component.cppSeq = '123';
    const REAL_CUSTOMER_1 = {
      customerName: 'Customer Name',
      validationCode: 200,
      validationMessage: 'abc123' };
    spyOn(assignmentService, 'findRealCustomer').and.returnValue(Observable.of(REAL_CUSTOMER_1));


    // WHEN
    component.findRealCustomer();

    // THEN
    expect(component.assignmentDetails.isAssignmentSaved).toBeFalsy();
  });

  it('should find Real Customer if validation code is 100', () => {
    // GIVEN
    component.assignmentDetails = new AssignmentMarkupModel('2', 'Contract', 'CMG', 2,
    MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE, true, false, REAL_CUST_VALUE, 1);
    component.realCustomerData = new RealCustomerModel('Heinz', '12345', 'Family', '12', true, false, false);
    component.selectedCustomerTypeModel = 31;
    component.customerId = '12';
    component.cppSeq = '123';
    const REAL_CUSTOMER_2 = {
      customerName: '',
      validationCode: 117,
      validationMessage: 'abc123' };
    spyOn(assignmentService, 'findRealCustomer').and.returnValue(Observable.of(REAL_CUSTOMER_2));


    // WHEN
    component.findRealCustomer();

    // THEN
    expect(component.realCustomerData.validationError).toBeTruthy();
  });

  it('should find Real Customer if validation code is not there', () => {
    // GIVEN
    component.assignmentDetails = new AssignmentMarkupModel('2', 'Contract', 'CMG', 2,
    MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE, true, false, REAL_CUST_VALUE, 1);
    component.realCustomerData = new RealCustomerModel('Heinz', '12345', 'Family', '12', true, false, false);
    component.selectedCustomerTypeModel = 31;
    component.customerId = '12';
    component.cppSeq = '123';
    const REAL_CUSTOMER_2 = {
      customerName: 'Customer Name',
      validationCode: '102',
      validationMessage: JSON.stringify({conceptName : 'Deepank contract', contractPriceProfileId: '123'})
    };
    spyOn(assignmentService, 'findRealCustomer').and.returnValue(Observable.of(REAL_CUSTOMER_2));


    // WHEN
    component.findRealCustomer();

    // THEN
    expect(component.realCustomerData.duplicateCustomerFound).toBe(false);
  });

  it('should display error message if Duplicate Customer is assigned', () => {
    // GIVEN
    component.assignmentDetails = new AssignmentMarkupModel('2', 'Contract', 'CMG', 2,
    MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE, true, false, REAL_CUST_VALUE, 1);
    component.realCustomerData = new RealCustomerModel('Heinz', '12345', 'Family', '12', false, false, false);
    component.selectedCustomerTypeModel = 31;
    component.customerId = '12';
    component.cppSeq = '123';
    const REAL_CUSTOMER_2 = {
      customerName: 'Customer Name',
      validationCode: 112,
      validationMessage: JSON.stringify({conceptName : 'Deepank contract', contractPriceProfileId: '123'})
    };
    spyOn(assignmentService, 'findRealCustomer').and.returnValue(Observable.of(REAL_CUSTOMER_2));


    // WHEN
    component.findRealCustomer();

    // THEN
    expect(component.realCustomerData.duplicateCustomerFound).toBe(true);
  });

  it('should display error message if Multiple Customer is assigned', () => {
    // GIVEN
    component.assignmentDetails = new AssignmentMarkupModel('2', 'Contract', 'CMG', 2,
    MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE, true, false, REAL_CUST_VALUE, 1);
    component.realCustomerData = new RealCustomerModel('Heinz', '12345', 'Family', '12', false, false, false);
    component.selectedCustomerTypeModel = 31;
    component.customerId = '12';
    component.cppSeq = '123';
    const REAL_CUSTOMER_2 = {
      customerName: 'Customer Name',
      validationCode: 113,
      validationMessage: JSON.stringify({conceptName : 'Deepank contract', contractPriceProfileId: '123'})
    };
    spyOn(assignmentService, 'findRealCustomer').and.returnValue(Observable.of(REAL_CUSTOMER_2));


    // WHEN
    component.findRealCustomer();

    // THEN
    expect(component.realCustomerData.validationError).toBe(true);
  });

  it('should validate for duplicate', () => {

    // GIVEN
    component.realCustomerData = {
    realCustomerName: 'Heinz Ketup',
    realCustomerId: '12',
    realCustomerType: '21',
    realCustomerGroupType: '',
    isCustomerSaved: false,
    duplicateCustomerFound: true,
    validationError: false
    };
    component.assignmentDetails = new AssignmentMarkupModel('2', 'Contract', 'CMG', 2,
    MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE, true, false, REAL_CUST_VALUE, 1);

    component.contractDetails = CONTRACT_DETAILS;

    // THEN
    expect(component.validateForDuplicate('12', 'Heinz')).toBeTruthy();

  });

  it('should delete Search Fields', () => {
    // GIVEN
    component.realCustomerData = {
      realCustomerName: 'Heinz Ketup',
      realCustomerId: '12',
      realCustomerType: '21',
      realCustomerGroupType: '',
      isCustomerSaved: false,
      duplicateCustomerFound: false,
      validationError: false
      };

    spyOn(component.deleteField, 'emit');

    // WHEN
    component.deleteSearchField();

    // THEN
    expect(component.deleteField.emit).toHaveBeenCalledWith(undefined);
  });

  it('should check CustomerKey Press', () => {
    // GIVEN
    const event = { 'target': { 'value': '' }, 'keyCode': 8 };

    // WHEN
    component.onCustomerKeyPress(event);

    // THEN
    expect(event.target.value).toBe('');
  });

  it('should check CustomerID on blur', () => {
    // GIVEN
    component.realCustomerData = {
      'realCustomerName': 'Heinz',
      'realCustomerId': '123',
      'realCustomerType': '12',
      'realCustomerGroupType': 'Family',
      'isCustomerSaved': true,
      'duplicateCustomerFound': false,
      'validationError': false
    }
    const event = {'target': { 'value': '' }, 'keyCode': 8 };

    // WHEN
    component.onBlurCustomerId(event);

    // THEN
    expect(event.target.value).toBe('');
  });

  it('should check CustomerID on key up', () => {
    // GIVEN
    component.realCustomerData = {
      'realCustomerName': 'Frooti',
      'realCustomerId': '456',
      'realCustomerType': '12',
      'realCustomerGroupType': 'Family',
      'isCustomerSaved': true,
      'validationError': false,
      'duplicateCustomerFound': false,
    }
    const event = {'target': { 'value': '' }, 'keyCode': 8 };

    // WHEN
    component.onKeyupCustomerId(event);

    // THEN
    expect(event.target.value).toBe('');
  });

  it('should erase the found customer name on change of customer type', () => {
    // GIVEN
    component.realCustomerData = {
      realCustomerName: 'Customer Name',
      realCustomerId: '123',
      realCustomerType: '32',
      realCustomerGroupType: 'PMG',
      isCustomerSaved: true,
      duplicateCustomerFound: false,
      validationError: false
    };

    // WHEN
    component.onDropDownChange();

    // THEN
    expect(component.realCustomerData.realCustomerName).toBe(null);

  });

  it('should set selectedCustomerTypeModel value', () => {
    // GIVEN
    component.realCustomerData = {
      'realCustomerName': 'Heinz',
      'realCustomerId': '123',
      'realCustomerType': '12',
      'realCustomerGroupType': 'Family',
      'isCustomerSaved': true,
      'duplicateCustomerFound': false,
      'validationError': false
    }
    const selectedType = 12;

    // WHEN
    fixture.detectChanges();
    component.ngOnInit();

    // THEN
    expect(component.selectedCustomerTypeModel).toBe(selectedType);
  });

  it('should delete assigned Real Customers', () => {
    // GIVEN
    component.gridMapKey = 'Model';
    spyOn(component.deleteField, 'emit');
    component.realCustomerData = {
      realCustomerName : 'CKN WNGS',
      realCustomerId: '12',
      realCustomerType: '31',
      realCustomerGroupType: 'Family',
      isCustomerSaved: true,
      duplicateCustomerFound: false,
      validationError: false
    }
    component.assignmentDetails = {
      gfsCustomerId: '12',
      markupName: 'Instant Noodles',
      gfsCustomerType: 'Family',
      gfsCustomerTypeId: 31,
      markupList: MARKUP_VALUE,
      subgroupList: SUBGROUP_VALUE,
      itemList: ITEM_VALUE,
      isDefault: false,
      isAssignmentSaved: false,
      realCustomerDTOList: REAL_CUST_VALUE,
      expireLowerInd: 1
    }
    const REAL_CUSTOMER: DeleteRealCustomerModel = new DeleteRealCustomerModel('12', '31');
    spyOn(assignmentService, 'deleteRealCustomer').and.returnValue(Observable.of([REAL_CUSTOMER]));
    spyOn(assignmentService, 'fetchActivatePricingButtonState').and.returnValue(Observable.of({enableActivatePricingButton: false}))

    // WHEN
    component.deleteRealCustomer();

    // THEN
    expect(component.deleteField.emit).toHaveBeenCalled();
  });

  it('should delete assigned default Real Customers', () => {
    // GIVEN
    component.gridMapKey = 'Model';
    spyOn(component.deleteField, 'emit');
    component.realCustomerData = {
      realCustomerName : 'CKN WNGS',
      realCustomerId: '12',
      realCustomerType: '31',
      realCustomerGroupType: 'Family',
      isCustomerSaved: true,
      duplicateCustomerFound: false,
      validationError: false
    }
    component.assignmentDetails = {
      gfsCustomerId: '12',
      markupName: 'Instant Noodles',
      gfsCustomerType: 'Family',
      gfsCustomerTypeId: 31,
      markupList: MARKUP_VALUE,
      subgroupList: SUBGROUP_VALUE,
      itemList: ITEM_VALUE,
      isDefault: true,
      isAssignmentSaved: false,
      realCustomerDTOList: REAL_CUST_VALUE,
      expireLowerInd: 1
    }
    const REAL_CUSTOMER: DeleteRealCustomerModel = new DeleteRealCustomerModel('12', '31');
    spyOn(assignmentService, 'deleteRealCustomer').and.returnValue(Observable.of([REAL_CUSTOMER]));
    spyOn(assignmentService, 'fetchActivatePricingButtonState').and.returnValue(Observable.of({enableActivatePricingButton: false}))

    // WHEN
    component.deleteRealCustomer();

    // THEN
    expect(component.deleteField.emit).toHaveBeenCalled();
  });

  it('should keep find disable if user both customer type and Id are not choosen', () => {
    // GIVEN
    component.realCustomerData = {
      realCustomerName: 'Customer Name',
      realCustomerId: '123',
      realCustomerType: '32',
      realCustomerGroupType: 'PMG',
      isCustomerSaved: true,
      duplicateCustomerFound: false,
      validationError: false
    };
    component.selectedCustomerTypeModel = -1;
    component.customerId = '';

    // WHEN
    component.findRealCustomer();

    // THEN
    expect(fixture.debugElement.query(By.css('.btn.btn-primary.mt-2.saved-find-customer'))).toBeFalsy();
  });

})
