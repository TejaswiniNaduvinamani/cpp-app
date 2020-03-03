import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DecimalPipe } from '@angular/common';
import { FormBuilder } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';
import { Renderer2 } from '@angular/core';

import { CONTRACT_TYPES } from './../../../shared/utils/app.constants';
import { ContractInformationDetails } from './../../contract-information/contract-information.model';
import { ItemLevelMarkupModel, ItemLevelDeleteModel } from './../item-level-markup/item-level-markup.model';
import { MarkupGridComponent } from './markup-grid.component';
import { MarkupGridModel, MarkupGridDetails } from './markup-grid.model';
import { MarkupService, TranslatorService, AuthorizationService } from './../../../shared';
import { SubgroupMarkupModel, SubgroupMarkupDeleteModel } from '../subgroup-markup/subgroup-markup.model';

describe('Markup-Grid-Component', () => {

  const CONTRACT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'), 'Draft',
    new Date('01/26/2020'), new Date('01/30/2020'), '123', 'edit', '2');

  const MARKUP_GRID_MODEL: MarkupGridModel = new MarkupGridModel('GROCERY', 1, '1', '%', 1, new Date('12/30/2020'),
  new Date('12/30/2021'), false, false);
  const MARKUP_GRID_MODEL2: MarkupGridModel = new MarkupGridModel('FROZEN', 2, '0.00', '$', 1, new Date('12/30/2020'),
  new Date('12/30/2021'), false, true);

  const SUBGROUP_MARKUP: SubgroupMarkupModel[] = [new SubgroupMarkupModel('12', 'My Subgroup',
     '2', '$', 1, new Date('2019/02/12'), new Date('2019/02/15'), false, false, false, false, false)];

  const DEFAULT_SUBGROUP: SubgroupMarkupModel = new SubgroupMarkupModel('123', 'My Subgroup', '1.00', '$', 1, new Date('2019/02/20'),
     new Date('2019/02/26'), false, false, false, false, false);

  const DEFAULT_ITEM_LEVEL_GRID: ItemLevelMarkupModel = new ItemLevelMarkupModel(
    false, '166720', '1', 'BLUEBERRY IQF 4-5# GFS', '10', '$', 1,
    new Date('12/30/2020'), new Date('12/30/2021'), false, false, false, false, false, false);

  const DEFAULT_ITEM_LEVEL_GRID2: ItemLevelMarkupModel = new ItemLevelMarkupModel(
     false, '', '', 'Heinz Ketchup', '0.00', '$', 1, new Date('12/30/2020'), new Date('12/30/2021'),
     false, false, false, false, false, false);

  const MARKUP_GRID_DETAILS: MarkupGridDetails = new MarkupGridDetails('151', 31, 123, 'qwerty', [MARKUP_GRID_MODEL,
      MARKUP_GRID_MODEL2], [DEFAULT_SUBGROUP], [DEFAULT_ITEM_LEVEL_GRID], true, 1);

  const ITEM_LEVEL_DELETE_MODEL: ItemLevelDeleteModel = new ItemLevelDeleteModel('166720', 'BLUEBERRY IQF 4-5# GFS', 1, '150');
  const SUBGROUP_DELETE_MODEL: SubgroupMarkupDeleteModel = new SubgroupMarkupDeleteModel('20213', 'FLOOR MATS', 1, '150', 1);

  let component: MarkupGridComponent;
  let fixture: ComponentFixture<MarkupGridComponent>;
  let markupService: MarkupService;

  const subgroupMarkupValues: SubgroupMarkupModel[] =  [
    {
      subgroupId: '125',
      subgroupDesc: 'My Subgroup',
      markup: '1.00',
      unit: '$',
      markupType: 1,
      effectiveDate: new Date('12/12/2016'),
      expirationDate: new Date('15/12/2016'),
      isInvalidMarkupCurrency: false,
      invalidMarkup: false,
      invalid: false,
      isSubgroupSaved: false,
      isSubgroupAlreadyExist: false
    }
  ];

  const itemLevelMarkupValues: ItemLevelMarkupModel[] = [
    {
      'noItemId': false,
      'itemId': '678',
      'itemDesc': 'My Future Item',
      'stockingCode': '0',
      'markup': '1.00',
      'unit': '$',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'invalid': false,
      'inactive': false,
      'isItemSaved': true,
      'isInvalidMarkupCurrency': false,
      'invalidMarkup': false,
      'isItemAlreadyExist': false
    },
    {
      'noItemId': true,
      'itemId': '',
      'itemDesc': 'My Future Item-4',
      'stockingCode': '0',
      'markup': '1.00',
      'unit': '$',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'invalid': false,
      'inactive': false,
      'isItemSaved': true,
      'isInvalidMarkupCurrency': false,
      'invalidMarkup': false,
      'isItemAlreadyExist': false
    }
  ]
  beforeEach(() => {

    const mockTranslateService  = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MarkupGridComponent],
      providers: [
        DecimalPipe,
        Renderer2,
        FormBuilder,
        MarkupService,
        AuthorizationService,
        { provide: TranslatorService, useValue: mockTranslateService },
      ]
    }).overrideTemplate(MarkupGridComponent, '');

    fixture = TestBed.createComponent(MarkupGridComponent);
    component = fixture.componentInstance;
    markupService = TestBed.get(MarkupService);
    component.itemLevelMarkupList = [DEFAULT_ITEM_LEVEL_GRID, DEFAULT_ITEM_LEVEL_GRID2];
    component.subgroupMarkupList = [DEFAULT_SUBGROUP];
    spyOn(component.itemLevelMarkupList, 'map').and.returnValue(['166720', 'BLUEBERRY IQF 4-5# GFS']);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create the markup-grid component', () => {
    // THEN
    expect(component).toBeTruthy();
  });

  it('should create a form group with all controls', () => {
    // WHEN
    component.loadForm();

    // THEN
    expect(component.markupGridForm.contains('expireLowerToggle')).toBeTruthy();
  });

  it('should set isMarkupSaved to false on edit of markup value', () => {

    const row = MARKUP_GRID_MODEL;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': '10' } };

    // WHEN
    component.onMarkupKeyUp(event, row);

    // THEN
    expect(component.markupGridDetails.isMarkupSaved).toBeFalsy();
  });

  it('should emit event on change of markup-type', () => {

    // GIVEN
    const row = MARKUP_GRID_MODEL;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': '10' } };
    spyOn(component.editMarkupGrid, 'emit');

    // WHEN
    component.onMarkupTypeChange(event, row);

    // THEN
    expect(component.markupGridDetails.isMarkupSaved).toBeFalsy();
    expect(component.editMarkupGrid.emit).toHaveBeenCalled();
  });

  it('should emit event on change of unit', () => {
    // GIVEN
    const row = {
      'markup': 0.06,
      'unit': '$',
      'markupType': 1
    };
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': '%' } };
    spyOn(component.editMarkupGrid, 'emit');

    // WHEN
    component.onUnitChange(event, row);

    // THEN
    expect(component.editMarkupGrid.emit).toHaveBeenCalled();
  });

  it('should set error flag when non-numeric markup value is entered', () => {
    // GIVEN
    component.rows = [MARKUP_GRID_MODEL];
    const row = MARKUP_GRID_MODEL;
    const event = { 'target': { 'value': 'abcd' } };
    fixture.detectChanges();
    spyOn(component._renderer, 'setAttribute');
    spyOn(component._renderer, 'addClass');
    // WHEN
    component.onblurMarkup(event, row);

    // THEN
    expect(row.invalidMarkup).toBeFalsy();
    expect(row.isInvalidCurrency).toBeTruthy();
  });

  it('should set error flag when markup value greater than 99 is entered', () => {
    // GIVEN
    const row = MARKUP_GRID_MODEL;
    component.rows = [MARKUP_GRID_MODEL];
    const event = { 'target': { 'value': '1000' } };
    fixture.detectChanges();
    spyOn(component._renderer, 'setAttribute');
    spyOn(component._renderer, 'addClass');

    // WHEN
    component.onblurMarkup(event, row);

    // THEN
    expect(row.invalidMarkup).toBeTruthy();
    expect(row.isInvalidCurrency).toBeFalsy();
  });

  it('should check rounding-off of markup value', () => {
    // GIVEN
    const row = MARKUP_GRID_MODEL;
    component.rows = [MARKUP_GRID_MODEL];
    const event = { 'target': { 'value': '10' } };
    fixture.detectChanges();
    spyOn(component._renderer, 'setAttribute');
    spyOn(component._renderer, 'removeClass');

    // WHEN
    component.onblurMarkup(event, row);

    // THEN
    expect(row.invalidMarkup).toBeFalsy();
    expect(row.isInvalidCurrency).toBeFalsy();
  });

  it('should round-off markup value and set error flag when 0 is entered', () => {
    // GIVEN
    const row = MARKUP_GRID_MODEL;
    const event = { 'target': { 'value': '0' } };
    component.rows = [MARKUP_GRID_MODEL];
    fixture.detectChanges();
    spyOn(component._renderer, 'addClass');

    // WHEN
    component.onblurMarkup(event, row);

    // THEN
    expect(row.isInvalidCurrency).toBeFalsy();
    expect(row.invalidMarkup).toBeTruthy();
    expect(row.markup).toBe('0.00');
  });

  it('should check if markup < min markup', () => {
    // GIVEN
    const row = {
      'markup': 0.00,
      'unit': '$',
    };

    // WHEN
    component.validateData(row);

    // THEN
    expect(component.validateData(row)).toBeTruthy();
  });

  it('should check if markup is within markup range', () => {
    // GIVEN
    const row = {
      'markup': 0.06,
      'unit': '$',
    };

    spyOn(component, 'validateData');

    // WHEN
    component.validateData(row);

    // THEN
    expect(component.validateData).toHaveBeenCalled()
  });

  it('should check if component emits event on save of markup grid', () => {
    // GIVEN
    component.rows = [MARKUP_GRID_MODEL];
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': '1000' } };
    spyOn(component.saveMarkupGridDetails, 'emit');
    spyOn(component, 'checkValidity').and.returnValue(false);
    fixture.detectChanges();

    // WHEN
    component.onSubmit(event);

    // THEN
    expect(component.saveMarkupGridDetails.emit).toHaveBeenCalled();
  });

  it('should check if component should not emit event if mandatory fields are missing', () => {
    // GIVEN
    component.rows = [MARKUP_GRID_MODEL];
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': '1000' } };
    fixture.detectChanges();
    spyOn(component.saveMarkupGridDetails, 'emit');

    // WHEN
    component.onSubmit(event);

    // THEN
    expect(component.saveMarkupGridDetails.emit).toHaveBeenCalledTimes(0);
  });

  it('should check if save markup button is not grayed-out', () => {
    // GIVEN
    component.rows = [MARKUP_GRID_MODEL, MARKUP_GRID_MODEL2];
    component.itemLevelMarkupList = [DEFAULT_ITEM_LEVEL_GRID];
    component.subgroupMarkupList = SUBGROUP_MARKUP;
    component.itemIdValidError = false;
    component.itemIdActiveError = false;
    component.itemIdDuplicateError = false;
    component.itemExitsInDBError = false;
    component.subgroupIdValidError = false;
    component.subgroupIdDuplicateError = false;

    spyOn(component.rows, 'some').and.returnValue(false);
    spyOn(component.itemLevelMarkupList, 'some').and.returnValue(false);
    spyOn(component.subgroupMarkupList, 'some').and.returnValue(false);

    // WHEN
    component.isSaveGrayedOut();

    // THEN
    expect(component.isSaveGrayed).not.toBeTruthy();
  });

  it('should check for validations', () => {
    // GIVEN
    component.rows = [MARKUP_GRID_MODEL, MARKUP_GRID_MODEL2];
    fixture.detectChanges();

    // WHEN
    component.checkMarkupValidations();

    // THEN
    expect(component.itemIdValidError).toBeFalsy();
    expect(component.itemIdActiveError).toBeFalsy();
    expect(component.itemIdDuplicateError).toBeFalsy();
    expect(component.itemExitsInDBError).toBeFalsy();
    expect(component.subgroupIdValidError).toBeFalsy();
    expect(component.subgroupIdDuplicateError).toBeFalsy();
  });

  it('should check if event is emitted on edit of Item level markup', () => {
    // GIVEN
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editMarkupGrid, 'emit');

    // WHEN
    component.onEditItemLevelGrid(component.markupGridDetails.gfsCustomerId);

    // THEN
    expect(component.editMarkupGrid.emit).toHaveBeenCalled();
  });

  it('should check if event is emitted on edit of subgroup markup', () => {
    // GIVEN
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editMarkupGrid, 'emit');

    // WHEN
    component.onEditSubgroupGrid(component.markupGridDetails.gfsCustomerId);

    // THEN
    expect(component.editMarkupGrid.emit).toHaveBeenCalled();
  });

  it('should check if event is emitted on edit of markup unit', () => {
    // GIVEN
    spyOn(component.onUnitChangeVal, 'emit');

    // WHEN
    component.onItemLevelUnitChange('$');

    // THEN
    expect(component.onUnitChangeVal.emit).toHaveBeenCalled();

    // WHEN
    component.onSubgroupUnitChange('$');

    // THEN
    expect(component.onUnitChangeVal.emit).toHaveBeenCalled();
  });

  it('should copy values from first row in case unit is %', () => {
    // GIVEN
    const row = {
      'markup': 0.06,
      'unit': '%',
    };
    component.rows = [MARKUP_GRID_MODEL, MARKUP_GRID_MODEL2];
    component.exceptionIndex = 0;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': '10' } };
    fixture.detectChanges();
    spyOn(component._renderer, 'setAttribute');
    spyOn(component._renderer, 'removeClass');

    // WHEN
    component.copyRow(event, row);

    // THEN
    expect(component._renderer.setAttribute).toHaveBeenCalled();
  });

  it('should copy values from first row in case unit is $', () => {
    // GIVEN
    const row = {
      'markup': 0.06,
      'unit': '$',
    };
    component.rows = [MARKUP_GRID_MODEL, MARKUP_GRID_MODEL2];
    component.exceptionIndex = 0;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': '10' } };
    fixture.detectChanges();
    spyOn(component._renderer, 'removeAttribute');
    spyOn(component._renderer, 'removeClass');

    // WHEN
    component.copyRow(event, row);

    // THEN
    expect(component._renderer.removeAttribute).toHaveBeenCalled();
  });

  it('should copy values from first row and set error flag when invalid markup is entered', () => {
    // GIVEN
    const row = {
      'markup': 1234,
      'unit': '$',
      'isInvalidCurrency': false,
      'invalidMarkup': true
    };
    component.rows = [MARKUP_GRID_MODEL, MARKUP_GRID_MODEL2];
    component.exceptionIndex = 0;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    const event = { 'target': { 'value': 1234 } };
    fixture.detectChanges();
    spyOn(component._renderer, 'removeAttribute');
    spyOn(component._renderer, 'addClass');

    // WHEN
    component.copyRow(event, row);

    // THEN
    expect(component._renderer.removeAttribute).toHaveBeenCalled();
    expect(component._renderer.addClass).toHaveBeenCalled();
  });

  it('should fetch default item level markup details', () => {
    // GIVEN
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editMarkupGrid, 'emit');
    spyOn(markupService, 'fetchItemLevelDefaults').and.returnValue(Observable.of([DEFAULT_ITEM_LEVEL_GRID]));

    // WHEN
    component.onAddItemLevelMarkup();

    // THEN
    expect(markupService.fetchItemLevelDefaults).toHaveBeenCalled();
    expect(component.itemLevelMarkupList.length).toBeTruthy();
    expect(component.editMarkupGrid.emit).toHaveBeenCalled();
  });

  it('should fetch defalut item level markup details when all product type unit is %', () => {
    // GIVEN
    component.isMarkupUnitDisabled = true;
    component.isFurtheranceMode = true;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editMarkupGrid, 'emit');
    const productMarkupList: MarkupGridModel[] = [{
      'productType': 'GROCERY',
      'itemPriceId': 1,
      'markup': '1.00',
      'unit': '%',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'isInvalidCurrency': false,
      'invalidMarkup': false
    },
    {
      'productType': 'SEAFOOD',
      'itemPriceId': 4,
      'markup': '4.00',
      'unit': '%',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'isInvalidCurrency': false,
      'invalidMarkup': false
    },
    {
      'productType': 'DAIRY',
      'itemPriceId': 6,
      'markup': '6.00',
      'unit': '%',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'isInvalidCurrency': false,
      'invalidMarkup': false
    }]

    component.markupGridDetails = {
        'gfsCustomerId': '150',
        'markupName': 'Contract',
        'gfsCustomerType': 31,
        'isMarkupSaved': true,
        'contractPriceProfileSeq': 1234,
        'productMarkupList': productMarkupList,
        'itemLevelMarkupList': itemLevelMarkupValues,
        'subgroupMarkupList': subgroupMarkupValues,
        'markupGridIndex': 12,
        'cppFurtheranceSeq': 345
      }

    const ITEM_LEVEL_GRID: ItemLevelMarkupModel = {
      'noItemId': false,
      'itemId': '678',
      'itemDesc': 'My Future Item',
      'stockingCode': '0',
      'markup': '1.00',
      'unit': '$',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'invalid': false,
      'inactive': false,
      'isItemSaved': true,
      'isInvalidMarkupCurrency': false,
      'invalidMarkup': false,
      'isItemAlreadyExist': false
    }
    spyOn(markupService, 'fetchItemLevelDefaults').and.returnValue(Observable.of([ITEM_LEVEL_GRID]));

    // WHEN
    component.onAddItemLevelMarkup();

    // THEN
    expect(markupService.fetchItemLevelDefaults).toHaveBeenCalled();
    expect(component.itemLevelMarkupList.length).toBeTruthy();
    expect(component.editMarkupGrid.emit).toHaveBeenCalled();
    expect(component.itemLevelMarkupList[component.itemLevelMarkupList.length - 1].markupType).toBe(1);
    expect(component.itemLevelMarkupList[component.itemLevelMarkupList.length - 1].unit).toBe('%');
  });

  it('should fetch defalut item level markup details when atleast one product type unit is % and markup based on sell is false', () => {
    // GIVEN
    component.showMarkupOnSell = true;
    component.isMarkupUnitDisabled = false;
    component.isFurtheranceMode = true;
    component.markupOnSellVal = false;
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editMarkupGrid, 'emit');
    const productMarkupList: MarkupGridModel[] = [{
      'productType': 'GROCERY',
      'itemPriceId': 1,
      'markup': '1.00',
      'unit': '%',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'isInvalidCurrency': false,
      'invalidMarkup': false
    },
    {
      'productType': 'SEAFOOD',
      'itemPriceId': 4,
      'markup': '4.00',
      'unit': '$',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'isInvalidCurrency': false,
      'invalidMarkup': false
    },
    {
      'productType': 'DAIRY',
      'itemPriceId': 6,
      'markup': '6.00',
      'unit': '%',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'isInvalidCurrency': false,
      'invalidMarkup': false
    }]

    component.markupGridDetails = {
        'gfsCustomerId': '150',
        'markupName': 'Contract',
        'gfsCustomerType': 31,
        'isMarkupSaved': true,
        'contractPriceProfileSeq': 1234,
        'productMarkupList': productMarkupList,
        'itemLevelMarkupList': itemLevelMarkupValues,
        'subgroupMarkupList': subgroupMarkupValues,
        'markupGridIndex': 12,
        'cppFurtheranceSeq': 345
      }

    const ITEM_LEVEL_GRID: ItemLevelMarkupModel = {
      'noItemId': false,
      'itemId': '678',
      'itemDesc': 'My Future Item',
      'stockingCode': '0',
      'markup': '1.00',
      'unit': '$',
      'markupType': 1,
      'effectiveDate': new Date('12/12/2020'),
      'expirationDate': new Date('15/12/2020'),
      'invalid': false,
      'inactive': false,
      'isItemSaved': true,
      'isInvalidMarkupCurrency': false,
      'invalidMarkup': false,
      'isItemAlreadyExist': false
    }
    spyOn(markupService, 'fetchItemLevelDefaults').and.returnValue(Observable.of([ITEM_LEVEL_GRID]));

    // WHEN
    component.onAddItemLevelMarkup();

    // THEN
    expect(markupService.fetchItemLevelDefaults).toHaveBeenCalled();
    expect(component.itemLevelMarkupList.length).toBeTruthy();
    expect(component.editMarkupGrid.emit).toHaveBeenCalled();
    expect(component.itemLevelMarkupList[component.itemLevelMarkupList.length - 1].markupType).toBe(1);
    expect(component.itemLevelMarkupList[component.itemLevelMarkupList.length - 1].unit).toBe('%');
  });

  it('should fetch defalut item level markup details when all product type unit is $ and markup based on sell is false', () => {
     // GIVEN
     component.showMarkupOnSell = false;
     component.isMarkupUnitDisabled = false;
     component.isFurtheranceMode = true;
     component.markupOnSellVal = false;
     component.markupGridDetails = MARKUP_GRID_DETAILS;
     spyOn(component.editMarkupGrid, 'emit');
     const productMarkupList: MarkupGridModel[] = [{
       'productType': 'GROCERY',
       'itemPriceId': 1,
       'markup': '1.00',
       'unit': '$',
       'markupType': 2,
       'effectiveDate': new Date('12/12/2020'),
       'expirationDate': new Date('15/12/2020'),
       'isInvalidCurrency': false,
       'invalidMarkup': false
     },
     {
       'productType': 'SEAFOOD',
       'itemPriceId': 4,
       'markup': '4.00',
       'unit': '$',
       'markupType': 2,
       'effectiveDate': new Date('12/12/2020'),
       'expirationDate': new Date('15/12/2020'),
       'isInvalidCurrency': false,
       'invalidMarkup': false
     },
     {
       'productType': 'DAIRY',
       'itemPriceId': 6,
       'markup': '6.00',
       'unit': '$',
       'markupType': 2,
       'effectiveDate': new Date('12/12/2020'),
       'expirationDate': new Date('15/12/2020'),
       'isInvalidCurrency': false,
       'invalidMarkup': false
     }]

     component.markupGridDetails = {
         'gfsCustomerId': '150',
         'markupName': 'Contract',
         'gfsCustomerType': 31,
         'isMarkupSaved': true,
         'contractPriceProfileSeq': 1234,
         'productMarkupList': productMarkupList,
         'itemLevelMarkupList': itemLevelMarkupValues,
         'subgroupMarkupList': subgroupMarkupValues,
         'markupGridIndex': 12,
         'cppFurtheranceSeq': 345
       }

     const ITEM_LEVEL_GRID: ItemLevelMarkupModel = {
       'noItemId': false,
       'itemId': '678',
       'itemDesc': 'My Future Item',
       'stockingCode': '0',
       'markup': '1.00',
       'unit': '$',
       'markupType': 2,
       'effectiveDate': new Date('12/12/2020'),
       'expirationDate': new Date('15/12/2020'),
       'invalid': false,
       'inactive': false,
       'isItemSaved': true,
       'isInvalidMarkupCurrency': false,
       'invalidMarkup': false,
       'isItemAlreadyExist': false
     }
     spyOn(markupService, 'fetchItemLevelDefaults').and.returnValue(Observable.of([ITEM_LEVEL_GRID]));

     // WHEN
     component.onAddItemLevelMarkup();
     component.itemLevelMarkupList.push(ITEM_LEVEL_GRID);

     // THEN
     expect(markupService.fetchItemLevelDefaults).toHaveBeenCalled();
     expect(component.itemLevelMarkupList.length).toBeTruthy();
     expect(component.editMarkupGrid.emit).toHaveBeenCalled();
     expect(component.itemLevelMarkupList[component.itemLevelMarkupList.length - 1].unit).toBe('$');
  });


  it('should fetch default subgroup markup details', () => {
    // GIVEN
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editMarkupGrid, 'emit');
    spyOn(markupService, 'fetchDefaultSubgroupMarkup').and.returnValue(Observable.of([DEFAULT_SUBGROUP]));

    // WHEN
    component.onAddSubgroupMarkup();

    // THEN
    expect(markupService.fetchDefaultSubgroupMarkup).toHaveBeenCalled();
    expect(component.subgroupMarkupList.length).toBeTruthy();
    expect(component.editMarkupGrid.emit).toHaveBeenCalled();
  });

  it('should set gfsCustomerId and emit event on deletion of item level markup', () => {
    // GIVEN
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.itemLevelDeleteInfo, 'emit');

    // WHEN
    component.onItemLevelDeleteInfo(ITEM_LEVEL_DELETE_MODEL);

    // THEN
    expect(component.itemLevelDeleteInfo.emit).toHaveBeenCalled();
  });

  it('should set gfsCustomerId and emit event on deletion of subgroup markup', () => {
    // GIVEN
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.subgroupDeleteInfo, 'emit');

    // WHEN
    component.onSubgroupDeleteInfo(SUBGROUP_DELETE_MODEL);

    // THEN
    expect(component.subgroupDeleteInfo.emit).toHaveBeenCalled();
    expect(SUBGROUP_DELETE_MODEL.gfsCustomerId).toBe(component.markupGridDetails.gfsCustomerId);
    expect(SUBGROUP_DELETE_MODEL.gfsCustomerType).toBe(component.markupGridDetails.gfsCustomerType);
  });
})
