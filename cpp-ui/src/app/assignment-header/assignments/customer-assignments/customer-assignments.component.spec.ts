import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';

import { AssignmentMarkupModel, RealCustomerModel, SaveAssignmentModel, MarkupDisplayModel,
  SaveAssignmentResponseModel, SubgroupDisplayModel } from 'app/assignment-header';
import { CustomerAssignmentsComponent } from 'app/assignment-header';
import { ItemLevelDisplayModel } from 'app/assignment-header';
import { ToasterService, AssignmentsService, TranslatorService } from '../../../shared';

describe('CustomerAssignmentsComponent', () => {

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

  const ASSIGNMENT_DATA: AssignmentMarkupModel = new AssignmentMarkupModel('2', 'Contract', 'CMG', 2,
    MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE, true, false, REAL_CUST_VALUE, 1);

  const ASSIGNMENT_DATA_1: AssignmentMarkupModel = {
    'gfsCustomerId': '1949',
    'markupName': 'ASDF',
    'gfsCustomerType': '31',
    'gfsCustomerTypeId': 12,
    'markupList': [
      {
        'productType': 'GROCERY',
        'itemPriceId': 1,
        'markup': '44',
        'unit': '%',
        'markupType': 1,
        'effectiveDate': new Date('9999-01-01 00:00:00.0'),
        'expirationDate': new Date('9999-01-01 00:00:00.0')
      }
    ],
    'subgroupList': [
      {
        'subgroupDesc': "SAFETY EQUIPMENT & SUPPLIES",
        'markup': "4.00",
        'unit': "%",
        'markupType': 1,
      }
    ],
    'itemList': [
      {
        'noItemId': null,
        'itemId': '100003',
        'itemDesc': 'CKN WINGS SPLT FC IF STEAMED  30# INC Limited',
        'stockingCode': null,
        'markup': '35',
        'unit': '$',
        'markupType': 1,
        'effectiveDate': new Date('9999-01-01 00:00:00.0'),
        'expirationDate': new Date('9999-01-01 00:00:00.0')
      }
    ],
    'isDefault': true,
    'isAssignmentSaved': true,
    'realCustomerDTOList': [
      {
        'realCustomerName': 'ABC',
        'realCustomerId': '123',
        'realCustomerType': '31',
        'realCustomerGroupType': 'SPMG',
        'isCustomerSaved': true,
        'duplicateCustomerFound': false,
        'validationError': false

      }
    ],
    'expireLowerInd': 1
  };

  let component: CustomerAssignmentsComponent;
  let fixture: ComponentFixture<CustomerAssignmentsComponent>;
  let assignmentService: AssignmentsService;

  beforeEach(() => {
    const mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    const mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);


    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CustomerAssignmentsComponent],
      providers: [
        { provide: ToasterService, useValue: mockToasterService },
        { provide: TranslatorService, useValue: mockTranslateService },
        AssignmentsService
      ]
    }).overrideTemplate(CustomerAssignmentsComponent, '');

    fixture = TestBed.createComponent(CustomerAssignmentsComponent);
    component = fixture.componentInstance;
    assignmentService = TestBed.get(AssignmentsService);
  });

  it('should create Customer Assignment Component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the customer types', () => {
    // GIVEN
    component.assignmentData = ASSIGNMENT_DATA_1;
    component.customerTypes = ['CMG', 'PMG', 'Family'];

    // WHEN
    component.ngOnInit();

    // THEN
    expect(component.customerTypeList).toBe(component.customerTypes);
  });

  it('should add search field', () => {
    // GIVEN
    component.customerInd = 1;
    component.assignmentData = ASSIGNMENT_DATA;

    // WHEN
    component.addAnother();

    // THEN
    expect(component.customerInd).toBe(2);
  });

  it('should diasble save assignment for invalid customer', () => {
    // GIVEN
    component.assignmentData = ASSIGNMENT_DATA;

    // WHEN
    component.disableSaveAssignment();

    // THEN
    expect(component.disableSaveInd).toBeTruthy();
  });

  it('should diasble save assignment  when no no assignments saved', () => {
    // GIVEN
    component.assignmentData = ASSIGNMENT_DATA_1;

    // WHEN
    component.disableSaveAssignment();

    // THEN
   expect(component.disableSaveInd).toBeTruthy();
  });

  it('should test ngDoCheck()', () => {
    // GIVEN
    component.customerTypes = ['CMG', 'PMG', 'Family'];
    component.assignmentData = ASSIGNMENT_DATA;

    // WHEN
    component.ngDoCheck();

    // THEN
    expect(component.realCustomerListLength).toBe(0);
  });

  it('should remove the grid on delete', () => {
    // GIVEN
    const testMap = new Map();
    testMap.set(1, { customerType: 'Family', customerName: 'Heinz' });
    testMap.set(2, { customerType: 'CMG', customerName: 'Chilli' });
    component.gridMap = testMap;
    component.assignmentData = ASSIGNMENT_DATA;

    // WHEN
    component.onDelete(1);

    // THEN
    expect(component.gridMap.has('1')).toBe(false);
  });

  it('should change saved accordion to unsaved when all mappings are deleted', () => {
    // GIVEN
    const testMap = new Map();
    testMap.set(1, { customerType: 'Family', customerName: 'Heinz' });
    component.gridMap = testMap;
    component.assignmentData = ASSIGNMENT_DATA;

    // WHEN
    component.onDelete(1);

    // THEN
    expect(component.gridMap.has('1')).toBe(false);
    expect(component.assignmentData.isAssignmentSaved).toBe(false);
  });

  it('should build Real Customer Object', () => {
    // GIVEN
    const testMap = new Map();
    testMap.set(1, { customerType: 'Family', customerName: 'Heinz', isCustomerSaved: false });
    testMap.set(2, { customerType: 'CMG', customerName: 'Chilli', isCustomerSaved: false });
    component.gridMap = testMap;
    component.assignmentData = ASSIGNMENT_DATA_1;

    // WHEN
    component.buildRealCustomerObject();

    // THEN
    expect(component.saveAssignment).toBeTruthy();
  });

  it('should Save Assignments and refresh the realCustomerList', () => {
    // GIVEN
    const testMap = new Map();
    testMap.set(1, { customerType: 'Family', customerName: 'Heinz', isCustomerSaved: false });
    testMap.set(2, { customerType: 'CMG', customerName: 'Chilli', isCustomerSaved: false });
    component.gridMap = testMap;
    component.assignmentData = ASSIGNMENT_DATA_1;
    component.saveAssignment = new SaveAssignmentModel(12, '123', 1, REAL_CUST_VALUE);
    spyOn(assignmentService, 'saveAssignment').and.returnValue(Observable.of([]));
    spyOn(assignmentService, 'fetchActivatePricingButtonState').and.returnValue(Observable.of({enableActivatePricingButton: false}));
    // WHEN
    component.saveAssignments();

    // THEN
    expect(component.realCustomerSaveList).toBeDefined();
  });

  it('should disable Save Assignments Button after successful assignment of Real Customers', () => {
    // GIVEN
    component.assignmentData = ASSIGNMENT_DATA_1;

    // WHEN
    component.disableSaveAfterRealCustomerAssignment();

    // THEN
    expect(component.disableSaveInd).toBeTruthy();
  });

  it('should enable Save Assignment if some real customer are present', () => {
    // GIVEN
    const testMap = new Map();
    testMap.set(1, { customerType: 'Family', realCustomerName: 'Heinz', isCustomerSaved: false, realCustomerTypeId: 12 });
    testMap.set(2, { customerType: 'CMG', realCustomerName: 'Chilli', isCustomerSaved: false, realCustomerTypeId: 13 });
    component.gridMap = testMap;
    component.gridMapKeyList = Array.from(testMap.keys());

    // WHEN
    component.disableSaveForEmptyValues();

    // THEN
    expect(component.disableSaveInd).toBeFalsy();
  });

  it('should disable Save Assignment if only empty search fields are present', () => {
    // GIVEN
    const testMap = new Map();
    testMap.set(1, { customerType: null, realCustomerName: null, isCustomerSaved: false, realCustomerTypeId: null });
    testMap.set(2, { customerType: null, realCustomerName: null, isCustomerSaved: false, realCustomerTypeId: null });
    component.gridMap = testMap;
    component.gridMapKeyList = Array.from(testMap.keys());

    // WHEN
    component.disableSaveForEmptyValues();

    // THEN
    expect(component.disableSaveInd).toBeTruthy();
  });

  it('should create index', () => {

    // GIVEN
    component.index = 0;
    const parentIndex = 1;

    // WHEN
    component.createIndex(parentIndex);

    // THEN
    expect(component.createIndex).toBeTruthy();
  });

  it('should validate for duplicates', () => {
    // GIVEN
    const EVENT = {
      key: 1,
      realCustomerId: 12,
      realCustomerType: 'Heinz'
    };
    const testMap = new Map();
    testMap.set(1, { realCustomerId: 12, realCustomerType: 'Heinz', duplicateCustomerFound: false });
    testMap.set(2, { realCustomerId: 12, realCustomerType: 'Heinz', duplicateCustomerFound: true });
    component.gridMap = testMap;
    component.gridMapKeyList = Array.from(testMap.keys());

    // WHEN
    component.validateForDuplicates(EVENT);

    // THEN
    expect(component.disableSaveInd).toBeTruthy();
  });

  it('should validate for duplicates condition2', () => {
    // GIVEN
    const EVENT = {
      key: 1,
      realCustomerId: 12,
      realCustomerType: 'Heinz'
    };
    const testMap = new Map();
    testMap.set(1, { realCustomerId: 12, realCustomerType: 'Heinz' });
    component.gridMap = testMap;
    component.gridMapKeyList = Array.from(testMap.keys());

    // WHEN
    component.validateForDuplicates(EVENT);

    // THEN
    expect(component.disableSaveInd).toBeTruthy();
  });

  it('should emit pricing activate state', () => {
    // GIVEN
    spyOn(component.enableActivatePricing, 'emit');

    // WHEN
    component.emitPricingActivateState(true);

    // THEN
    expect(component.enableActivatePricing.emit).toHaveBeenCalledWith(true);
  });

  it('should detect delete of default concept mapping', () => {
    // GIVEN
    spyOn(component.defaultAssignmentDelete, 'emit');

    // WHEN
    component.detectDefaultAssignmentDelete(true);

    // THEN
    expect(component.defaultAssignmentDelete.emit).toHaveBeenCalledWith(true);
  });
})

