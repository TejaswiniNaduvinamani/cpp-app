import { Component, OnInit, Input, DoCheck, Output, EventEmitter, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';

import { FutureItemModel, ItemAssignmentModel } from '../item-assignments.model';
import { ItemAssignmentService, ToasterService, TranslatorService, DUPLICATE_ITEM_ERROR_CODE, IMAGE_PATH } from '../../../shared';
declare var $;

@Component({
  selector: 'app-item-display',
  templateUrl: './item-display.component.html',
  styleUrls: ['./item-display.component.scss']
})
export class ItemDisplayComponent implements OnInit, DoCheck, OnDestroy {

  @ViewChild('saveItemAssignmentBtn') public saveItemAssignmentBtn: ElementRef;

  @Input() itemAssignmentDetails: FutureItemModel;
  @Input() index: number;
  @Input() isViewMode: boolean;
  private subscription: Subscription[] = [];
  public itemMap = new Map();
  public itemMapKeyList = [];
  public itemInd = 1;
  public rows: number;
  public futureItemListLength: number;
  public disableSaveInd = true;
  public saveItem = <FutureItemModel>{};
  public itemSaveList: ItemAssignmentModel[] = [];
  public itemKey = [];
  public savedItemAssignmentList = [];
  public childIndex: number;
  public duplicateList: string;
  public duplicateOnSaveFound: boolean;
  public imageDir = IMAGE_PATH;

  constructor(private _itemAssignmentService: ItemAssignmentService,
    private _toaster: ToasterService,
    private _translatorService: TranslatorService ) { }

  ngOnInit() {
    this.itemAssignmentDetails.itemAssignmentList.forEach( assignedFutureItem => {
      this.itemMap.set(this.itemAssignmentDetails.exceptionName + this.itemAssignmentDetails.futureItemDesc + String(this.itemInd), {
        itemId: assignedFutureItem.itemId,
        itemDescription: assignedFutureItem.itemDescription,
        isItemSaved: assignedFutureItem.isItemSaved,
        invalidItem: false,
        itemExists: false
      });
      this.itemInd ++;
    });
    this.itemMapKeyList = Array.from( this.itemMap.keys());
    this.futureItemListLength = this.itemMapKeyList.length;
  }

  addAnotherItem() {
    this.itemMap.set(this.itemAssignmentDetails.exceptionName + this.itemAssignmentDetails.futureItemDesc + String(this.itemInd), {
      itemId: null,
      itemDescription: null,
      isItemSaved: false,
      invalidItem: false,
      itemExists: false
    });
    this.itemMapKeyList = Array.from( this.itemMap.keys());
    this.itemInd ++;
   }

  ngDoCheck() {
    this.futureItemListLength = this.itemMapKeyList.length;
    this.disableSaveAssignment();
    this.disableSaveAfterItemAssignment();
  }

  onDelete(mapKey) {
    this.itemMap.delete(mapKey);
    this.itemMapKeyList = Array.from( this.itemMap.keys());
    this.disableSaveAssignment();
    if (!this.itemMapKeyList.length || !this.itemMapKeyList.some(mapkey => this.itemMap.get(mapkey).itemDescription)) {
      this.itemAssignmentDetails.isFutureItemSaved = false;
    }
  }

  disableSaveAssignment() {
    this.itemMapKeyList = Array.from( this.itemMap.keys());
    if (this.itemMapKeyList.some(
      mapkey => this.itemMap.get(mapkey).invalidItem
    ) || this.itemMapKeyList.some(
      mapkey => this.itemMap.get(mapkey).itemExists
    ) || this.itemMapKeyList.some(
      mapkey => this.itemMap.get(mapkey).itemDescription === null
    )) {
        this.disableSaveInd = true;
        return;
    } else {
        this.disableSaveInd = false;
    }
    if ( !this.itemAssignmentDetails.isFutureItemSaved && this.itemMapKeyList.every(
      mapkey => !this.itemMap.get(mapkey).itemDescription
    )) {
            this.disableSaveInd = true;
            return;
    }
  }

  disableSaveAfterItemAssignment() {
    if (this.itemAssignmentDetails.isFutureItemSaved) {
      this.disableSaveInd = true;
      return;
    }
  }

  onSaveClick() {
    $('#saveItemModal' + this.index).modal('show');
    $('#saveItemModal' + this.index).on('shown.bs.modal', () => {
      this.saveItemAssignmentBtn.nativeElement.focus();
      });
  }

  saveAssignments() {
    this.buildItemObject();
    const saveAssignmentSuccess = this._translatorService.translate('TOASTER_MESSAGES.ITEM_ASSIGNMENT_SAVE_SUCCESS');
    this.subscription.push(this._itemAssignmentService.saveItemAssignment(this.saveItem).subscribe( response  => {
      if ( response.errorCode === DUPLICATE_ITEM_ERROR_CODE ) {
        this.duplicateOnSaveFound = true;
        this.duplicateList = response.errorMessage;
      } else {
        this.duplicateOnSaveFound = false;
        this._toaster.showSuccess(saveAssignmentSuccess, '');
        this.itemAssignmentDetails.isFutureItemSaved = true;
        this.itemKey.forEach(mapKey => {
          const  itemMapObject = this.itemMap.get(mapKey);
            if (itemMapObject.itemId && !itemMapObject.isItemSaved) {
              this.itemMap.get(mapKey).isItemSaved = true;
            }
          });
        }
      }));
    this.itemSaveList = [];
  }

  buildItemObject() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    const contractPriceProfileSeq = contractDetails.cppseqid;
    this.saveItem.contractPriceProfileSeq = contractPriceProfileSeq;
    this.saveItem.futureItemDesc = this.itemAssignmentDetails.futureItemDesc;
    this.saveItem.exceptionName = this.itemAssignmentDetails.exceptionName;
    this.saveItem.gfsCustomerId = this.itemAssignmentDetails.gfsCustomerId;
    this.saveItem.gfsCustomerTypeCode = this.itemAssignmentDetails.gfsCustomerTypeCode;
    this.saveItem.isFutureItemSaved = true;
    this.itemKey = Array.from( this.itemMap.keys());
    this.itemKey.forEach(mapKey => {
    const  itemMapObject = this.itemMap.get(mapKey);
      if (itemMapObject.itemId && !itemMapObject.isItemSaved) {
        this.itemSaveList.push(itemMapObject);
      }
    });
    this.saveItem.itemAssignmentList = this.itemSaveList;
  }

  createDeleteModalIndex(parentIndex: number) {
    this.childIndex = this.cantorPair(parentIndex, this.index);
    return this.childIndex;
  }

  cantorPair(k1, k2) {
    return ((k1 + k2) * (k1 + k2 + 1)) / 2 + k2;
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}
