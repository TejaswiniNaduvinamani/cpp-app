import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DecimalPipe } from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Observable';
import { Renderer2, EventEmitter, ElementRef } from '@angular/core';

import { ItemLevelMarkupModel } from '../item-level-markup/item-level-markup.model';
import { MarkupService, AuthorizationService } from '../../../shared';
import { MarkupGridDetails, MarkupGridModel } from '../markup-grid/markup-grid.model';
import { SubgroupMarkupComponent } from './subgroup-markup.component';
import { SubgroupMarkupModel } from './subgroup-markup.model';

describe('SubgroupMarkupComponent', () => {
  const MARKUP_GRID_MODEL: MarkupGridModel = new MarkupGridModel('GROCERY', 1, '1', '%', 1,
  new Date('12/30/2020'), new Date('12/30/2021'), false, false);
  const MARKUP_GRID_MODEL2: MarkupGridModel = new MarkupGridModel('FROZEN', 2, '0.00', '$', 1,
  new Date('12/30/2020'), new Date('12/30/2021'), false, false);

  const SUBGROUP_MARKUP: SubgroupMarkupModel = new SubgroupMarkupModel('123', 'My Subgroup',
     '2', '$', 1, new Date('2019/02/12'), new Date('2019/02/15'), false, false, false, false, false);

  const DEFAULT_ITEM_LEVEL_GRID: ItemLevelMarkupModel = new ItemLevelMarkupModel(
      false, '166720', '1', 'BLUEBERRY IQF 4-5# GFS', '10', '$', 1,
      new Date('12/30/2020'), new Date('12/30/2021'), false, false, false, false, false, false);

  const MARKUP_GRID_DETAILS: MarkupGridDetails = new MarkupGridDetails('151', 31, 123, 'qwerty', [MARKUP_GRID_MODEL,
      MARKUP_GRID_MODEL2], [SUBGROUP_MARKUP], [DEFAULT_ITEM_LEVEL_GRID], true, 1);

  let component: SubgroupMarkupComponent;
  let fixture: ComponentFixture<SubgroupMarkupComponent>;
  let markupService: MarkupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SubgroupMarkupComponent],
      providers: [
        DecimalPipe,
        Renderer2,
        MarkupService,
        AuthorizationService
      ]
    }).overrideTemplate(SubgroupMarkupComponent, '');

    fixture = TestBed.createComponent(SubgroupMarkupComponent);
    component = fixture.componentInstance;
    markupService = TestBed.get(MarkupService);
  });

  it('should create the Subgroup-Markup component', () => {
    // WHEN
    fixture.detectChanges();
    // THEN
   expect(component).toBeTruthy();
  });

  it('should update the user entered markup value ', () => {
    // GIVEN
    const row =  SUBGROUP_MARKUP;
    const event = { 'target': { 'value': '10' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editSubgroupGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onMarkupKeyUp(event, row);

    // THEN
    expect(row.markup).toBe('10');
    expect(component.editSubgroupGrid.emit).toHaveBeenCalled();
    expect(component.markupGridDetails).toBe(MARKUP_GRID_DETAILS);
  });

  it('should set error flag if subgroup markup value is invalid', () => {
    // GIVEN
    const row = SUBGROUP_MARKUP;
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

  it('should set error flag if subgroup markup value is Non-numeric', () => {
    // GIVEN
    const row = SUBGROUP_MARKUP;
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

  it('should round-off subgroup markup value when valid value is entered', () => {
    // GIVEN
    const row = SUBGROUP_MARKUP;
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

  it('should round-off subgroup markup value and set error flag when 0 is entered', () => {
    // GIVEN
    const row = SUBGROUP_MARKUP;
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

  it('should fetch subgroup description for valid subgroupId', () => {
    // GIVEN
    const row =  SUBGROUP_MARKUP;
    const event = { 'target': { 'value': '123' }};
    component.subgroupMarkup = [SUBGROUP_MARKUP];
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.subgroupDescriptionStatus, 'emit');
    spyOn(component.renderer, 'addClass');
    spyOn(markupService, 'fetchSubgroupDescription').and.returnValue(Observable.of({
      'subgroupId': '123',
      'subgroupDesc': 'My Subgroup',
      'validationMessage': 'Va',
      'validationCode': 200
    }));
    fixture.detectChanges();

    // WHEN
    component.onblurSubgroupId(event, row);

    // THEN
    expect(markupService.fetchSubgroupDescription).toHaveBeenCalled();
    expect(row.subgroupDesc).toBe('My Subgroup');
    expect(component.subgroupDescriptionStatus.emit).toHaveBeenCalled();
  });

  it('should not fetch subgroup description for already existing subgroupId', () => {
    // GIVEN
    const row =  SUBGROUP_MARKUP;
    const event = { 'target': { 'value': '125' }};
    component.subgroupMarkup = [SUBGROUP_MARKUP];
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.subgroupDescriptionStatus, 'emit');
    spyOn(component.renderer, 'addClass');
    spyOn(markupService, 'fetchSubgroupDescription').and.returnValue(Observable.of({
      'subgroupId': '125',
      'subgroupDesc': '',
      'validationMessage': 'Duplicate',
      'validationCode': 123
    }));
    fixture.detectChanges();

    // WHEN
    component.onblurSubgroupId(event, row);

    // THEN
    expect(row.subgroupDesc).toBe('');
    expect(component.subgroupDescriptionStatus.emit).toHaveBeenCalled();
  });

  it('should not fetch subgroup description for invalid subgroupId', () => {
    // GIVEN
    const row =  SUBGROUP_MARKUP;
    const event = { 'target': { 'value': '125' }};
    component.subgroupMarkup = [SUBGROUP_MARKUP];
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.renderer, 'setAttribute');
    spyOn(component.subgroupDescriptionStatus, 'emit');
    spyOn(component.renderer, 'addClass');
    spyOn(markupService, 'fetchSubgroupDescription').and.returnValue(Observable.of({
      'subgroupId': '125',
      'subgroupDesc': '',
      'validationMessage': 'Invalid',
      'validationCode': 124
    }));
    fixture.detectChanges();

    // WHEN
    component.onblurSubgroupId(event, row);

    // THEN
    expect(row.subgroupDesc).toBe('');
    expect(component.subgroupDescriptionStatus.emit).toHaveBeenCalled();
  });

  it('should update the user entered MarkupType value', () => {

    const row =  SUBGROUP_MARKUP;
    const event = { 'target': { 'value': 3 }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editSubgroupGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onMarkupTypeChange(event, row);

    // THEN
    expect(row.markupType).toBe(3);
    expect(component.editSubgroupGrid.emit).toHaveBeenCalled();
  });

  it('should update the user entered unit % value', () => {

    const row =  SUBGROUP_MARKUP;
    const event = { 'target': { 'value': '%' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editSubgroupGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onUnitChange(event, row);

    // THEN
    expect(row.unit).toBe('%');
    expect(component.editSubgroupGrid.emit).toHaveBeenCalled();
  });

  it('should update the user entered unit $ value', () => {

    const row =  SUBGROUP_MARKUP;
    const event = { 'target': { 'value': '$' }};
    component.markupGridDetails = MARKUP_GRID_DETAILS;
    spyOn(component.editSubgroupGrid, 'emit');
    fixture.detectChanges();

    // WHEN
    component.onUnitChange(event, row);

    // THEN
    expect(row.unit).toBe('$');
    expect(component.editSubgroupGrid.emit).toHaveBeenCalled();
  });

  it('should allow only positive numeric value for Subgroup Id', () => {
    // GIVEN
    const row =  SUBGROUP_MARKUP;
    const event = jasmine.createSpyObj('e', [ 'preventDefault' ]);

    // WHEN
    component.onSubgroupIdKeyPress(event, row);

    // THEN
    expect(row.subgroupId).toBe('125');
    expect(event.preventDefault).toHaveBeenCalled();
  });

  it('should set isMarkupSaved to false on edit of subgroup Id', () => {
    // GIVEN
     const row =  SUBGROUP_MARKUP;
     component.markupGridDetails = MARKUP_GRID_DETAILS;
     const event = { 'target': { 'value': '10' }};
     fixture.detectChanges();

     // WHEN
     component.onSubgroupIdKeyUp(event, row);

     // THEN
     expect(component.markupGridDetails.isMarkupSaved).toBeFalsy();
   });

   it('should get subgroupId and subgroupDesc attributes', () => {
    // GIVEN
    spyOn(component.subgroupMarkupDeleteInfo, 'emit');
    const element = new ElementRef({
      getAttribute: () => {
        return 'FLOOR MATS';
      }
    });
    const element1 = new ElementRef({
      getAttribute: () => {
        return '20213';
      }
    });

    // WHEN
    component.onDeleteSubgroupMarkup(element.nativeElement);

    // THEN
    expect(component.subgroupDeleteModel.subgroupDesc).toBe('FLOOR MATS');

    // WHEN
    component.onDeleteSubgroupMarkup(element1.nativeElement);

    // THEN
    expect(component.subgroupDeleteModel.subgroupId).toBe('20213');
    expect(component.subgroupMarkupDeleteInfo.emit).toHaveBeenCalled();
   });

});
