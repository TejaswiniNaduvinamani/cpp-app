import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DecimalPipe } from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Observable';
import { Renderer2, ElementRef } from '@angular/core';

import { ItemLevelMarkupComponent } from './item-level-markup.component';
import { ItemLevelMarkupModel, ItemDetailsForFurtheranceModel } from './item-level-markup.model';
import { MarkupGridModel, MarkupGridDetails } from './../markup-grid/markup-grid.model';
import { MarkupService, TranslatorService, ToasterService, AuthorizationService, FurtheranceService } from 'app/shared';
import { SubgroupMarkupModel } from '../subgroup-markup/subgroup-markup.model';
import { SessionStorageInterface } from 'app/contract';
import { FutureItemModel, ItemAssignmentModel, ItemSaveStatusModel } from 'app/assignment-header';

describe('Item-Level-Markup Component', () => {
  const MARKUP_GRID_MODEL: MarkupGridModel = new MarkupGridModel('GROCERY', 1, '1', '%', 1,
  new Date('12/30/2020'), new Date('12/30/2021'), false, false);
  const MARKUP_GRID_MODEL2: MarkupGridModel = new MarkupGridModel('FROZEN', 2, '0.00', '$', 1,
  new Date('12/30/2020'), new Date('12/30/2021'), false, false);

  const DEFAULT_SUBGROUP: SubgroupMarkupModel = new SubgroupMarkupModel('123', 'My Subgroup', '1.00', '$', 1, new Date('2019/02/20'),
     new Date('2019/02/26'), false, false, false, false, false);

  const ITEM_LEVEL_GRID_MODEL: ItemLevelMarkupModel = new ItemLevelMarkupModel(false, '166720', '1', 'BLUEBERRY IQF 4-5# GFS', '11',
   '%', 1, new Date('12/30/2020'), new Date('12/30/2021'), false, false, false, false, false, false);

  const ITEM_LEVEL_GRID_MODEL2: ItemLevelMarkupModel = new ItemLevelMarkupModel(false, '166720', '1', 'BLUEBERRY IQF 4-5# GFS', '11',
   '%', 1, new Date('12/30/2020'), new Date('12/30/2021'), false, false, false, false, false, true);

  const ITEM_LEVEL_GRID_MODEL_FURTHERANCE: ItemLevelMarkupModel = new ItemLevelMarkupModel(false, '166720', '1', 'BLUEBERRY IQF 4-5# GFS',
  '11', '%', 1, new Date('12/30/2020'), new Date('12/30/2021'), false, false, true, false, false, false);

  const MARKUP_GRID_DETAILS: MarkupGridDetails = new MarkupGridDetails('151', 31, 123, 'qwerty', [MARKUP_GRID_MODEL,
    MARKUP_GRID_MODEL2], [DEFAULT_SUBGROUP], [ITEM_LEVEL_GRID_MODEL], true, 1);

  const ITEM_ASSIGNMENT_VALUE: Array<ItemAssignmentModel> = [new ItemAssignmentModel('12', '12 - Heinz Ketchup', false, false, false)];

  const FUTURE_ITEM_VALUE: FutureItemModel = new FutureItemModel(21, 'Heinz Ketchup', 'Chillis', '150',
    11, ITEM_ASSIGNMENT_VALUE, false, false, []);

  const ITEM_SAVE_STATUS_MODEL: ItemSaveStatusModel = new ItemSaveStatusModel(200, 'Item Saved');

  const ITEM_DETAILS: ItemDetailsForFurtheranceModel = new ItemDetailsForFurtheranceModel(12, 123, 'Hienz Ketchup', '151', 31,
   ITEM_ASSIGNMENT_VALUE, []);

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
  "cppFurtheranceSeq": 123,
  "isPricingExhibitAttached": true,
  "penddate": new Date('12/12/2020'),
  "pstdate": new Date('12/12/2019'),
  "versionNumber": 1
 }

  let component: ItemLevelMarkupComponent;
  let fixture: ComponentFixture<ItemLevelMarkupComponent>;
  let markupService: MarkupService;
  let furtheranceService: FurtheranceService;
  let mockTranslateService;
  let mockToasterService;

  beforeEach(() => {

    mockTranslateService =  jasmine.createSpyObj<TranslatorService>('translateService', ['translate',  'translateWithValue']);
    mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ItemLevelMarkupComponent],
      providers: [
        DecimalPipe,
        FurtheranceService,
        Renderer2,
        MarkupService,
        AuthorizationService,
        {provide: TranslatorService, useValue: mockTranslateService},
        {provide: ToasterService, useValue: mockToasterService},
      ]
    }).overrideTemplate(ItemLevelMarkupComponent, '');

    fixture = TestBed.createComponent(ItemLevelMarkupComponent);
    component = fixture.componentInstance;
    markupService = TestBed.get(MarkupService);
    furtheranceService = TestBed.get(FurtheranceService);
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create the Item-Level-Markup component', () => {
    // WHEN
    fixture.detectChanges();
    // THEN
   expect(component).toBeTruthy();
  });

  it('should update the user entered markup value ', () => {

    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '10' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editItemLevelGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onMarkupKeyUp(event, row);

    // THEN
    expect(row.markup).toBe('10');
    expect(component.editItemLevelGrid.emit).toHaveBeenCalled();
  });

  it('should set error flag if Item level markup value is invalid', () => {
    // GIVEN
    const row = ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '1000' }};

    fixture.detectChanges();
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.renderer, 'addClass');
    spyOn(component.renderer, 'removeClass');

    // WHEN
    component.onblurMarkup(event, row);

    // THEN
    expect(row.invalidMarkup).toBeTruthy();
  });

  it('should set error flag if Item level markup value is Non-numeric', () => {
    // GIVEN
    const row = ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': 'abcd' }};

    fixture.detectChanges();
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.renderer, 'addClass');
    spyOn(component.renderer, 'removeClass');

    // WHEN
    component.onblurMarkup(event, row);

    // THEN
    expect(row.isInvalidMarkupCurrency).toBeTruthy();
  });

  it('should rounf-off Item level markup value when valid value is entered', () => {
    // GIVEN
    const row = ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '10' }};

    fixture.detectChanges();
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.renderer, 'addClass');
    spyOn(component.renderer, 'removeClass');

    // WHEN
    component.onblurMarkup(event, row);

    // THEN
    expect(row.isInvalidMarkupCurrency).toBeFalsy();
    expect(row.invalidMarkup).toBeFalsy();
    expect(row.markup).toBe('10.00');
  });

  it('should rounf-off Item level markup value and set error flag when 0 is entered', () => {
    // GIVEN
    const row = ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '0' }};

    fixture.detectChanges();
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.renderer, 'addClass');

    // WHEN
    component.onblurMarkup(event, row);

    // THEN
    expect(row.isInvalidMarkupCurrency).toBeFalsy();
    expect(row.invalidMarkup).toBeTruthy();
    expect(row.markup).toBe('0.00');
  });

  it('should fetch item description for valid ItemId', () => {
    // GIVEN
    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '166720' }};
    component.itemLevelMarkupList = [ITEM_LEVEL_GRID_MODEL];
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.itemDescriptionStatus, 'emit');
    spyOn(component.renderer, 'addClass');
    spyOn(markupService, 'fetchItemDescription').and.returnValue(Observable.of({
      'itemNo': '101',
      'itemDescription': '1-6/4 PK 9.5 OZ FRAPPUCCINO VA',
      'itemStatusCode': 'AC',
      'stockingCode ': '3',
      'isActive': true,
      'isValid': true
    }));
    fixture.detectChanges();

    // WHEN
    component.onblurItemId(event, row);

    // THEN
    expect(markupService.fetchItemDescription).toHaveBeenCalled();
    expect(row.itemDesc).toBe('1-6/4 PK 9.5 OZ FRAPPUCCINO VA');
    expect(component.itemDescriptionStatus.emit).toHaveBeenCalled();
  });

  it('should not fetch item description for already existing ItemId', () => {
    // GIVEN
    const row =  ITEM_LEVEL_GRID_MODEL2;
    const event = { 'target': { 'value': '166720' }};
    component.itemLevelMarkupList = [ITEM_LEVEL_GRID_MODEL2];
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.itemDescriptionStatus, 'emit');
    spyOn(component.renderer, 'addClass');
    spyOn(markupService, 'fetchItemDescription').and.returnValue(Observable.of({
      'isItemAlreadyExist': true
    }));
    fixture.detectChanges();

    // WHEN
    component.onblurItemId(event, row);

    // THEN
    expect(row.itemDesc).toBe('');
    expect(component.itemDescriptionStatus.emit).toHaveBeenCalled();
  });

  it('should trim item description on focus out', () => {
    // GIVEN
    const row =  ITEM_LEVEL_GRID_MODEL;
    component.itemLevelMarkupList = [ITEM_LEVEL_GRID_MODEL];
    spyOn(markupService, 'fetchItemDescription').and.returnValue(Observable.of({
      'itemNo': '101',
      'itemDescription': '1-6/4 PK 9.5 OZ FRAPPUCCINO VA    ',
      'itemStatusCode': 'AC',
      'stockingCode ': '3',
      'isActive': true,
      'isValid': true
    }));
    fixture.detectChanges();

    // WHEN
    component.onblurItemDesc(row);

    // THEN
    expect(row.itemDesc).toBe('1-6/4 PK 9.5 OZ FRAPPUCCINO VA');
  });

  it('should not fetch item description for invalid ItemId ', () => {
    // GIVEN
    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '1' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    component.itemLevelMarkupList = [ITEM_LEVEL_GRID_MODEL];
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.itemDescriptionStatus, 'emit');
    spyOn(component.renderer, 'addClass');
    spyOn(markupService, 'fetchItemDescription').and.returnValue(Observable.of({
      'isActive': true,
      'isValid': false
    }));
    fixture.detectChanges();

    // WHEN
    component.onblurItemId(event, row);

    // THEN
    expect(row.itemDesc).toBe('');
    expect(component.itemDescriptionStatus.emit).toHaveBeenCalled();
  });

  it('should not fetch item description for inactive ItemId ', () => {
    // GIVEN
    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '777790' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    component.itemLevelMarkupList = [ITEM_LEVEL_GRID_MODEL];
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.itemDescriptionStatus, 'emit');
    spyOn(component.renderer, 'addClass');
    spyOn(markupService, 'fetchItemDescription').and.returnValue(Observable.of({
      'isActive': false,
      'isValid': true
    }));
    fixture.detectChanges();

    // WHEN
    component.onblurItemId(event, row);

    // THEN
    expect(row.itemDesc).toBe('');
    expect(component.itemDescriptionStatus.emit).toHaveBeenCalled();
  });

  it('should update the user entered ItemDesc value', () => {

    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': 'Hienz Ketchup' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editItemLevelGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onKeyupItemDesc(event, row);

    // THEN
    expect(row.itemDesc).toBe('Hienz Ketchup');
    expect(component.editItemLevelGrid.emit).toHaveBeenCalled();
  });

  it('should emit event and update value on paste of item desc', () => {

    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editItemLevelGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onPasteItemDesc();

    // THEN
    expect(component.markupGridDetails.isMarkupSaved).toBeFalsy();
    expect(component.editItemLevelGrid.emit).toHaveBeenCalled();
  });

  it('should update the user entered MarkupType value', () => {

    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': 3 }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editItemLevelGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onMarkupTypeChange(event, row);

    // THEN
    expect(row.markupType).toBe(3);
    expect(component.editItemLevelGrid.emit).toHaveBeenCalled();
  });

  it('should update the user entered unit % value', () => {

    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '%' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editItemLevelGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onUnitChange(event, row);

    // THEN
    expect(row.unit).toBe('%');
    expect(component.editItemLevelGrid.emit).toHaveBeenCalled();
  });

  it('should update the user entered unit $ value', () => {

    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = { 'target': { 'value': '$' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editItemLevelGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onUnitChange(event, row);

    // THEN
    expect(row.unit).toBe('$');
    expect(component.editItemLevelGrid.emit).toHaveBeenCalled();
  });

  it('should update the row on check of NoItemId', () => {

    const row =  ITEM_LEVEL_GRID_MODEL;
    row.noItemId = false;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editItemLevelGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onChangeNoItemId(row);

    // THEN
    expect(row.noItemId).toBe(true);
    expect(component.editItemLevelGrid.emit).toHaveBeenCalled();
  });

  it('should update the row on uncheck of NoItemId', () => {

    const row =  ITEM_LEVEL_GRID_MODEL;
    row.noItemId = true;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editItemLevelGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onChangeNoItemId(row);

    // THEN
    expect(row.noItemId).toBe(false);
    expect(component.editItemLevelGrid.emit).toHaveBeenCalled();
  });

  it('should allow only positive numeric value for Item Id', () => {
    // GIVEN
    const row =  ITEM_LEVEL_GRID_MODEL;
    const event = jasmine.createSpyObj('e', [ 'preventDefault' ]);

    // WHEN
    component.onItemIdKeyPress(event, row);

    // THEN
    expect(row.itemId).toBe('');
    expect(event.preventDefault).toHaveBeenCalled();
  });

  it('should get itemId and itemDesc attributes', () => {
    // GIVEN
    spyOn(component.itemLevelDeleteInfo, 'emit');
    const element = new ElementRef({
      getAttribute: () => {
        return 'BLUEBERRY IQF 4-5# GFS';
      }
    });
    const element1 = new ElementRef({
      getAttribute: () => {
        return '166720';
      }
    });
    fixture.detectChanges();

    // WHEN
    component.onDeleteItemLevelMarkup(element.nativeElement);

    // THEN
    expect(component.itemLevelDeleteModel.itemDescription).toBe('BLUEBERRY IQF 4-5# GFS');

    // WHEN
    component.onDeleteItemLevelMarkup(element1.nativeElement);

    // THEN
    expect(component.itemLevelDeleteModel.itemNo).toBe('166720');
    expect(component.itemLevelDeleteInfo.emit).toHaveBeenCalled();
  });

  it('should set isMarkupSaved to false on edit of item Id', () => {
   // GIVEN
    const row =  ITEM_LEVEL_GRID_MODEL;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': '10' }};
    fixture.detectChanges();

    // WHEN
    component.onItemIdKeyUp(event, row);

    // THEN
    expect(component.markupGridDetails.isMarkupSaved).toBeFalsy();
  });

  it('should disable the markup unit for furtherance and saved item', () => {
    // GIVEN
    const row = ITEM_LEVEL_GRID_MODEL_FURTHERANCE;
    component.isFurtheranceMode = true;

    // WHEN
    component.isMarkupUnitDisable(row);

    // THEN
    expect(component.isMarkupUnitDisable).toBeTruthy();
  });

  it('should disable the markup unit for furtherance mode and true value of markup on sell', () => {
    // GIVEN
    const row = ITEM_LEVEL_GRID_MODEL;
    component.isFurtheranceMode = true;
    component.markupOnSellVal = true;

    // WHEN
    component.isMarkupUnitDisable(row);

    // THEN
    expect(component.isMarkupUnitDisable).toBeTruthy();
  });

  it('should disable the markup unit for furtherance mode and false value of markup on sell', () => {
    // GIVEN
    const row = ITEM_LEVEL_GRID_MODEL;
    component.isFurtheranceMode = true;
    component.markupOnSellVal = false;

    // WHEN
    component.isMarkupUnitDisable(row);

    // THEN
    expect(component.isMarkupUnitDisable).toBeTruthy();
  });

  it('should fetch mapped items on edit of future items in item grid', () => {
    // GIVEN
    const row =  ITEM_LEVEL_GRID_MODEL;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    component.contractPriceProfileId = 105;
    const itemDetails = {
      'itemAssignmentList': [
        {
          'itemDescription': 'Heinz Ketchup',
          'isItemSaved': true
        }
      ]
    }
    spyOn(furtheranceService, 'fetchMappedItemsForFurtherance').and.returnValue(Observable.of(itemDetails));

    // WHEN
    component.onEditFutureItem(row);
    fixture.detectChanges();

    // THEN
    expect(furtheranceService.fetchMappedItemsForFurtherance).toHaveBeenCalled();
    expect(component.itemAssignmentList.length).toBe(1);
    expect(component.itemAssignmentList[0].itemDescription).toBe('Heinz Ketchup');
  });

  it('should add an empty row to the itemAssignmentList on click of Add item button', () => {
    // GIVEN
    component.itemAssignmentList = [{ 'itemId': '123', 'itemDescription': 'Heinz Ketchup', 'isItemSaved': true,
    'itemExists': false, 'invalidItem': false }];

    // WHEN
    component.addAnotherItem();

    // THEN
    expect(component.itemAssignmentList.length).toBe(2);
    expect(component.itemAssignmentList[1].itemDescription).toBe('');
  });

  it('should delete(pop) a row from itemAssignmentList on click of clear icon', () => {
    // GIVEN
    component.itemAssignmentList = [{ 'itemId': '123', 'itemDescription': 'Heinz Ketchup', 'isItemSaved': true,
    'itemExists': false, 'invalidItem': false }];

    // WHEN
    component.onDeleteItemRow(0);

    // THEN
    expect(component.itemAssignmentList.length).toBe(1);

     // GIVEN
     component.itemAssignmentList = [{ 'itemId': '123', 'itemDescription': 'Heinz Ketchup', 'isItemSaved': false,
     'itemExists': false, 'invalidItem': false }];

     // WHEN
     component.onDeleteItemRow(0);

     // THEN
     expect(component.itemAssignmentList.length).toBe(0);
  });

  it('should build future item details for furtherance', () => {
    // GIVEN
    component.contractPriceProfileId = 105;
    component.currentItemDesc = 'Description';
    component.markupGridDetails = MARKUP_GRID_DETAILS
    component.itemAssignmentList = [new ItemAssignmentModel('12', 'Description', false, false, false)];

    // WHEN
    component.buildFutureItemDetailsForFurtherance();

    // THEN
    expect(component.saveItemDetailsForFurtherance.contractPriceProfileSeq).toBe(105);
    expect(component.saveItemDetailsForFurtherance.cppFurtheranceSeq).toBe(123);
    expect(component.saveItemDetailsForFurtherance.itemAssignmentList).toBeTruthy();

  });

  it('should invoke deleteItemAssignment in furtherance service and delete assigned item for furtherance mode', () => {
    // GIVEN
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
    component.futureItemDeleteIndex = 0;
    component.itemDetails = FUTURE_ITEM_VALUE;
    component.itemAssignmentList = ITEM_ASSIGNMENT_VALUE;
    mockTranslateService.translate.and.returnValue('Item assignment has been successfully deleted.');
    spyOn(furtheranceService, 'deleteItemAssignmentForFurtherance').and.returnValue(Observable.of([]));

    // WHEN
    component.deleteItemForFurtherance();

    // THEN
    expect(furtheranceService.deleteItemAssignmentForFurtherance).toHaveBeenCalled();
    expect(mockToasterService.showSuccess).toHaveBeenCalled();
  });

  it('should build future item details for furtherance', () => {
    // GIVEN
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
    component.contractPriceProfileId = 121;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    component.itemAssignmentList = ITEM_ASSIGNMENT_VALUE;

    // WHEN
    component.buildFutureItemDetailsForFurtherance();

    // THEN
    expect(component.saveItemDetailsForFurtherance.contractPriceProfileSeq).toBe(121);
    expect(component.saveItemDetailsForFurtherance.cppFurtheranceSeq).toBe(123);
  });

  it('should save item assignments for furtherance for new and valid item', () => {
    // GIVEN
    component.itemAssignmentList = ITEM_ASSIGNMENT_VALUE;
    mockTranslateService.translate.and.returnValue('All item assignments were successfully saved.');
    spyOn(furtheranceService, 'saveMappedItemsForFurtherance').and.returnValue(Observable.of(ITEM_SAVE_STATUS_MODEL));

    // WHEN
    component.saveItemAssignmentsForFurtherance(ITEM_DETAILS);

    // THEN
    expect(furtheranceService.saveMappedItemsForFurtherance).toHaveBeenCalled();
    expect(mockToasterService.showSuccess).toHaveBeenCalled();
    expect(component.itemAssignmentList).toBeTruthy();
  });

  it('should not save item assignments for furtherance for duplicate item', () => {
    // GIVEN
    const ITEM_SAVE_STATUS_MODEL_DUPLICATE = {
      'errorCode': 150,
      'errorMessage': 'Duplicate Item'
    }
    spyOn(furtheranceService, 'saveMappedItemsForFurtherance').and.returnValue(Observable.of(ITEM_SAVE_STATUS_MODEL_DUPLICATE));

    // WHEN
    component.saveItemAssignmentsForFurtherance(ITEM_DETAILS);

    // THEN
    expect(furtheranceService.saveMappedItemsForFurtherance).toHaveBeenCalled();
    expect(component.duplicateOnSaveFound).toBeTruthy();
    expect(component.duplicateList).toBe('Duplicate Item');
  });

  it('should disable save assignment button for invalid item, already existing items and other scenarios', () => {
    // GIVEN
    const ITEM_ASSIGNMENT_VALUE_INVALID: Array<ItemAssignmentModel> = [
      {
        'itemId': '12',
        'itemDescription': '12 - Heinz Ketchup',
        'isItemSaved': false,
        'invalidItem': true,
        'itemExists': false,
      }];
    component.itemAssignmentList = ITEM_ASSIGNMENT_VALUE_INVALID;

    // WHEN
    component.disableSaveAssignmentForMarkup();

    // THEN
    expect(component.disableSaveAssignment).toBeTruthy();

    // GIVEN
    const ITEM_ASSIGNMENT_VALUE_ALREADY_EXISTING: Array<ItemAssignmentModel> = [ {
      'itemId': '12',
      'itemDescription': '12 - Heinz Ketchup',
      'isItemSaved': false,
      'invalidItem': false,
      'itemExists': true,
    }];
    component.itemAssignmentList = ITEM_ASSIGNMENT_VALUE_ALREADY_EXISTING;

    // WHEN
    component.disableSaveAssignmentForMarkup();

    // THEN
    expect(component.disableSaveAssignment).toBeTruthy();

    // GIVEN
    const ITEM_ASSIGNMENT_VALUE_SAVED: Array<ItemAssignmentModel> = [ {
      'itemId': '12',
      'itemDescription': '12 - Heinz Ketchup',
      'isItemSaved': true,
      'invalidItem': false,
      'itemExists': false,
    }];
    component.itemAssignmentList = ITEM_ASSIGNMENT_VALUE_SAVED;

    // WHEN
    component.disableSaveAssignmentForMarkup();

    // THEN
    expect(component.disableSaveAssignment).toBeTruthy();

    // GIVEN
    const ITEM_ASSIGNMENT: Array<ItemAssignmentModel> = [ {
      'itemId': '12',
      'itemDescription': '',
      'isItemSaved': false,
      'invalidItem': false,
      'itemExists': false,
    }];
    component.itemAssignmentList = ITEM_ASSIGNMENT;

     // WHEN
     component.disableSaveAssignmentForMarkup();

     // THEN
     expect(component.disableSaveAssignment).toBeTruthy();

     // GIVEN
     const ITEM_ASSIGNMENT_FALSE: Array<ItemAssignmentModel> = [ {
      'itemId': '12',
      'itemDescription': '12-Hienz Ketchup',
      'isItemSaved': false,
      'invalidItem': false,
      'itemExists': false,
    }];
    component.itemAssignmentList = ITEM_ASSIGNMENT_FALSE;

     // WHEN
     component.disableSaveAssignmentForMarkup();

     // THEN
     expect(component.disableSaveAssignment).toBeFalsy();
  });
})
