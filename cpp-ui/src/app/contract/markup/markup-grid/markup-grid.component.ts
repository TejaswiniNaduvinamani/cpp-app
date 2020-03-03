import { Component, OnInit, Input, Output, EventEmitter, Renderer2, ViewChild, OnDestroy, DoCheck } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';

import {
  IMAGE_PATH, MARKUP_TYPES, UNIT_TYPES, MARKUP_PATTERN, ZERO_VALUE, MarkupService, ONE } from 'app/shared';
import { ItemLevelMarkupModel, ItemLevelDeleteModel,
  SubgroupMarkupModel, SubgroupMarkupDeleteModel } from './../../../contract';
import { MarkupGridModel, MarkupGridDetails } from './markup-grid.model';
declare var $;

const MIN_MARKUP_DOLLAR = 0.05;
const MAX_MARKUP_DOLLAR = 5.00;
const MIN_MARKUP_PERCENT = 5.00;
const MAX_MARKUP_PERCENT = 45.00;

@Component({
  selector: 'app-markup-grid',
  templateUrl: './markup-grid.component.html',
  styleUrls: ['./markup-grid.component.scss']
})
export class MarkupGridComponent implements OnInit, OnDestroy, DoCheck {
  @Input() rows: MarkupGridModel[];
  @Input() exceptionIndex = 0;
  @Input() contractPriceProfileId: string;
  @Input() effectiveDate: Date;
  @Input() expirationDate: Date;
  @Input() displayViewMode: boolean;
  @Input() isFurtheranceMode: boolean;
  @Input() markupGridDetails: MarkupGridDetails;
  @Input() itemLevelMarkupList: ItemLevelMarkupModel[];
  @Input() subgroupMarkupList: SubgroupMarkupModel[];
  @Input() markupOnSellVal: boolean;
  @Input() determineFurtheranceMode: boolean;
  @Input() isMarkupUnitDisabled: boolean;
  @Input() showMarkupOnSell: boolean;
  @Output() saveMarkupGridDetails = new EventEmitter<MarkupGridDetails>();
  @Output() editMarkupGrid = new EventEmitter<string>();
  @Output() onUnitChangeVal = new EventEmitter<string>();
  @Output() itemLevelDeleteInfo = new EventEmitter<ItemLevelDeleteModel>();
  @Output() subgroupDeleteInfo = new EventEmitter<SubgroupMarkupDeleteModel>();

  public selectedRows: MarkupGridModel[] = [];
  public markupGridForm: FormGroup;
  public selected = [];
  public imageDir = IMAGE_PATH;
  public isSaveGrayed = true;
  private subscription: Subscription[] = [];
  public itemIdValidError = false;
  public subgroupIdValidError = false;
  public itemIdActiveError = false;
  public itemIdDuplicateError = false;
  public subgroupIdDuplicateError = false;
  public markupRequiredError = false;
  public itemIdRequiredError = false;
  public subgroupIdRequiredError = false;
  public itemDescRequiredError = false;
  public markupInvalidError = false;
  public markupFormatValidError = false;
  public itemExitsInDBError = false;
  public isAddItemDisabled: boolean;
  public isAddSubgroupDisabled: boolean;

  constructor(
    private _decimalPipe: DecimalPipe,
    private formBuilder: FormBuilder,
    public _renderer: Renderer2,
    private _markupService: MarkupService,
  ) { }

  ngOnInit() {
    this.loadForm();
  }

  ngDoCheck() {
    this.checkMarkupValidations();
   }

  loadForm() {
    this.markupGridForm = this.formBuilder.group({
      expireLowerToggle: this.formBuilder.group({
        selectedValue: [false]
      })
    });
  }

