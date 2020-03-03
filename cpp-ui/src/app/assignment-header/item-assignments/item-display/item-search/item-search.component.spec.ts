import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Observable';

import { ContractInformationDetails } from 'app/contract/contract-information/contract-information.model';
import { ItemAssignmentModel, FutureItemModel } from 'app/assignment-header';
import { ItemSearchComponent } from './item-search.component';
import { MarkupService, ItemAssignmentService, TranslatorService, ToasterService } from 'app/shared';


describe('ItemSearchComponent', () => {

  const CONTRACT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    'IFS', new Date('01/26/2020'), new Date('01/30/2020'), 'Draft', new Date('01/26/2020'), new Date('01/30/2020'), '123', 'edit', '2');

 const ITEM_ASSIGNMENT_VALUE: Array<ItemAssignmentModel> = [new ItemAssignmentModel('12', '12 - Heinz Ketchup', false, false, false)];

 const ITEM_ASSIGNMENT_LIST: ItemAssignmentModel = new ItemAssignmentModel('166720', 'BLUEBERRY', false, false, false);

  const FUTURE_ITEM_VALUE: FutureItemModel = new FutureItemModel(21, 'Heinz Ketchup', 'Chillis', '150',
     11, ITEM_ASSIGNMENT_VALUE, false, false, []);

  let component: ItemSearchComponent;
  let fixture: ComponentFixture<ItemSearchComponent>;
  let itemAssignmentService: ItemAssignmentService;
  let toaster: ToasterService;

  beforeEach(() => {
    const mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    const mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ItemSearchComponent],
      providers: [
        ItemAssignmentService,
        MarkupService,
        {provide: ToasterService, useValue: mockToasterService},
        {provide: TranslatorService, useValue: mockTranslateService}
      ]
    }).overrideTemplate(ItemSearchComponent, '');

    fixture = TestBed.createComponent(ItemSearchComponent);
    component = fixture.componentInstance;
    itemAssignmentService = TestBed.get(ItemAssignmentService);
    toaster = TestBed.get(ToasterService);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create ItemSearchComponent component', () => {
    // THEN
    expect(component).toBeTruthy();
  });

  it('should fetch item description for valid ItemId', () => {
    // GIVEN
    component.itemId = '166720';
    component.itemData = ITEM_ASSIGNMENT_LIST;
    component.itemDetails = FUTURE_ITEM_VALUE;
    spyOn(itemAssignmentService, 'fetchItemDescriptionForAssignment').and.returnValue(Observable.of({
      'itemNo': '101',
      'itemDescription': '1-6/4 PK 9.5 OZ FRAPPUCCINO VA',
      'itemStatusCode': 'AC',
      'stockingCode ': '3',
      'isActive': true,
      'isValid': true
    }));

    // WHEN
    component.findItemDescription();

    // THEN

    expect(component.itemData.itemDescription).toBe('1-6/4 PK 9.5 OZ FRAPPUCCINO VA');
    expect(component.itemDetails.isFutureItemSaved).toBeFalsy();
  });

  it('should not fetch item description for invalid ItemId ', () => {
    // GIVEN
    component.itemId = '166720';
    component.itemData = ITEM_ASSIGNMENT_LIST;
    component.itemDetails =  FUTURE_ITEM_VALUE;
    spyOn(itemAssignmentService, 'fetchItemDescriptionForAssignment').and.returnValue(Observable.of({
      'isActive': true,
      'isValid': false
    }));

    // WHEN
    component.findItemDescription();

    // THEN
    expect(itemAssignmentService.fetchItemDescriptionForAssignment).toHaveBeenCalled();
  });

  it('should display duplicate item error if found', () => {
    // GIVEN
    component.itemId = '166720';
    component.itemData = ITEM_ASSIGNMENT_LIST;
    component.itemDetails = FUTURE_ITEM_VALUE;
    spyOn(itemAssignmentService, 'fetchItemDescriptionForAssignment').and.returnValue(Observable.of({
      'isActive': true,
      'isValid': false,
      'isItemAlreadyExist': true

    }));

    // WHEN
    component.findItemDescription();

    // THEN
    expect(component.itemData.itemExists).toBeTruthy();
  });

 it('should display delete icon when search fields are more than 1', () => {
    // GIVEN
    component.rows = 2;

    // WHEN
    component.ngDoCheck();

    // THEN
    expect(component.allowClear).toBeTruthy();
  });

  it('should not display delete icon for one search field', () => {
    // GIVEN
    component.rows = 1;

    // WHEN
    component.ngDoCheck();

    // THEN
    expect(component.allowClear).toBeFalsy();
  });

  it('should disable Find if no Customer Id and Type is selected', () => {
    // GIVEN
    component.itemId = '';

    // WHEN
    component.isFindItemGrayOut();

    // THEN
    expect(component.isFindItemGrayOut()).toBeTruthy();
  });

  it('should not disable Find if Customer Id or Type is selected', () => {
    // GIVEN
    component.itemId = '123';

    // WHEN
    component.isFindItemGrayOut();

    // THEN
    expect(component.isFindItemGrayOut()).toBeFalsy();
  });

  it('should delete Search Fields for invalid items', () => {
    // GIVEN
    component.invalid = true;
    component.inactive = true;
    spyOn(component.deleteField, 'emit');

    // WHEN
    component.deleteSearchField();

    // THEN
    expect(component.deleteField.emit).toHaveBeenCalledWith(undefined);
  });

  it('should delete Search Fields', () => {
    // GIVEN
    spyOn(component.deleteField, 'emit');

    // WHEN
    component.deleteSearchField();

    // THEN
    expect(component.deleteField.emit).toHaveBeenCalledWith(undefined);
  });


  it('should emit the index of deleted row on click of clear icon', () => {
    // GIVEN
    component.onItemLevelMarkup = true;
    component.index = 1;
    spyOn(component.deleteField, 'emit');

    // WHEN
    component.deleteSearchField();

    // THEN
    expect(component.deleteField.emit).toHaveBeenCalledWith(1);
  });

  it('should should check ItemKey Press', () => {
    // GIVEN
    const event = { 'target': { 'value': '' }, 'keyCode': 8 };

    // WHEN
    component.onItemKeyPress(event);

    // THEN
    expect(event.target.value).toBe('');
  });

  it('should check ItemId on blur', () => {
    // GIVEN
    const event = {'target': { 'value': '' }, 'keyCode': 8 };
    component.itemData = ITEM_ASSIGNMENT_LIST;
    // WHEN
    component.onBlurItemId(event);
    fixture.detectChanges();

    // THEN
    expect(event.target.value).toBe('');
  });

  it('should check ItemId on key up', () => {
    // GIVEN
    const event = {'target': { 'value': '' }, 'keyCode': 8 };
    component.itemData = ITEM_ASSIGNMENT_LIST;
    // WHEN
    component.onKeyupItemId(event);
    fixture.detectChanges();

    // THEN
    expect(event.target.value).toBe('');
    expect(component.itemData.itemDescription).toBeNull();
  });

  it('should assign itemID', () => {

    // GIVEN
    component.itemData = ITEM_ASSIGNMENT_LIST;

    // WHEN
    component.ngOnInit();

    // THEN
    expect(component.itemId).toBe('166720');
  });

  it('should invoke deleteItemAssignment service and delete assigned item', () => {
    // GIVEN
    component.itemDetails = FUTURE_ITEM_VALUE;
    component.itemData = ITEM_ASSIGNMENT_LIST;
    spyOn(component.deleteField, 'emit');
    spyOn(itemAssignmentService, 'deleteItemAssignment').and.returnValue(Observable.of([]));

    // WHEN
    component.onDeleteItemAssignment();

    // THEN
    expect(itemAssignmentService.deleteItemAssignment).toHaveBeenCalled();
    expect(component.deleteField.emit).toHaveBeenCalled();
    expect(toaster.showSuccess).toHaveBeenCalled();
  });

  it('should return true and show trash icon if item is already saved', () => {
    // GIVEN
    component.onItemLevelMarkup = true;
    component.isViewMode = true;
    component.itemData =  {
      'itemId': '517541',
      'itemDescription': 'Heinz Ketchup',
      'isItemSaved': true,
      'invalidItem': false,
      'itemExists': false
    };

    // WHEN
    component.showHideTrashIcon();

    // THEN
    expect(component.showHideTrashIcon()).toBeTruthy();
  });

  it('should return false and hide trash icon if item is not saved ', () => {
    // GIVEN
    component.onItemLevelMarkup = false;
    component.isViewMode = true;
    component.itemData =  {
      'itemId': '517541',
      'itemDescription': 'Heinz Ketchup',
      'isItemSaved': false,
      'invalidItem': false,
      'itemExists': false
    };

    // WHEN
    component.showHideTrashIcon();

    // THEN
    expect(component.showHideTrashIcon()).toBeFalsy();
  });

  it('should return true and show clear icon if item is already saved', () => {
    // GIVEN
    component.allowClear = true;
    component.onItemLevelMarkup = true;
    component.isViewMode = false;
    component.itemData =  {
      'itemId': '517541',
      'itemDescription': 'Heinz Ketchup',
      'isItemSaved': false,
      'invalidItem': false,
      'itemExists': false
    };

    // WHEN
    component.showHideClearIcon();

    // THEN
    expect(component.showHideClearIcon()).toBeTruthy();
  });

  it('should return false and hide clear icon if item is not saved ', () => {
    // GIVEN
    component.onItemLevelMarkup = false;
    component.isViewMode = true;
    component.itemData =  {
      'itemId': '517541',
      'itemDescription': 'Heinz Ketchup',
      'isItemSaved': false,
      'invalidItem': false,
      'itemExists': false
    };

    // WHEN
    component.showHideClearIcon();

    // THEN
    expect(component.showHideClearIcon()).toBeFalsy();
  });

  it('should display itemAssignmentDeleteModal', () => {
    // GIVEN
    component.onItemLevelMarkup = true;
    spyOn(component.deleteField, 'emit');

    // WHEN
    component.itemAssignmentDeleteModal();

    // THEN
    expect(component.deleteField.emit).toHaveBeenCalled();

     // GIVEN
     component.onItemLevelMarkup = false;

     // WHEN
     component.itemAssignmentDeleteModal();

     // THEN
     expect(component.itemAssignmentDeleteModal).toBeTruthy();

  })
})

