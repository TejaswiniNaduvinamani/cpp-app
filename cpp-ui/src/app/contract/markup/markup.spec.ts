import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';
import { Router, ActivatedRoute } from '@angular/router';
import { Renderer2, ElementRef } from '@angular/core';

import { ActivatedRouteStub } from './../../../../test/unit-testing/mock/activated-route-stub';
import { CONTRACT_TYPES } from './../../shared/utils/app.constants';
import { ItemLevelMarkupModel, ItemLevelDeleteModel } from './item-level-markup/item-level-markup.model';
import { MarkupComponent } from './markup.component';
import { MarkupGridDetails, MarkupGridModel} from 'app/contract';
import { MarkupService, StepperService, ToasterService, TranslatorService,
   AuthorizationService, AuthorizationDetails, FurtheranceService } from './../../shared';
import { RouterStub } from './../../../../test/unit-testing/mock/router-stub';
import { SubgroupMarkupModel, SubgroupMarkupDeleteModel } from './subgroup-markup/subgroup-markup.model';

describe('Markup-Component', () => {

  const ITEM_LEVEL_MARKUP: ItemLevelMarkupModel[] = [new ItemLevelMarkupModel(false, '12', '2',
  'Item', '2', '$', 12, new Date('2019/02/12'), new Date('2019/02/15'), false, false, false, false, false, false)];

  const SUBGROUP_MARKUP: SubgroupMarkupModel[] = [new SubgroupMarkupModel('151', 'My Subgroup',
  '2', '$', 1, new Date('2019/02/12'), new Date('2019/02/15'), false, false, false, false, false)];

  const SUBGROUP_MARKUP_PERCENT: SubgroupMarkupModel[] = [new SubgroupMarkupModel('12', 'My Subgroup',
  '2', '%', 1, new Date('2019/02/12'), new Date('2019/02/15'), false, false, false, false, false)];

  const MARKUP_GRID_ROW: MarkupGridModel = new MarkupGridModel('GROCERY', 1, '1.00', '$', 1, new Date('12/30/2017'),
  new Date('12/07/2019'), false, false);

  const MARKUP_ROW: MarkupGridModel = new MarkupGridModel('GROCERY', 1, '7.00', '%', 1,
  new Date('12/30/2017'), new Date('12/07/2019'), true, true);

  const MARKUP_GRID_ROW1 = [{
    'isCompleted': false,
    'markupId': 151,
    'markupName': 'qwerty',
    'itemLevelMarkupList': ITEM_LEVEL_MARKUP,
    'subgroupMarkupList': SUBGROUP_MARKUP,
    'productMarkupList': [
      {
        'productType': 'GROCERY',
        'itemPriceId': 1,
        'markup': '1.00',
        'unit': '$',
        'markupType': 1,
        'effectiveDate': new Date('12/30/2017'),
        'expirationDate': new Date('12/07/2019'),
        'isInvalidCurrency': false,
        'invalidMarkup': false
      }
    ]
  }];

  const MARKUP_GRID_ROW2 = [{
    'isCompleted': false,
    'markupId': 151,
    'markupName': 'qwerty',
    'subgroupMarkupList': SUBGROUP_MARKUP_PERCENT,
    'itemLevelMarkupList': [
      {
        'noItemId': false,
        'itemId': '12',
        'stockingCode': '2',
        'itemDesc': 'Item',
        'markup': '22',
        'unit': '%',
        'markupType': 12,
        'expirationDate': new Date('2019/02/12'),
        'effectiveDate': new Date('2019/02/15'),
        'inactive': false,
        'invalid': false,
        'isItemSaved': false,
        'isInvalidMarkupCurrency': false,
        'invalidMarkup': false
      }
    ],
    'productMarkupList': [
      {
        'productType': 'GROCERY',
        'itemPriceId': 1,
        'markup': '7.00',
        'unit': '%',
        'markupType': 1,
        'effectiveDate': new Date('12/30/2017'),
        'expirationDate': new Date('12/07/2019'),
        'isInvalidCurrency': true,
        'invalidMarkup': false
      }
    ]
  }];

  const MARKUP_GRID_DETAILS = {
      'isMarkupSaved': true,
      'gfsCustomerId': '151',
      'gfsCustomerType': 31,
      'contractPriceProfileSeq': 123,
      'markupName': 'qwerty',
      'itemLevelMarkupList': ITEM_LEVEL_MARKUP,
      'subgroupMarkupList': SUBGROUP_MARKUP,
      'markupGridIndex': 0,
      'productMarkupList': [
        {
          'productType': 'GROCERY',
          'itemPriceId': 1,
          'markup': '1.00',
          'unit': '$',
          'markupType': 1,
          'effectiveDate': new Date('12/30/2017'),
          'expirationDate': new Date('12/07/2019'),
          'isInvalidCurrency': false,
        'invalidMarkup': true
        },
        {
          'productType': 'FROZEN',
          'itemPriceId': 2,
          'markup': '2.00',
          'unit': '%',
          'markupType': 1,
          'effectiveDate': new Date('12/30/2017'),
          'expirationDate': new Date('12/07/2019'),
          'isInvalidCurrency': false,
        'invalidMarkup': false
        }
      ]
    };

    const MARKUP_GRID_DETAILS_1 = {
      'isMarkupSaved': true,
      'gfsCustomerId': '151',
      'gfsCustomerType': 31,
      'contractPriceProfileSeq': 123,
      'markupName': 'qwerty',
      'itemLevelMarkupList': ITEM_LEVEL_MARKUP,
      'subgroupMarkupList': SUBGROUP_MARKUP,
      'markupGridIndex': 0,
      'productMarkupList': [
        {
          'productType': 'GROCERY',
          'itemPriceId': 1,
          'markup': '1.00',
          'unit': '$',
          'markupType': 1,
          'effectiveDate': new Date('12/30/2017'),
          'expirationDate': new Date('12/07/2019'),
          'isInvalidCurrency': false,
        'invalidMarkup': false
        },
        {
          'productType': 'FROZEN',
          'itemPriceId': 2,
          'markup': '2.00',
          'unit': '%',
          'markupType': 1,
          'effectiveDate': new Date('12/30/2017'),
          'expirationDate': new Date('12/07/2019'),
          'isInvalidCurrency': false,
        'invalidMarkup': false
        }
      ]
    };

    const MARKUP_GRID_DETAILS_2 = {
      'markupGridIndex': 0,
      'isMarkupSaved': false,
      'gfsCustomerId': '151',
      'gfsCustomerType': 31,
      'contractPriceProfileSeq': 123,
      'markupName': 'qwerty',
      'itemLevelMarkupList': ITEM_LEVEL_MARKUP,
      'subgroupMarkupList': SUBGROUP_MARKUP,
      'productMarkupList': [
        {
          'productType': 'GROCERY',
          'itemPriceId': 1,
          'markup': '1.00',
          'unit': '$',
          'markupType': 1,
          'effectiveDate': new Date('12/30/2017'),
          'expirationDate': new Date('12/07/2019'),
          'isInvalidCurrency': false,
          'invalidMarkup': true
        },
        {
          'productType': 'FROZEN',
          'itemPriceId': 2,
          'markup': '2.00',
          'unit': '$',
          'markupType': 1,
          'effectiveDate': new Date('12/30/2017'),
          'expirationDate': new Date('12/07/2019'),
          'isInvalidCurrency': false,
          'invalidMarkup': false
        }
      ]
    };

  const AUTH_DETAILS: AuthorizationDetails = new AuthorizationDetails(true, true, true, true, 'draft')

  const ITEM_LEVEL_DELETE_MODEL: ItemLevelDeleteModel = new ItemLevelDeleteModel('166720', 'BLUEBERRY IQF 4-5# GFS', 1, '150');

  const SUBGROUP_DELETE_MODEL: SubgroupMarkupDeleteModel = new SubgroupMarkupDeleteModel('20213', 'FLOOR MATS', 1, '150', 1);

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

  let component: MarkupComponent;
  let fixture: ComponentFixture<MarkupComponent>;
  let markupService: MarkupService;
  let stepperService: StepperService;
  let authService: AuthorizationService;
  let router: RouterStub;
  let route: ActivatedRouteStub;
  let furtheranceService: FurtheranceService;
  let mockTranslateService;
  let mockToasterService;

  beforeEach(() => {
    mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MarkupComponent],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        {provide: TranslatorService, useValue: mockTranslateService},
        {provide: ToasterService, useValue: mockToasterService},
        Renderer2,
        FormBuilder,
        StepperService,
        MarkupService,
        AuthorizationService,
        FurtheranceService
      ]
    }).overrideTemplate(MarkupComponent, '');

    fixture = TestBed.createComponent(MarkupComponent);
    component = fixture.componentInstance;
    markupService = TestBed.get(MarkupService);
    stepperService = TestBed.get(StepperService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
    authService = TestBed.get(AuthorizationService);
    furtheranceService = TestBed.get(FurtheranceService);

    sessionStorage.setItem('contractInfo', '{"isFurtheranceMode":true}');
  });


  it('should create the markup component', () => {
    // THEN
   expect(component).toBeTruthy();
  });

  it('should create a form group with all controls', () => {
    // WHEN
    fixture.detectChanges();

    // THEN
    expect(component.markupForm.contains('markupOnSellToggle')).toBeTruthy();
    expect(component.markupModalForm.contains('markupStructure')).toBeTruthy();
    expect(component.editmarkupModalForm.contains('editMarkupStructure')).toBeTruthy();
  });

  it('should fetch markup grid details', () => {
    // GIVEN
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of([]));
    spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(markupService, 'fetchMarkupGridDetails').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));

    // WHEN
    fixture.detectChanges();

      // THEN
    expect(component.markupAccordions.length).toBe(1);
  });

  it('should fetch markup grid details when furtherance is editable', () => {
    // GIVEN
    component.cppFurtheranceSeq = 123;
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of([]));
    spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(markupService, 'fetchMarkupGridDetails').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));
    spyOn(furtheranceService, 'canEditFurtherance' ).and.returnValue(Observable.of({canEditFurtherance: true}));

    // WHEN
    fixture.detectChanges();

      // THEN
    expect(component.markupAccordions.length).toBe(1);
    expect(component.canEditFurtherance).toBeTruthy();
    expect(furtheranceService.canEditFurtherance).toHaveBeenCalledWith(component.cppFurtheranceSeq);
  });

  it('should save markup grid details', () => {
    // GIVEN
    fixture.detectChanges();
    component.isFurtheranceMode = false;
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    component.copyMarkups.push({markupName: 'Contract', gfsCustomerId: '1'});
    component.contractPriceProfileId = 121;
    spyOn(markupService, 'saveMarkupGridDetails').and.returnValue(Observable.of(MARKUP_GRID_DETAILS));

    // WHEN
    component.onSaveMarkupGridDetails(MARKUP_GRID_DETAILS);

    // THEN
    expect(component.copyMarkups.length).toBeTruthy();
  });

  it('should save markup grid details for furtherance mode', () => {
    // GIVEN
    fixture.detectChanges();
    component.isFurtheranceMode = true;
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    component.contractPriceProfileId = 121;
    spyOn(furtheranceService, 'saveMarkupGridForFurtherance').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));

    // WHEN
    component.onSaveMarkupGridDetails(MARKUP_GRID_DETAILS);

      // THEN
    expect(furtheranceService.saveMarkupGridForFurtherance).toHaveBeenCalled();
  });

  it('should save markup on sell value', () => {
    // GIVEN
    fixture.detectChanges();
    component.isFurtheranceMode = false;
    component.contractPriceProfileId = 121;
    component.effectiveDate = new Date('12/30/2017');
    component.expirationDate = new Date('12/30/2018');
    component.markupOnSellVal = true;
    spyOn(markupService, 'saveMarkupIndicators').and.returnValue(Observable.of(MARKUP_GRID_DETAILS));
    let spy = spyOn(router, 'navigate');

    // WHEN
    component.onSubmit();

      // THEN
    expect(component.showSpinner).toBeFalsy();
    expect(spy).toHaveBeenCalledWith((['/splitcasefee']), {relativeTo: route});
  });

  it('should navigate to review page when contract type is IFS', () => {

    // GIVEN
    fixture.detectChanges();
    component.selectedContractType = CONTRACT_TYPES.IFS;
    let spy = spyOn(router, 'navigate');

   // WHEN
    component.navigateToUrl();

    // THEN
    expect(spy).toHaveBeenCalledWith((['/review']), {relativeTo: route});
 });

 it('should add a new exception markup ', () => {
    // GIVEN
    fixture.detectChanges();
    component.markupModalForm.get('markupStructure').setValue('New Exception');
    component.contractPriceProfileId = 121;
    component.effectiveDate = new Date('12/30/2017');
    component.expirationDate = new Date('12/30/2018');
    spyOn(markupService, 'addExceptionDetails').and.returnValue(Observable.of(MARKUP_GRID_DETAILS));

    // WHEN
    component.onAddException();

    // THEN
    expect(markupService.addExceptionDetails).toHaveBeenCalled();
    expect(component.markupAccordions.length).toBeTruthy();
    expect(component.isNextDisabled).toBeTruthy();
  });

  it('should cancel a new exception markup ', () => {
    // GIVEN
    fixture.detectChanges();
    component.markupModalForm.get('markupStructure').setValue('New Exception');
    component.editmarkupModalForm.get('editMarkupStructure').setValue('Edit Exception');

    // WHEN
    component.onCancelException();

    // THEN
    expect(component.markupModalForm.valid).toBeFalsy();
    expect(component.editmarkupModalForm.valid).toBeFalsy();
  });

 it('should rename an exception markup ', () => {
    // GIVEN
    fixture.detectChanges();
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    component.editModalInput = new ElementRef({
      getAttribute: () => {
        return 151;
      }
    });
    component.editmarkupModalForm.get('editMarkupStructure').setValue('RenamedException');
    component.contractPriceProfileId = 121;
    spyOn(markupService, 'renameMarkupException').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));

    // WHEN
    component.onRenameMarkupException();

    // THEN
    expect(markupService.renameMarkupException).toHaveBeenCalled();
    expect(component.markupAccordions[0].markupName).toBe('RenamedException');
  });

  it('should delete an exception markup ', () => {
    // GIVEN
    fixture.detectChanges();
    mockTranslateService.translate.and.returnValue('has been deleted');
    component.markupAccordions = [MARKUP_GRID_DETAILS];
    component.editModalInput = new ElementRef({
      getAttribute: () => {
        return 151;
      }
    });
    component.contractPriceProfileId = 121;
    spyOn(markupService, 'deleteMarkupException').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));

    // WHEN
    component.onDeleteException();

    // THEN
    expect(markupService.deleteMarkupException).toHaveBeenCalled();
    expect(component.markupAccordions.length).toBe(0);
  });

  it('should display Delete Modal Title', () => {
    // GIVEN
    fixture.detectChanges();
    mockTranslateService.translate.and.returnValue('Delete');
    component.editModalInput = new ElementRef({
      getAttribute: () => {
        return 'MarkupName';
      }
    });
    component.deleteModalTitle = new ElementRef({});

    // WHEN
    component.onDeleteClick();

    // THEN
    expect(component.deleteModalTitle.nativeElement.textContent).toBeTruthy();
  });

  it('should display Edit Modal Title', () => {
    // GIVEN
    fixture.detectChanges();
    mockTranslateService.translate.and.returnValue('Edit');
    spyOn(component._renderer, 'setAttribute');
    const $ele = new ElementRef({
      getAttribute: () => {
        return 'markupname';
      }
    });
    component.editModalTitle = new ElementRef({});
    component.editModalInput = new ElementRef({});

    // WHEN
    component.onEdit($ele.nativeElement);

    // THEN
    expect(component.editModalTitle.nativeElement.textContent).toBeTruthy();
  });

  it('should set MarkupOnSell to true if unit = % ', () => {
    // GIVEN
    fixture.detectChanges();
    const value = '%';
    component.markupAccordions.push(MARKUP_GRID_DETAILS);

    // WHEN
    component.setMarkupOnSell(value);

    // THEN
    expect(component.markupOnSellVal).toBeTruthy();
  });


  it('should set MarkupOnSell to false if unit = $ ', () => {
    // GIVEN
    fixture.detectChanges();
    const value = '$';

    // WHEN
    component.setMarkupOnSell(value);

    // THEN
    expect(component.markupOnSellVal).toBeFalsy();
  });

  it('should set toggleMarkupOnSell to false if unit = $ ', () => {
    // GIVEN
    fixture.detectChanges();
    const value = true;

    // WHEN
    component.toggleMarkupOnSell(value);

    // THEN
    expect(component.markupOnSellVal).toBeTruthy();
  });

  it('should remove MarkupGrid name from copyMarkups onEdit of MarkupGrid', () => {
    // GIVEN
    fixture.detectChanges();
    const value = '1';
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    component.copyMarkups.push({markupName: 'Contract', gfsCustomerId: '1'});

    // WHEN
    component.onEditMarkupGrid(value);

    // THEN
    expect(component.copyMarkups.length).toBe(0);
  });

  it('should collapse Markup accordion', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(component._renderer, 'addClass');
    spyOn(component._renderer, 'removeClass');
    const $ele = new ElementRef({
      querySelector: () => {
        return { classList : ['fa', 'fa-angle-down']}  },
      });
    // WHEN
    component.toggleCollapse($ele.nativeElement);

    // THEN
    expect(component._renderer.addClass).toHaveBeenCalled();
    expect(component._renderer.removeClass).toHaveBeenCalled();
  });

  it('should expand Markup accordion', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(component._renderer, 'addClass');
    spyOn(component._renderer, 'removeClass');
    const $ele = new ElementRef({
      querySelector: () => {
        return { classList : ['fa', 'fa-angle-right']}  },
      });
    // WHEN
    component.toggleCollapse($ele.nativeElement);

    // THEN
    expect(component._renderer.addClass).toHaveBeenCalled();
    expect(component._renderer.removeClass).toHaveBeenCalled();
  });

  it('should copy values from one markup grid to another', () => {
    // GIVEN
    fixture.detectChanges();
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    spyOn(component._renderer, 'setAttribute');
    const $event = new ElementRef({
      getAttribute: () => {
        return 0;
      },
      'value' : 151
    });

    // WHEN
    component.onCopyFrom($event.nativeElement);

    // THEN
    expect(component.showMarkupOnSell).toBeTruthy();
    expect(component.markupOnSellVal).toBeFalsy();
  });

  it('should display delete item-level markup modal title', () => {
    // GIVEN
    fixture.detectChanges();
    mockTranslateService.translate.and.returnValue('Delete');
    spyOn(component._renderer, 'setAttribute');
    component.deleteItemModalTitle = new ElementRef({});

    // WHEN
    component.onItemLevelDeleteInfo(ITEM_LEVEL_DELETE_MODEL);

    // THEN
    expect(component.deleteItemModalTitle.nativeElement.textContent).toBeTruthy();
  });

  it('should delete an item-level markup with valid item Id ', () => {
    // GIVEN
    fixture.detectChanges();
    component.markupAccordions.push(MARKUP_GRID_DETAILS_2);
    component.deleteItemModalTitle = new ElementRef({
      getAttribute: () => {
        return 151;
      }
    });
    component.contractPriceProfileId = 121;
    component.isFurtheranceMode = false;
    spyOn(markupService, 'deleteItemLevelMarkup').and.returnValue(Observable.of([MARKUP_GRID_DETAILS_2]));

    // WHEN
    component.deleteItemMarkup();

    // THEN
    expect(markupService.deleteItemLevelMarkup).toHaveBeenCalled();
  });

  it('should delete an item-level markup with future item description', () => {
    // GIVEN
    fixture.detectChanges();
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    component.deleteItemModalTitle = new ElementRef({
      getAttribute: () => {
        return '';
      }
    });
    component.contractPriceProfileId = 121;
    component.isFurtheranceMode = false;
    spyOn(markupService, 'deleteItemLevelMarkup').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));

    // WHEN
    component.deleteItemMarkup();

    // THEN
    expect(markupService.deleteItemLevelMarkup).toHaveBeenCalled();
  });

  it('should delete an item-level markup when furtherance is in progress', () => {
    // GIVEN
    fixture.detectChanges();
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    const gfsCustomerId = '';
    const gfsCustomerType = '';
    const deleteItemDesc = '';
    const deleteItemId = '';
    component.deleteItemModalTitle = new ElementRef({
      getAttribute: () => {
        return '';
      }
    });
    component.contractPriceProfileId = 121;
    component.isFurtheranceMode = true;
    spyOn(furtheranceService, 'deleteItemLevelMarkupFurtherance').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));

    // WHEN
    component.deleteItemMarkup();

    // THEN
    expect(furtheranceService.deleteItemLevelMarkupFurtherance).toHaveBeenCalledWith(component.contractPriceProfileId,
      component.cppFurtheranceSeq, gfsCustomerId, gfsCustomerType, deleteItemId, deleteItemDesc);
  });

  it('should show success toaster', () => {
    // GIVEN
    fixture.detectChanges();
    mockTranslateService.translate.and.returnValue('has been deleted');
    const deleteItemIndex = '2';
    const gfsCustomerId = 151;
    const deleteItemDesc = 'My Item';

    // WHEN
    component.showDeleteItemSuccessMsg(deleteItemIndex, gfsCustomerId, deleteItemDesc);

    // THEN
    expect(mockToasterService.showSuccess).toHaveBeenCalledWith('"My Item"has been deleted', '');
  })

  it('should populate View Markup Grid Data', () => {
    // GIVEN;
    const markupAccordion = MARKUP_GRID_ROW1;
    const markupList = [MARKUP_GRID_ROW];
    spyOn(markupList, 'map').and.returnValue(['GROCERY', 1, '$1.00', 1, '12/30/2017', '12/07/2019']);

     // WHEN
    component.createViewMarkup(markupAccordion);

     expect(markupList.length).toBeTruthy();
  });

  it('should populate View Markup Grid Data when unit type is %', () => {
    // GIVEN;
    const markupAccordion = MARKUP_GRID_ROW2;
    const markupList = [MARKUP_ROW];
    spyOn(markupList, 'map').and.returnValue(['GROCERY', 1, '7.00%', 1, '12/30/2017', '12/07/2019']);

     // WHEN
    component.createViewMarkup(markupAccordion);

    // THEN
     expect(markupList.length).toBeTruthy();
  });

  it('should populate Item Level View Markup Grid Data', () => {
    // GIVEN;
    const markupAccordion = MARKUP_GRID_ROW1;
    const itemLevelMarkupList = [ITEM_LEVEL_MARKUP];
    spyOn(itemLevelMarkupList, 'map').and.returnValue([false, '12', '2', 'Item', '$2', 12, '2019/02/12', '2019/02/15', false, false]);

     // WHEN
     component.createItemLevelViewMarkup(markupAccordion);

     // THEN
     expect(itemLevelMarkupList.length).toBeTruthy();
  });

  it('should populate Item Level View Markup Grid Data when unit type is %', () => {
    // GIVEN;
    const markupAccordion = MARKUP_GRID_ROW2;
    const itemLevelMarkupList = [ITEM_LEVEL_MARKUP];
    spyOn(itemLevelMarkupList, 'map').and.returnValue([false, '12', '2', 'Item', '22%', 12, '2019/02/12', '2019/02/15', false, false]);

     // WHEN
     component.createItemLevelViewMarkup(markupAccordion);

     // THEN
     expect(itemLevelMarkupList.length).toBeTruthy();
  });

  it('should populate View Subgroup Markup Data', () => {
    // GIVEN;
    const markupAccordion = MARKUP_GRID_ROW1;
    const subgroupMarkupList = [SUBGROUP_MARKUP];
    spyOn(subgroupMarkupList, 'map').and.returnValue(['12', 'My Subgroup', '$2.00', 1, '2019/02/12', '2019/02/15']);

     // WHEN
    component.createSubgroupViewMarkup(markupAccordion);

     expect(subgroupMarkupList.length).toBeTruthy();
  });

  it('should populate View Subgroup Markup Data when unit type is %', () => {
    // GIVEN;
    const markupAccordion = MARKUP_GRID_ROW2;
    const subgroupMarkupList = [SUBGROUP_MARKUP_PERCENT];
    spyOn(subgroupMarkupList, 'map').and.returnValue(['12', 'My Subgroup', '2%', 1, '2019/02/12', '2019/02/15']);

     // WHEN
    component.createSubgroupViewMarkup(markupAccordion);

    // THEN
     expect(subgroupMarkupList.length).toBeTruthy();
  });

  it('should fetch markup indicators - true case', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(markupService, 'fetchMarkupIndicators').and.returnValue(Observable.of({'markupOnSell': true}));

    // WHEN
    component.fetchMarkupIndicators();

    // THEN
    expect(component.markupOnSellVal).toBeTruthy();
    expect(component.showMarkupOnSell).toBeTruthy();
    expect(component.showSpinner).toBeFalsy();
  });

  it('should fetch markup indicators - false case', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(markupService, 'fetchMarkupIndicators').and.returnValue(Observable.of({'markupOnSell': false}));

    // WHEN
    component.fetchMarkupIndicators();

    // THEN
    expect(component.markupOnSellVal).toBeFalsy();
    expect(component.showMarkupOnSell).toBeFalsy();
  });

  it('should fetch markup indicators when displayViewMode is true', () => {
    // GIVEN
    fixture.detectChanges();
    component.displayViewMode = true;
    spyOn(markupService, 'fetchMarkupIndicators').and.returnValue(Observable.of({'markupOnSell': true}));

    // WHEN
    component.fetchMarkupIndicators();

    // THEN
    expect(component.createViewMarkup).toBeTruthy();
  });

  it('should fetch markup indicators when displayViewMode is false', () => {
    // GIVEN
    fixture.detectChanges();
    component.displayViewMode = false;
    component.markupAccordions = [MARKUP_GRID_DETAILS_1];
    spyOn(markupService, 'fetchMarkupIndicators').and.returnValue(Observable.of({'markupOnSell': true}));

    // WHEN
    component.fetchMarkupIndicators();

    // THEN
    expect(component.copyMarkups).toBeTruthy();
  });

  it('should fetch expire lower value', () => {
    // GIVEN
    fixture.detectChanges();
    spyOn(markupService, 'fetchMarkupIndicators').and.returnValue(Observable.of({'markupOnSell': false, 'expireLower': true}));

    // WHEN
    component.fetchMarkupIndicators();

    // THEN
    expect(component.markupForm.get('expireLowerQuestion').value).toBeTruthy();
  });

  it('should set isSubmitDisabled to false if any radio button is selected in expire lower ', () => {
    // GIVEN
    fixture.detectChanges();

    // WHEN
    component.onClickExpireLower();

    // THEN
    expect(component.isSubmitDisabled).toBeFalsy();
  });

  it('should verify on exhibit deletion function', () => {
    // GIVEN
    component.actionId = 'ItemMarkupDeletion';
    fixture.detectChanges();
    mockTranslateService.translate.and.returnValue('has been deleted');
    component.markupAccordions.push(MARKUP_GRID_DETAILS_2);
    component.deleteItemModalTitle = new ElementRef({
      getAttribute: () => {
        return 151;
      }
    });
    component.contractPriceProfileId = 121;
    spyOn(markupService, 'deleteItemLevelMarkup').and.returnValue(Observable.of([MARKUP_GRID_DETAILS_2]));

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(component.isPricingExhibitAttached).toBe(false);

    // GIVEN
    component.actionId = 'RenameExceptionMarkup';
    fixture.detectChanges();
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    component.editModalInput = new ElementRef({
      getAttribute: () => {
        return 151;
      }
    });
    component.editmarkupModalForm.get('editMarkupStructure').setValue('RenamedException');
    component.contractPriceProfileId = 121;
    spyOn(markupService, 'renameMarkupException').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(component.isPricingExhibitAttached).toBe(false);

    // GIVEN
    component.actionId = 'ExceptionDeletion';
    fixture.detectChanges();
    mockTranslateService.translate.and.returnValue('has been deleted');
    component.markupAccordions = [MARKUP_GRID_DETAILS];
    component.editModalInput = new ElementRef({
      getAttribute: () => {
        return 151;
      }
    });
    component.contractPriceProfileId = 121;
    spyOn(markupService, 'deleteMarkupException').and.returnValue(Observable.of([MARKUP_GRID_DETAILS]));

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(component.isPricingExhibitAttached).toBe(false);

    // GIVEN
    component.actionId = 'AddException';
    fixture.detectChanges();
    component.markupModalForm.get('markupStructure').setValue('New Exception');
    component.contractPriceProfileId = 121;
    component.effectiveDate = new Date('12/30/2017');
    component.expirationDate = new Date('12/30/2018');
    spyOn(markupService, 'addExceptionDetails').and.returnValue(Observable.of(MARKUP_GRID_DETAILS));

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(component.isPricingExhibitAttached).toBe(false);
    expect(component.markupAccordions).toEqual([MARKUP_GRID_DETAILS]);

    // GIVEN
    component.actionId = 'SaveMarkup';
    fixture.detectChanges();
    component.markupAccordions.push(MARKUP_GRID_DETAILS);
    component.copyMarkups.push({markupName: 'Contract', gfsCustomerId: '1'});
    component.markupModalForm.get('markupStructure').setValue('New Exception');
    component.contractPriceProfileId = 121;
    component.effectiveDate = new Date('12/30/2017');
    component.expirationDate = new Date('12/30/2018');
    component.saveGridDetailsObj = MARKUP_GRID_DETAILS;
    spyOn(markupService, 'saveMarkupGridDetails').and.returnValue(Observable.of(MARKUP_GRID_DETAILS));

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(component.isPricingExhibitAttached).toBe(false);

    // GIVEN
    component.actionId = 'Submit';
    fixture.detectChanges();
    component.contractPriceProfileId = 121;
    component.effectiveDate = new Date('12/30/2017');
    component.expirationDate = new Date('12/30/2018');
    component.markupOnSellVal = true;
    spyOn(markupService, 'saveMarkupIndicators').and.returnValue(Observable.of(MARKUP_GRID_DETAILS));
    spyOn(router, 'navigate');

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(component.isPricingExhibitAttached).toBe(false);
  });

  it('should store event description before deletion of pricing exhibit', () => {
    // GIVEN
    component.isPricingExhibitAttached = true;

    // WHEN
    component.deleteItemMarkup();

    // WHEN
    component.onRenameMarkupException();

    // WHEN
    component.onDeleteException();

    // WHEN
    component.onAddException();

    // WHEN
    component.onSubmit();

    // WHEN
    component.onSaveMarkupGridDetails({}, 'SaveMarkup');

    // THEN
    expect(component.actionId).toBe('SaveMarkup');
  });

  it('should return if is exhibit deleted is false', () => {
    // GIVEN
    const isExhibitDeleted = false;

    // WHEN
    component.onExhibitDeletion(isExhibitDeleted);
  });

  it('should display delete subgroup markup modal title', () => {
    // GIVEN
    mockTranslateService.translate.and.returnValue('Delete');
    spyOn(component._renderer, 'setAttribute');
    component.deleteSubgroupModalTitle = new ElementRef({});

    // WHEN
    component.onSubgroupDeleteInfo(SUBGROUP_DELETE_MODEL);

    // THEN
    expect(component.deleteSubgroupModalTitle.nativeElement.textContent).toBeTruthy();
  });

  it('should delete an subgroup markup with valid subgroup Id ', () => {
    // GIVEN
    mockTranslateService.translate.and.returnValue('has been deleted');
    component.markupAccordions.push(MARKUP_GRID_DETAILS_2);
    component.deleteSubgroupModalTitle = new ElementRef({
      getAttribute: () => {
        return 151;
      }
    });
    component.contractPriceProfileId = 121;
    spyOn(markupService, 'deleteSubgroupMarkup').and.returnValue(Observable.of({}));

    // WHEN
    component.deleteSubgroupMarkup();

    // THEN
    expect(markupService.deleteSubgroupMarkup).toHaveBeenCalled();
    expect(mockTranslateService.translate).toHaveBeenCalled();
  });

  it('should determine the value of the furtherance indicator', () => {
    // GIVEN
    component.displayViewMode = true;
    component.canEditFurtherance = true;

    // WHEN
    component.determineFurtheranceIndicator();

    // THEN
    expect(component.determineFurtheranceMode).toBeTruthy();
    expect(component.displayViewMode).toBeTruthy();
    expect(component.canEditFurtherance).toBeTruthy();

    // GIVEN
    component.displayViewMode = true;
    component.canEditFurtherance = false;

    // WHEN
    component.determineFurtheranceIndicator();

    // THEN
    expect(component.determineFurtheranceMode).toBeFalsy();
    expect(component.displayViewMode).toBeTruthy();
    expect(component.canEditFurtherance).toBeFalsy();

    // GIVEN
    component.displayViewMode = false;
    component.canEditFurtherance = false;

    // WHEN
    component.determineFurtheranceIndicator();

    // THEN
    expect(component.determineFurtheranceMode).toBeTruthy();
    expect(component.displayViewMode).toBeFalsy();
    expect(component.canEditFurtherance).toBeFalsy();

    // GIVEN
    component.displayViewMode = false;
    component.canEditFurtherance = true;

    // WHEN
    component.determineFurtheranceIndicator();

    // THEN
    expect(component.determineFurtheranceMode).toBeTruthy();
    expect(component.displayViewMode).toBeFalsy();
    expect(component.canEditFurtherance).toBeTruthy();
  })

  it('should disable markup unit selections if all product type have unit as %', () => {
     // GIVEN
  const productMarkupList1: MarkupGridModel[] = [{
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

  const productMarkupList2: MarkupGridModel[] = [{
    'productType': 'BEVERAGE',
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
    'productType': 'DESERT',
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
    'productType': 'CHOCOLATE',
    'itemPriceId': 6,
    'markup': '6.00',
    'unit': '%',
    'markupType': 1,
    'effectiveDate': new Date('12/12/2020'),
    'expirationDate': new Date('15/12/2020'),
    'isInvalidCurrency': false,
    'invalidMarkup': false
  }]

  const markupGridDetails: MarkupGridDetails[] = [
    {
      'gfsCustomerId': '150',
      'markupName': 'Contract',
      'gfsCustomerType': 31,
      'isMarkupSaved': true,
      'contractPriceProfileSeq': 1234,
      'productMarkupList': productMarkupList1,
      'itemLevelMarkupList': itemLevelMarkupValues,
      'subgroupMarkupList': subgroupMarkupValues,
      'markupGridIndex': 12,
      'cppFurtheranceSeq': 345
    },
    {
      'gfsCustomerId': '150',
      'markupName': 'Contract',
      'gfsCustomerType': 31,
      'isMarkupSaved': true,
      'contractPriceProfileSeq': 1234,
      'productMarkupList': productMarkupList2,
      'itemLevelMarkupList': itemLevelMarkupValues,
      'subgroupMarkupList': subgroupMarkupValues,
      'markupGridIndex': 12,
      'cppFurtheranceSeq': 345
    }
  ]

  // WHEN & THEN
  expect(component.fetchMarkupUnitState(markupGridDetails)).toBeTruthy();
  })

  it('should return false if atleast one product type have unit as $', () => {
    // GIVEN
 const productMarkupList1: MarkupGridModel[] = [{
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

 const productMarkupList2: MarkupGridModel[] = [{
   'productType': 'BEVERAGE',
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
   'productType': 'DESERT',
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
   'productType': 'CHOCOLATE',
   'itemPriceId': 6,
   'markup': '6.00',
   'unit': '$',
   'markupType': 2,
   'effectiveDate': new Date('12/12/2020'),
   'expirationDate': new Date('15/12/2020'),
   'isInvalidCurrency': false,
   'invalidMarkup': false
 }]

 const markupGridDetails: MarkupGridDetails[] = [
   {
     'gfsCustomerId': '150',
     'markupName': 'Contract',
     'gfsCustomerType': 31,
     'isMarkupSaved': true,
     'contractPriceProfileSeq': 1234,
     'productMarkupList': productMarkupList1,
     'itemLevelMarkupList': itemLevelMarkupValues,
     'subgroupMarkupList': subgroupMarkupValues,
     'markupGridIndex': 12,
     'cppFurtheranceSeq': 345
   },
   {
     'gfsCustomerId': '150',
     'markupName': 'Contract',
     'gfsCustomerType': 31,
     'isMarkupSaved': true,
     'contractPriceProfileSeq': 1234,
     'productMarkupList': productMarkupList2,
     'itemLevelMarkupList': itemLevelMarkupValues,
     'subgroupMarkupList': subgroupMarkupValues,
     'markupGridIndex': 12,
     'cppFurtheranceSeq': 345
   }
 ]

 // WHEN & THEN
 expect(component.fetchMarkupUnitState(markupGridDetails)).toBeFalsy();
 })

})