  isSaveGrayedOut() {
    return this.isSaveGrayed = (this.rows.some(element => !element.markup) ||
      this.itemLevelMarkupList.some(element => !element.markup) ||
      this.subgroupMarkupList.some(element => !element.markup) ||
      this.subgroupMarkupList.some(element => !element.subgroupId) ||
      this.itemLevelMarkupList.some(function (itemLevel) {
        if (!itemLevel.noItemId) { // incase of 0 - unchecked
          return !itemLevel.itemId;
        }
      }) ||
      this.itemLevelMarkupList.some(function (itemLevel) {
        if (itemLevel.noItemId) { // incase of 1 - checked
          return !itemLevel.itemDesc;
        }
      }) ||
      this.rows.some(element => element.isInvalidCurrency) ||
      this.rows.some(element => element.invalidMarkup) ||
      this.itemLevelMarkupList.some(element => element.isInvalidMarkupCurrency) ||
      this.itemLevelMarkupList.some(element => element.invalidMarkup) ||
      this.subgroupMarkupList.some(element => element.isInvalidMarkupCurrency) ||
      this.subgroupMarkupList.some(element => element.invalidMarkup) ||
      this.itemIdValidError || this.itemIdActiveError || this.itemIdDuplicateError || this.itemExitsInDBError ||
      this.subgroupIdValidError || this.subgroupIdDuplicateError)
  }

