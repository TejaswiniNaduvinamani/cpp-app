import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';

import { ItemDisplayComponent } from './item-display.component';
import { ItemAssignmentService, TranslatorService, ToasterService } from '../../../shared';
import { ItemAssignmentModel, FutureItemModel } from '../item-assignments.model';

describe('ItemDisplayComponent', () => {

  const ITEM_ASSIGNMENT_VALUE: Array<ItemAssignmentModel> = [
    new ItemAssignmentModel('12', '12 - Heinz Ketchup', false, false, false)
  ];

  const FUTURE_ITEM_VALUE: FutureItemModel = new FutureItemModel(21, 'Heinz Ketchup', 'Chillis',
   '150', 11, ITEM_ASSIGNMENT_VALUE, false, false, [] );

  const FUTURE_ITEM_VALUE_1: FutureItemModel = {
    'contractPriceProfileSeq': 21,
    'futureItemDesc': 'Heinz Ketchup',
    'exceptionName': 'Chillis',
    'gfsCustomerId': '150',
    'gfsCustomerTypeCode': 11,
    'itemAssignmentList': [
      {
        'itemId': '12',
        'itemDescription': '12 - Heinz Ketchup',
        'isItemSaved': false,
        'invalidItem': false,
        'itemExists': false,
      }
    ],
    'isFutureItemSaved': true,
    'isItemAlreadyExist': false,
    'duplicateItemIdList': []
  };

  let component: ItemDisplayComponent;
  let fixture: ComponentFixture<ItemDisplayComponent>;
  let itemService: ItemAssignmentService;

  beforeEach(() => {
    const mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    const mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ ItemDisplayComponent ],
      providers: [
        {provide: ToasterService, useValue: mockToasterService},
        {provide: TranslatorService, useValue: mockTranslateService},
        ItemAssignmentService
        ]
    }).overrideTemplate(ItemDisplayComponent, '');

    fixture = TestBed.createComponent(ItemDisplayComponent);
    component = fixture.componentInstance;
    itemService = TestBed.get(ItemAssignmentService);
  });

  it('should create item-display component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the map and create list', () => {
    // GIVEN
    component.itemAssignmentDetails = FUTURE_ITEM_VALUE_1;

    // WHEN
    component.ngOnInit();

    // THEN
    expect(component.itemMapKeyList).toBeDefined();
  });

  it('should add search field', () => {
    // GIVEN
    component.itemInd = 1;
    component.itemAssignmentDetails = FUTURE_ITEM_VALUE;

    // WHEN
    component.addAnotherItem();

    // THEN
    expect(component.itemInd).toBe(2);
  });

  it('should diasble save assignment for invalid item', () => {
    // GIVEN
    component.itemAssignmentDetails = FUTURE_ITEM_VALUE;

    // WHEN
    component.disableSaveAssignment();

    // THEN
    expect(component.disableSaveInd).toBeTruthy();
});

it('should remove the fields on delete', () => {
  // GIVEN
  const testMap = new Map();
  testMap.set(1, {itemId: '15', itemDescription: '15 - Heinz Ketchup'});
  testMap.set(2, {itemId: '16', itemDescription: '16 - Heinz Ketchup'});
  component.itemMap = testMap;
  component.itemAssignmentDetails = FUTURE_ITEM_VALUE;

  // WHEN
  component.onDelete(1);

  // THEN
  expect(component.itemMap.has('1')).toBe(false);
});

it('should disable Save Assignments Button after successful assignment of items', () => {
  // GIVEN
  component.itemAssignmentDetails = FUTURE_ITEM_VALUE_1;

  // WHEN
  fixture.detectChanges();
  component.disableSaveAfterItemAssignment();

  // THEN
  expect(component.disableSaveInd).toBeTruthy();
});

it('should build Item Object', () => {
  // GIVEN
  const testMap = new Map();
  testMap.set(1, {itemId: '15', itemDescription: '15 - Heinz Ketchup', isItemSaved: false});
  testMap.set(2, {itemId: '16', itemDescription: '16 - Heinz Ketchup', isItemSaved: false});
  component.itemMap = testMap;
  component.itemAssignmentDetails = FUTURE_ITEM_VALUE;
  // WHEN
  component.buildItemObject();

  // THEN
  expect(component.saveItem).toBeTruthy();
});

it('should Save Assignments', () => {
  // GIVEN
  const testMap = new Map();
  testMap.set(1, {itemId: '15', itemDescription: '15 - Heinz Ketchup', isItemSaved: false});
  testMap.set(2, {itemId: '16', itemDescription: '16 - Heinz Ketchup', isItemSaved: false});
  component.itemMap = testMap;
  component.itemAssignmentDetails = FUTURE_ITEM_VALUE;
  component.saveItem = new FutureItemModel(21, 'Heinz Ketchup', 'Chillis', '150', 11, ITEM_ASSIGNMENT_VALUE, false, false, []);
  spyOn(itemService, 'saveItemAssignment').and.returnValue(Observable.of([]));

  // WHEN
  component.saveAssignments();

  // THEN
  expect(component.itemSaveList).toBeDefined();
  expect(itemService.saveItemAssignment).toHaveBeenCalled();
});

it('should fetch error message when duplicate is found', () => {
  // GIVEN
  const testMap = new Map();
  testMap.set(1, {itemId: '15', itemDescription: '15 - Heinz Ketchup', isItemSaved: false});
  testMap.set(2, {itemId: '16', itemDescription: '16 - Heinz Ketchup', isItemSaved: false});
  component.itemMap = testMap;
  component.itemAssignmentDetails = FUTURE_ITEM_VALUE;
  component.saveItem = new FutureItemModel(21, 'Heinz Ketchup', 'Chillis', '150', 11, ITEM_ASSIGNMENT_VALUE, false, false, []);
  spyOn(itemService, 'saveItemAssignment').and.returnValue(Observable.of({
    errorCode: 150,
    errorMessage: 'Duplicate Found'
  }));

  // WHEN
  component.saveAssignments();

  // THEN
  expect(itemService.saveItemAssignment).toHaveBeenCalled();
  expect(component.duplicateList).toBe('Duplicate Found');
});

it('should create delete modal index', () => {
  // GIVEN
  const parentIndex = 1;
  component.index = 2;

  // WHEN
  component.createDeleteModalIndex(parentIndex);

  // THEN
  expect(component.childIndex).toBe(8);
});

});
