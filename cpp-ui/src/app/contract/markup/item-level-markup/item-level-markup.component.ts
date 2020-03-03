import { Component, Input, Renderer2, Output, EventEmitter, OnDestroy, ViewChild, ElementRef, DoCheck } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { Subscription } from 'rxjs/Subscription';

import { ItemAssignmentModel, FutureItemModel } from 'app/assignment-header';
import { ItemLevelMarkupModel, ItemLevelDeleteModel, ItemDetailsForFurtheranceModel } from './item-level-markup.model';
import { MarkupGridDetails } from './../markup-grid/markup-grid.model';
import { ZERO_VALUE, BACKSPACE_KEY, MARKUP_PATTERN, IMAGE_PATH, UNIT_TYPES, MARKUP_TYPES, ITEM_ID_PATTERN,
  MarkupService, TranslatorService, ToasterService, FurtheranceService, DUPLICATE_ITEM_ERROR_CODE } from 'app/shared';
declare var $;

@Component({
  selector: 'app-item-level-markup',
  templateUrl: './item-level-markup.component.html',
  styleUrls: ['./item-level-markup.component.scss']
})

export class ItemLevelMarkupComponent implements OnDestroy, DoCheck {

  @ViewChild('saveAssignmentsBtn') public saveAssignmentsBtn: ElementRef;

  @Input() itemLevelMarkupList: Array<ItemLevelMarkupModel>;
  @Input() markupGridDetails: MarkupGridDetails;
  @Input() contractPriceProfileId: number;
  @Input() displayViewMode: boolean;
  @Input() isFurtheranceMode: boolean;
  @Input() markupOnSellVal: boolean;
  @Input() exceptionIndex = 0;
  @Input() isMarkupUnitDisabled: boolean;
  @Input() determineFurtheranceMode: boolean;
  @Output() editItemLevelGrid = new EventEmitter<string>();
  @Output() itemDescriptionStatus = new EventEmitter<string>();
  @Output() itemLevelDeleteInfo = new EventEmitter<ItemLevelDeleteModel>();
  @Output() onItemLevelUnitChangeVal = new EventEmitter<string>();

  public currentItemDesc: string;
  public imageDir = IMAGE_PATH;
  public selected = [];
  public subscription: Subscription[] = [];
  public itemLevelDeleteModel = <ItemLevelDeleteModel>{};
  public itemAssignmentList: ItemAssignmentModel[] = [];
  public itemDetails: FutureItemModel;
  public saveItemDetailsForFurtherance = <ItemDetailsForFurtheranceModel>{};
  public disableSaveAssignment = true;
  public duplicateOnSaveFound: boolean;
  public duplicateList: string;
  public futureItemDeleteIndex: number;
  public deleteItemTitle = '';
  public disableSaveForFurtherance = false;

  constructor(
    private _decimalPipe: DecimalPipe,
    public renderer: Renderer2,
    public furtheranceService: FurtheranceService,
    public markupService: MarkupService,
    public translatorService: TranslatorService,
    public toaster: ToasterService
  ) { }

  ngDoCheck() {
    if (this.disableSaveForFurtherance) {
    this.disableSaveAssignmentForMarkup();
    }
  }

