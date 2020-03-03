import { Component, OnInit, Input, DoCheck, Output, EventEmitter, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { ItemAssignmentModel, FutureItemModel } from '../../item-assignments.model';
import { MarkupService, IMAGE_PATH, BACKSPACE_KEY, ENTER_KEY, ItemAssignmentService,
   TranslatorService, ToasterService } from '../../../../shared';
declare var $;
const ITEM_ID_PATTERN = /[0-9]/;

@Component({
  selector: 'app-item-search',
  templateUrl: './item-search.component.html',
  styleUrls: ['./item-search.component.scss']
})
export class ItemSearchComponent implements OnInit, DoCheck, OnDestroy {

  @ViewChild('deleteItemAssignmentBtn') public deleteItemAssignmentBtn: ElementRef;

  @Input() itemData: ItemAssignmentModel;
  @Input() rows: number;
  @Input() itemDetails: FutureItemModel;
  @Input() itemMapKey;
  @Input() index: number;
  @Input() onItemLevelMarkup = false;
  @Input() modalIndex = 0;
  @Input() isViewMode: boolean;
  @Output() deleteField = new EventEmitter<number>();
  public imageDir = IMAGE_PATH;
  public subscription: Subscription[] = [];
  public itemId: string;
  public invalid: boolean;
  public inactive: boolean;
  public allowClear: boolean;


  constructor(
    public itemAssignmentService: ItemAssignmentService,
    public markupService: MarkupService,
    public translatorService: TranslatorService,
    public toaster: ToasterService
  ) { }

  ngOnInit() {
    this.itemId = this.itemData.itemId;
  }

  findItemDescription() {
    this.itemData.itemId = this.itemId;
    if (this.itemId && !this.itemData.isItemSaved) {
      this.subscription.push(this.itemAssignmentService.fetchItemDescriptionForAssignment(this.itemId.trim(),
            this.itemDetails.gfsCustomerId, String(this.itemDetails.gfsCustomerTypeCode), String(this.itemDetails.contractPriceProfileSeq))
        .subscribe(itemDescDetails => {
          if ( !itemDescDetails.isItemAlreadyExist  ) {
            this.itemData.itemExists = false;
            if (!itemDescDetails.isValid) {
              this.invalid = true;
              this.itemData.itemDescription = null;
              this.itemData.invalidItem = true;
         } else {
            this.itemData.itemDescription = itemDescDetails.itemDescription;
            this.itemData.isItemSaved = false;
            this.invalid = false;
            this.itemData.invalidItem = false;
            this.itemDetails.isFutureItemSaved = false;
          }
        } else {
           this.itemData.invalidItem  = false;
           this.itemData.itemExists = true;
           this.itemData.itemDescription = null;
        }}));
    }
  }

  deleteSearchField() {
    this.onItemLevelMarkup ? this.deleteField.emit(this.index) : this.deleteField.emit(this.itemMapKey);
  }

  isFindItemGrayOut() {
    return (this.itemId) ? false : true
  }

  ngDoCheck() {
    this.allowClear = (this.rows > 1) ? true : false;
  }

  onItemKeyPress($event) {
    const inputChar = String.fromCharCode($event.charCode);
    if ($event.keyCode !== BACKSPACE_KEY && $event.keyCode !== ENTER_KEY
      &&  !ITEM_ID_PATTERN.test(inputChar) || $event.target.value.length > 8) {
      $event.preventDefault();
    }
  }

  onBlurItemId($event) {
    this.itemId = ($event.target.value).trim();
  }

  onKeyupItemId($event) {
    this.itemId = ($event.target.value).trim();
    this.itemData.itemDescription = null;
  }

  itemAssignmentDeleteModal() {
    if (this.onItemLevelMarkup) {
      $('#futureItemAssignmentModal' + this.modalIndex).modal('hide');
      this.deleteField.emit(this.index);
    } else {
      $('#itemAssignmentDeleteModal' + this.index).modal('show');
      $('#itemAssignmentDeleteModal' + this.index).on('shown.bs.modal', () => {
        this.deleteItemAssignmentBtn.nativeElement.focus();
      });
    }
  }

  showHideTrashIcon() {
    if ((this.itemData.isItemSaved && this.onItemLevelMarkup) || (this.itemData.isItemSaved && !this.isViewMode)) {
      return true;
    } else {
      return false;
    }
  }

  showHideClearIcon() {
    if ((this.allowClear && !this.itemData.isItemSaved) && (this.onItemLevelMarkup || !this.isViewMode)) {
      return true;
    } else {
      return false;
    }
  }

  onDeleteItemAssignment() {
    const itemAssignmentDeleteMsg = this.translatorService.translate('TOASTER_MESSAGES.ITEM_ASSIGNMENT_DELETE_SUCCESS');
    this.subscription.push(
      this.itemAssignmentService.deleteItemAssignment(String(this.itemDetails.contractPriceProfileSeq), this.itemDetails.gfsCustomerId,
        String(this.itemDetails.gfsCustomerTypeCode), this.itemData.itemId, this.itemDetails.futureItemDesc).subscribe(() => {
          this.onItemLevelMarkup ? this.deleteField.emit(this.index) : this.deleteField.emit(this.itemMapKey);
          this.toaster.showSuccess(itemAssignmentDeleteMsg, '');
        }));
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}