  onEditItemLevelGrid(editgfsCustomerId) {
    this.editMarkupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onEditSubgroupGrid(editgfsCustomerId) {
    this.editMarkupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onMarkupKeyUp($event, row) {
    row.markup = $event.target.value;
    this.markupGridDetails.isMarkupSaved = false;
    this.editMarkupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onMarkupTypeChange(event, row) {
    row.markupType = event.target.value;
    this.markupGridDetails.isMarkupSaved = false;
    this.editMarkupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onUnitChange(event, row) {
    row.unit = event.target.value;
    if (event.target.value === UNIT_TYPES.PERCENT) {
      row.markupType = MARKUP_TYPES.SELL_UNIT;
    }
    this.onUnitChangeVal.emit(row.unit);
    this.markupGridDetails.isMarkupSaved = false;
    this.editMarkupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onItemLevelUnitChange(unit) {
    this.onUnitChangeVal.emit(unit);
  }

  onSubgroupUnitChange(unit) {
    this.onUnitChangeVal.emit(unit);
  }

  onblurMarkup(event, row) {
    event.target.value = (event.target.value).trim();
    if (!MARKUP_PATTERN.test(event.target.value) && (event.target.value)) {
      this.setMarkupValidationFlag(event, row, false, true);
    } else {
      const markupVal = event.target.value ? this._decimalPipe.transform(event.target.value, '1.2-2') : '';
      const markupValArray = (markupVal).split('.');
      if (markupValArray[0] && markupValArray[0].length > 2 || markupVal === ZERO_VALUE) {
        if (markupVal === ZERO_VALUE) {
          this.roundOffMarkupValue(event, row);
        }
        this.setMarkupValidationFlag(event, row, true, false);
      } else {
        this.roundOffMarkupValue(event, row);
        this.setMarkupValidationFlag(event, row, false, false);
      }
    }
  }

  roundOffMarkupValue(event, row) {
    const markupVal = event.target.value ? this._decimalPipe.transform(event.target.value, '1.2-2') : '';
    row.markup = markupVal.replace(/,/g, '');
    this._renderer.setProperty(event.target, 'value', markupVal.replace(/,/g, ''));
  }

  setMarkupValidationFlag(event, row, invalidMarkup, isInvalidCurrency) {
    row.isInvalidCurrency = isInvalidCurrency;
    row.invalidMarkup = invalidMarkup;
    if (invalidMarkup || isInvalidCurrency) {
      this._renderer.addClass(event.target, 'markup-error');
    } else {
      this._renderer.removeClass(event.target, 'markup-error');
    }
  }

  copyRow(event, row) {
    this.rows.forEach((ele, index) => {
      const i = (this.exceptionIndex * this.rows.length) + index;
      ele.markup = row.markup;
      ele.unit = row.unit;
      ele.markupType = row.markupType;
      if (row.unit === UNIT_TYPES.PERCENT) {
        this._renderer.setAttribute(document.getElementsByClassName('markup-type')[i], 'disabled', 'true');
      } else {
        this._renderer.removeAttribute(document.getElementsByClassName('markup-type')[i], 'disabled');
      }
      if (row.isInvalidCurrency || row.invalidMarkup) {
        this._renderer.addClass(document.getElementsByClassName('markup')[i], 'markup-error');
        this.rows[index].invalidMarkup = row.invalidMarkup;
        this.rows[index].isInvalidCurrency = row.isInvalidCurrency;
      } else {
        this._renderer.removeClass(document.getElementsByClassName('markup')[i], 'markup-error');
        this.rows[index].invalidMarkup = false;
        this.rows[index].isInvalidCurrency = false;
      }
    });
    this.markupGridDetails.isMarkupSaved = false;
    this.onUnitChangeVal.emit(row.unit);
    this.editMarkupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  validateData(row) {
    if (row.markup !== '' &&
      ((row.unit === UNIT_TYPES.DOLLAR && (row.markup) < MIN_MARKUP_DOLLAR) ||
        (row.unit === UNIT_TYPES.DOLLAR && (row.markup) > MAX_MARKUP_DOLLAR) ||
        (row.unit === UNIT_TYPES.PERCENT && (row.markup) < MIN_MARKUP_PERCENT) ||
        (row.unit === UNIT_TYPES.PERCENT && (row.markup) > MAX_MARKUP_PERCENT))) {
      return true;
    } else {
      return false;
    }
  }

  checkValidity(): boolean {
    this.markupRequiredError = this.rows.some(element => !element.markup) ||
      this.itemLevelMarkupList.some(element => !element.markup) ||
      this.subgroupMarkupList.some(element => !element.markup);
    this.itemIdRequiredError = this.itemLevelMarkupList.some(function (itemLevelMarkup) {
      if (!itemLevelMarkup.noItemId) {
        return !itemLevelMarkup.itemId;
      }
    });
    this.subgroupIdRequiredError = this.subgroupMarkupList.some(subgroup => !subgroup.subgroupId);
    this.itemDescRequiredError = this.itemLevelMarkupList.some(function (itemLevelMarkup) {
      if (itemLevelMarkup.noItemId) {
        return !itemLevelMarkup.itemDesc;
      }
    });
    if (this.markupRequiredError || this.itemIdRequiredError || this.itemDescRequiredError ||
      this.itemIdValidError || this.itemIdActiveError || this.itemIdDuplicateError || this.itemExitsInDBError
      || this.markupFormatValidError || this.markupInvalidError || this.subgroupIdValidError ||
      this.subgroupIdDuplicateError || this.subgroupIdRequiredError) {
      return true;
    } else {
      return false;
    }
  }

  onSubmit($event) {
    if (!this.checkValidity()) {
      this.markupGridDetails.productMarkupList = this.rows;
      this.markupGridDetails.itemLevelMarkupList = this.itemLevelMarkupList;
      this.markupGridDetails.subgroupMarkupList = this.subgroupMarkupList;
      this.markupGridDetails.markupGridIndex = this.exceptionIndex;
      this.saveMarkupGridDetails.emit(this.markupGridDetails);
    }
  }

  onAddItemLevelMarkup() {
    this.isAddItemDisabled = true;
    this.subscription.push(this._markupService.fetchItemLevelDefaults(this.contractPriceProfileId, this.effectiveDate,
      this.expirationDate)
      .subscribe(row => {
        if ( this.isMarkupUnitDisabled && this.isFurtheranceMode ) {
          this.assignDefaultPercentageValue(row);
        } else if (!this.markupOnSellVal && !this.isMarkupUnitDisabled && this.isFurtheranceMode && this.showMarkupOnSell) {
          this.assignDefaultPercentageValue(row);
        }
      this.itemLevelMarkupList.push(row);
      this.markupGridDetails.isMarkupSaved = false;
      this.editMarkupGrid.emit(this.markupGridDetails.gfsCustomerId);
      this.isAddItemDisabled = false;
    }));
  }

  assignDefaultPercentageValue(row: ItemLevelMarkupModel) {
    row.unit = UNIT_TYPES.PERCENT;
    row.markupType = ONE;
  }

  onAddSubgroupMarkup() {
    this.isAddSubgroupDisabled = true;
    this.subscription.push(this._markupService.fetchDefaultSubgroupMarkup(this.expirationDate, this.effectiveDate)
  .subscribe(subgroupRow => {
    this.subgroupMarkupList.push(subgroupRow);
    this.markupGridDetails.isMarkupSaved = false;
    this.editMarkupGrid.emit(this.markupGridDetails.gfsCustomerId);
    this.isAddSubgroupDisabled = false;
  }));
  }

  checkMarkupValidations() {
    this.itemIdValidError = (this.itemLevelMarkupList.some(function (element) {
      if (!element.noItemId) {
        return element.invalid;
      }
    }));
    this.subgroupIdValidError = (this.subgroupMarkupList.some(element => element.invalid));
    this.itemIdActiveError = (this.itemLevelMarkupList.some(element => element.inactive));
    this.markupFormatValidError = this.itemLevelMarkupList.some(element => element.isInvalidMarkupCurrency)
      || this.rows.some(element => element.isInvalidCurrency) || this.subgroupMarkupList.some(element => element.isInvalidMarkupCurrency);
    this.markupInvalidError = this.itemLevelMarkupList.some(element => element.invalidMarkup)
      || this.rows.some(element => element.invalidMarkup) || this.subgroupMarkupList.some(element => element.invalidMarkup);
    let itemIdArray = this.itemLevelMarkupList.map(function (item) { return item.itemId });
    this.itemIdDuplicateError = itemIdArray.some(function (itemId, itemIndex) {
      if (itemId) {
        return itemIdArray.indexOf(itemId) !== itemIndex;
      }
    });
    let subgroupIdArray = this.subgroupMarkupList.map(function (subgroup) { return subgroup.subgroupId });
    this.subgroupIdDuplicateError = subgroupIdArray.some(function (subgroupId, subgroupIndex) {
      if (subgroupId) {
        return subgroupIdArray.indexOf(subgroupId) !== subgroupIndex;
      }
    });
    this.itemExitsInDBError = this.itemLevelMarkupList.some(element => element.isItemAlreadyExist);
    let itemDescArray = this.itemLevelMarkupList.map(function (item) { return item.itemDesc.toLowerCase().trim() });
    this.itemIdDuplicateError = itemDescArray.some(function (itemDesc, index) {
      if (itemDesc) {
        return itemDescArray.indexOf(itemDesc) !== index;
      }
    });
  }

  onItemLevelDeleteInfo(itemLevelDeleteInfo: ItemLevelDeleteModel) {
    itemLevelDeleteInfo.gfsCustomerId = this.markupGridDetails.gfsCustomerId;
    itemLevelDeleteInfo.gfsCustomerType = this.markupGridDetails.gfsCustomerType;
    this.itemLevelDeleteInfo.emit(itemLevelDeleteInfo);
  }

  onSubgroupDeleteInfo(subgroupDeleteInfo: SubgroupMarkupDeleteModel) {
    subgroupDeleteInfo.gfsCustomerId = this.markupGridDetails.gfsCustomerId;
    subgroupDeleteInfo.gfsCustomerType = this.markupGridDetails.gfsCustomerType;
    this.subgroupDeleteInfo.emit(subgroupDeleteInfo);
  }

  onMarkupTipContent() {
    $('#markuptipContent').modal('show');
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }
}