  onMarkupKeyUp($event, row: ItemLevelMarkupModel) {
    row.markup = $event.target.value;
    this.markupGridDetails.isMarkupSaved = false;
    this.editItemLevelGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onblurMarkup(event, row: ItemLevelMarkupModel) {
    event.target.value = (event.target.value).trim();
    if (!MARKUP_PATTERN.test(event.target.value) && (event.target.value)) {
      this.setItemMarkupValidationFlag(event, row, false, true);
    } else {
      const markupVal = event.target.value ? this._decimalPipe.transform(event.target.value, '1.2-2') : '';
      const markupValArray = (markupVal).split('.');
      if (markupValArray[0] && markupValArray[0].length > 2 || markupVal === ZERO_VALUE) {
        if (markupVal === ZERO_VALUE) {
        this.roundOffMarkupValue(event, row);
      }
        this.setItemMarkupValidationFlag(event, row, true, false);
      } else {
        this.roundOffMarkupValue(event, row);
        this.setItemMarkupValidationFlag(event, row, false, false);
      }
    }
  }

  roundOffMarkupValue(event, row) {
    const markupVal = event.target.value ? this._decimalPipe.transform(event.target.value, '1.2-2') : '';
    row.markup = markupVal.replace(/,/g, '');
    this.renderer.setProperty(event.target, 'value', markupVal.replace(/,/g, ''));
  }

  setItemMarkupValidationFlag(event, row, invalidMarkup, isInvalidMarkupCurrency) {
    row.isInvalidMarkupCurrency = isInvalidMarkupCurrency;
    row.invalidMarkup = invalidMarkup;
    if (invalidMarkup || isInvalidMarkupCurrency) {
      this.renderer.addClass(event.target, 'itemlevel-markup-error');
    } else {
      this.renderer.removeClass(event.target, 'itemlevel-markup-error');
    }
  }

  onKeyupItemDesc(event, row) {
    row.itemDesc = (event.target.value).trim();
    this.markupGridDetails.isMarkupSaved = false;
    this.editItemLevelGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onPasteItemDesc() {
    this.markupGridDetails.isMarkupSaved = false;
    this.editItemLevelGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onblurItemDesc(row) {
    row.itemDesc = row.itemDesc.trim();
  }

  onblurItemId(event, row: ItemLevelMarkupModel) {
    const itemId = (event.target.value).trim();
    row.itemId = itemId;
    const cmgCustomerId = this.markupGridDetails.gfsCustomerId;
    const cmgCustomerTypeCode = String(this.markupGridDetails.gfsCustomerType);
    const contractPriceProfileSeq = String(this.markupGridDetails.contractPriceProfileSeq);
    row.itemDesc = '';
    row.stockingCode = '';
    if (itemId) {
      this.subscription.push(this.markupService.fetchItemDescription(itemId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq)
        .subscribe(itemDescDetails => {
          if (itemDescDetails.isItemAlreadyExist) {
            this.setItemDescDetails(row, true, false, false);
          } else if (!itemDescDetails.isValid) {
            this.setItemDescDetails(row, false, true, false);
          } else if (itemDescDetails.isValid && !itemDescDetails.isActive) {
            this.setItemDescDetails(row, false, false, true);
          } else if (itemDescDetails.isValid && itemDescDetails.isActive && !itemDescDetails.isItemAlreadyExist) {
            row.itemDesc = itemDescDetails.itemDescription;
            row.stockingCode = itemDescDetails.stockingCode;
            this.setItemDescDetails(row, false, false, false);
          }
        }));
    }
  }

  setItemDescDetails(row, isItemAlreadyExist, invalid, inactive) {
    row.isItemAlreadyExist = isItemAlreadyExist;
    row.invalid = invalid;
    row.inactive = inactive;
    this.itemDescriptionStatus.emit();
  }

  onMarkupTypeChange(event, row) {
    row.markupType = event.target.value;
    this.markupGridDetails.isMarkupSaved = false;
    this.editItemLevelGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onChangeNoItemId(row: ItemLevelMarkupModel) {
    if (!row.isItemSaved) {
      row.stockingCode = '';
      row.itemDesc = '';
      row.itemId = '';
      row.invalid = false;
      row.inactive = false;
      if (row.noItemId) {
        row.noItemId = false;
      } else {
        row.noItemId = true;
      }
      this.itemDescriptionStatus.emit();
      this.markupGridDetails.isMarkupSaved = false;
      this.editItemLevelGrid.emit(this.markupGridDetails.gfsCustomerId);
    } else {
      return false;
    }
  }

  onItemIdKeyPress($event, row) {
    const inputChar = String.fromCharCode($event.charCode);
    if ($event.keyCode !== BACKSPACE_KEY && !ITEM_ID_PATTERN.test(inputChar) ||
      (inputChar === '0' && $event.target.value.length === 0)) {
      $event.preventDefault();
    }
  }

  onItemIdKeyUp($event, row) {
    row.itemId = $event.target.value;
    this.markupGridDetails.isMarkupSaved = false;
  }

  onUnitChange(event, row) {
    const selectedUnit = event.target.value;
    row.unit = selectedUnit;
    if (selectedUnit === UNIT_TYPES.PERCENT) {
      row.markupType = MARKUP_TYPES.SELL_UNIT;
    }
    this.onItemLevelUnitChangeVal.emit(row.unit);
    this.markupGridDetails.isMarkupSaved = false;
    this.editItemLevelGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onDeleteItemLevelMarkup($element) {
    this.itemLevelDeleteModel.itemDescription = $element.getAttribute('itemDesc');
    this.itemLevelDeleteModel.itemNo = $element.getAttribute('itemId');
    this.itemLevelDeleteModel.itemIndex = $element.getAttribute('rowIndex');
    this.itemLevelDeleteInfo.emit(this.itemLevelDeleteModel);
  }

  isMarkupUnitDisable(row: ItemLevelMarkupModel) {
    if ((this.isFurtheranceMode && row.isItemSaved) || this.isMarkupUnitDisabled) {
      return true;
    } else {
    return false;
    }
  }

  onItemLevelTipContent() {
    $('#markuptipContent').modal('show');
  }

  onEditFutureItem(rowDetails: ItemLevelMarkupModel) {
    this.currentItemDesc = rowDetails.itemDesc;
    this.subscription.push(this.furtheranceService.fetchMappedItemsForFurtherance(this.contractPriceProfileId,
      this.markupGridDetails.gfsCustomerId, this.markupGridDetails.gfsCustomerType, rowDetails.itemDesc)
      .subscribe(itemDetails => {
        this.itemDetails = itemDetails;
        this.itemAssignmentList = itemDetails.itemAssignmentList;
        this.disableSaveForFurtherance = true;
        $('#futureItemAssignmentModal' + this.exceptionIndex).modal('show');
        $('#futureItemAssignmentModal' + this.exceptionIndex).on('shown.bs.modal', () => {
          this.saveAssignmentsBtn.nativeElement.focus();
        });
      }));
  }

  buildFutureItemDetailsForFurtherance() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.saveItemDetailsForFurtherance.contractPriceProfileSeq = this.contractPriceProfileId;
    this.saveItemDetailsForFurtherance.cppFurtheranceSeq = contractDetails.cppFurtheranceSeq;
    this.saveItemDetailsForFurtherance.futureItemDesc = this.currentItemDesc;
    this.saveItemDetailsForFurtherance.gfsCustomerId = this.markupGridDetails.gfsCustomerId;
    this.saveItemDetailsForFurtherance.gfsCustomerTypeCode = this.markupGridDetails.gfsCustomerType;
    this.saveItemDetailsForFurtherance.itemAssignmentList = this.itemAssignmentList
          .filter( itemDetails => !itemDetails.isItemSaved && itemDetails.itemDescription !== '' );
    this.saveItemAssignmentsForFurtherance(this.saveItemDetailsForFurtherance);
  }

  saveItemAssignmentsForFurtherance(itemDetails: ItemDetailsForFurtheranceModel) {
    const saveAssignmentSuccess = this.translatorService.translate('TOASTER_MESSAGES.ITEM_ASSIGNMENT_SAVE_SUCCESS');
    this.furtheranceService.saveMappedItemsForFurtherance(itemDetails).subscribe( itemAssigned => {
      if ( itemAssigned.errorCode === DUPLICATE_ITEM_ERROR_CODE ) {
        this.duplicateOnSaveFound = true;
        this.duplicateList = itemAssigned.errorMessage;
      } else {
        this.duplicateOnSaveFound = false;
        $('#futureItemAssignmentModal' + this.exceptionIndex).modal('hide');
        this.toaster.showSuccess(saveAssignmentSuccess, '');
        this.itemAssignmentList.forEach(item => {
            if (item.itemId && !item.isItemSaved) {
              item.isItemSaved = true;
            }
          });
        }
      });
  }

  addAnotherItem() {
    this.itemAssignmentList.push({
      'itemId': '',
      'itemDescription': '',
      'isItemSaved': false,
      'itemExists': false,
      'invalidItem': false });
  }

  onDeleteItemRow(rowIndex) {
    if (this.itemAssignmentList[rowIndex].isItemSaved) {
      this.futureItemDeleteIndex = rowIndex;
      this.deleteItemTitle = this.itemAssignmentList[rowIndex].itemDescription;
      $('#itemAssignmentDeleteModalForMarkup' + this.exceptionIndex).modal('show');
    } else {
        this.itemAssignmentList.splice(rowIndex, 1);
    }
  }

  deleteItemForFurtherance() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    const cppFurtheranceSeq = contractDetails.cppFurtheranceSeq;
    const itemAssignmentDeleteMsg = this.translatorService.translate('TOASTER_MESSAGES.ITEM_ASSIGNMENT_DELETE_SUCCESS');
    this.subscription.push(
      this.furtheranceService.deleteItemAssignmentForFurtherance
      (String(this.itemDetails.contractPriceProfileSeq), cppFurtheranceSeq, this.itemDetails.gfsCustomerId,
        String(this.itemDetails.gfsCustomerTypeCode), this.itemAssignmentList[this.futureItemDeleteIndex].itemId,
         this.itemDetails.futureItemDesc).subscribe(() => {
          this.itemAssignmentList.splice(this.futureItemDeleteIndex, 1);
          $('#futureItemAssignmentModal' + this.exceptionIndex).modal('show');
          this.toaster.showSuccess(itemAssignmentDeleteMsg, '');
        }));
  }

  disableSaveAssignmentForMarkup() {
    if (this.itemAssignmentList.some(
      itemDetails => itemDetails.invalidItem
    ) || this.itemAssignmentList.some(
      itemDetails => itemDetails.itemExists
    ) || this.itemAssignmentList.some(
      itemDetails => !itemDetails.itemDescription
    ) || this.itemAssignmentList.every(
       itemDetails => itemDetails.isItemSaved
    )) {
        this.disableSaveAssignment = true;
        return;
    } else {
        this.disableSaveAssignment = false;
    }
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }
}