export class TranslateServiceStub {

public translate(key: string): string {
    if (key === 'VALIDATION_CODE.112_DUPLICATE_ON_SAVE') {
      return 'Duplicate customers found. Please fix and try again. ';
    } else if (key === 'VALIDATION_CODE.121') {
      return 'Please assign a customer to the contract level concept.';
    } else if (key === 'VALIDATION_CODE.116') {
        return 'Please enter customers that belong to the membership hierarchy. ';
      } else {
        return 'Sample Error Message';
      }
    }
}

describe('CustomerAssignmentsComponent-Validations', () => {
  let component: CustomerAssignmentsComponent;
  let fixture: ComponentFixture<CustomerAssignmentsComponent>;
  let assignmentService: AssignmentsService;
  beforeEach(() => {
    const mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CustomerAssignmentsComponent],
      providers: [
        { provide: ToasterService, useValue: mockToasterService },
        { provide: TranslatorService, useClass: TranslateServiceStub },
        AssignmentsService
      ]
    }).overrideTemplate(CustomerAssignmentsComponent, '');

    fixture = TestBed.createComponent(CustomerAssignmentsComponent);
    component = fixture.componentInstance;
    assignmentService = TestBed.get(AssignmentsService);
  });

  it('should construct validation message in case of duplicate code', () => {
    // GIVEN
    const errorCode = 112;
    const errorMessage = '(Raphiel Icecream, Dunkin Donuts)';

    // WHEN
    component.constructValidationMessage(errorCode, errorMessage);

    // THEN
    expect(component.validationMsg + component.duplicateList).toBe
    ('Duplicate customers found. Please fix and try again. (Raphiel Icecream, Dunkin Donuts)');

  });

  it('should construct error message for unassigned default concept', () => {
    // GIVEN
    const errorCode = 121;

    // WHEN
    component.constructValidationMessage(errorCode);

    // THEN
    expect(component.validationMsg).toBe('Please assign a customer to the contract level concept.');
  });

  it('should construct error message for invalid member Hierarchy', () => {
    // GIVEN
    const errorCode = 116;
    const errorMessage = '(Cream Stone Dessert, Raspberry Pie)';

    // WHEN
    component.constructValidationMessage(errorCode, errorMessage);

    // THEN
    expect(component.validationMsg + component.memberHierarchyErrorMessage).toBe
      ('Please enter customers that belong to the membership hierarchy. (Cream Stone Dessert, Raspberry Pie)');
  });

  it('should run a generic test for all other error code', () => {
    // GIVEN
   const ERROR_CODE = [113, 114, 117, 119, 122];

    // WHEN
    ERROR_CODE.forEach(code => {
      component.constructValidationMessage(code);

      // THEN
      expect(component.validationMsg).toBe('Sample Error Message');
    });
  })
})




