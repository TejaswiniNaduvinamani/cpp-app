import { Component, OnInit, Input, DoCheck, Output, EventEmitter, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';

import { AssignmentMarkupModel } from 'app/assignment-header/assignments/assignments.model';
import { RealCustomerModel, SaveAssignmentModel } from './customer-assignments.model';
import { AssignmentsService, ToasterService, TranslatorService,
   VALIDATION_CODE, DUPLICATE_VALIDATION_CODE,  IMAGE_PATH } from '../../../shared';
declare var $;

@Component({
  selector: 'app-customer-assignments',
  templateUrl: './customer-assignments.component.html',
  styleUrls: ['./customer-assignments.component.scss']
})
export class CustomerAssignmentsComponent implements OnInit, DoCheck, OnDestroy  {

  @ViewChild('saveCustomerAssignmentBtn') public saveCustomerAssignmentBtn: ElementRef;

  @Input() assignmentData: AssignmentMarkupModel;
  @Input() customerTypes = [];
  @Input() index: number;
  @Input() isViewMode: boolean;
  @Output() defaultAssignmentDelete = new EventEmitter<boolean>();
  @Output() enableActivatePricing = new EventEmitter<boolean>();
  private subscription: Subscription[] = [];
  public gridMapKeyList = [];
  public customerInd = 1;
  public duplicateCustomerInd = false;
  public realCustomerListLength: number;
  public selectedRealCustomerInd: number;
  public selectedRealCustomerList = [];
  public gridMap = new Map();
  public checkDuplicateList = [];
  public checkDuplicateCustomer = [];
  public disableSaveInd = true;
  public customerTypeList = [];
  public saveAssignment = <SaveAssignmentModel>{};
  public realCustomerSaveList: RealCustomerModel[] = [];
  public realCustomerDTOKey = [];
  public savedAssignmentList = [];
  public disableRealCustomerSearch: boolean;
  public contractPriceProfileSeq: number;
  public contractPriceProfileId: number;
  public duplicateCustomerFound: boolean;
  public clmContractStartDate: Date;
  public clmContractEndDate: Date;
  public duplicateList: string;
  public imageDir = IMAGE_PATH;
  public validationErrorInd = false;
  public validationMsg: string;
  public contractType: string;
  public memberHierarchyErrorMessage = '';

  constructor(private _assignmentService: AssignmentsService,
    private _toaster: ToasterService,
    private _translatorService: TranslatorService ) { }

  ngOnInit() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractPriceProfileId = contractDetails.cppid;
    this.contractPriceProfileSeq = contractDetails.cppseqid;
    this.clmContractStartDate = contractDetails.cstdate;
    this.clmContractEndDate = contractDetails.cenddate;
    this.contractType = contractDetails.contractType;
    this.assignmentData.realCustomerDTOList.forEach( assignedRealCustomer => {
      this.gridMap.set(this.assignmentData.markupName + String(this.customerInd), {
        realCustomerName: assignedRealCustomer.realCustomerName,
        realCustomerId: assignedRealCustomer.realCustomerId,
        realCustomerType: assignedRealCustomer.realCustomerType,
        realCustomerGroupType: assignedRealCustomer.realCustomerGroupType,
        isCustomerSaved: assignedRealCustomer.isCustomerSaved,
        duplicateCustomerFound: false,
        validationError: false
      });
      this.customerInd ++;
    }
    );
    this.gridMapKeyList = Array.from( this.gridMap.keys());
    this.realCustomerListLength = this.gridMapKeyList.length;
    this.assignCustomerTypes();
  }

  addAnother() {
    this.gridMap.set(this.assignmentData.markupName + String(this.customerInd), {
      realCustomerName: null,
      realCustomerId: null,
      realCustomerType: null,
      realCustomerGroupType: null,
      isCustomerSaved: false,
      duplicateCustomerFound: false,
      validationError: false
    });
    this.gridMapKeyList = Array.from( this.gridMap.keys());
    this.customerInd ++;
  }

  ngDoCheck() {
    this.realCustomerListLength = this.gridMapKeyList.length;
    this.disableSaveAssignment();
    this.assignCustomerTypes();
  }

  onDelete(mapKey) {
    this.gridMap.delete(mapKey);
    this.gridMapKeyList = Array.from( this.gridMap.keys());
    this.disableSaveAssignment();
    if ( !this.gridMapKeyList.length || !this.gridMapKeyList.some(mapkey => this.gridMap.get(mapkey).realCustomerName) ) {
      this.assignmentData.isAssignmentSaved = false;
    }
  }

  assignCustomerTypes() {
    this.customerTypeList = this.customerTypes;
  }

  emitPricingActivateState($event) {
    this.enableActivatePricing.emit($event);
  }

  validateForDuplicates($event) {
    this.gridMap.forEach( realCustomerDetails => {
      if ( realCustomerDetails !==  this.gridMap.get($event.key) ) {
      if ( realCustomerDetails.realCustomerId === $event.realCustomerId &&
        realCustomerDetails.realCustomerType === $event.realCustomerType ) {
        this.gridMap.get($event.key).duplicateCustomerFound = true;
        this.disableSaveInd = true;
        return true;
      }
    }
    })
  }

  disableSaveAssignment() {
    this.gridMapKeyList = Array.from( this.gridMap.keys());
    if ( this.gridMapKeyList.some(
      mapkey => this.gridMap.get(mapkey).validationError) || this.gridMapKeyList.some(
        mapkey => this.gridMap.get(mapkey).duplicateCustomerFound
    )) {
      this.disableSaveInd = true;
      return;
    }
    if ( !this.assignmentData.isAssignmentSaved && this.gridMapKeyList.every(
      mapkey => !this.gridMap.get(mapkey).realCustomerName
    )) {
            this.disableSaveInd = true;
            return;
    }
    this.disableSaveAfterRealCustomerAssignment();
  }

  disableSaveAfterRealCustomerAssignment() {
    if (this.assignmentData.isAssignmentSaved) {
      this.disableSaveInd = true;
      return;
    }
    this.disableSaveForEmptyValues();
  }

  disableSaveForEmptyValues() {
    this.checkDuplicateList = [];
    this.gridMapKeyList.forEach( elementKey => {
    this.checkDuplicateList.push(this.gridMap.get(elementKey).realCustomerName + this.gridMap.get(elementKey).realCustomerTypeId)
    })
    if (this.checkDuplicateList.length && this.checkDuplicateList[0] !== 0 ) {
      this.disableSaveInd = false;
    } else if (this.checkDuplicateList.length > 0 && this.checkDuplicateList[0] === 0) {
      let emptyFieldInd = true;
      this.checkDuplicateList.forEach(ele => {
        if (ele !== 0) {
          emptyFieldInd = false;
        }
      })
      if (emptyFieldInd === true) {
        this.disableSaveInd = true;
      } else {
        this.disableSaveInd = false;
      }
    }
  }

  saveAssignments() {
    this.buildRealCustomerObject();
    const saveAssignmentSuccess = this._translatorService.translate('TOASTER_MESSAGES.ASSIGNMENT_SAVE_SUCCESS');
    this.subscription.push(this._assignmentService.saveAssignment(this.saveAssignment).switchMap(( savedAssignment ) => {
      if ( savedAssignment.errorCode in DUPLICATE_VALIDATION_CODE || savedAssignment.errorCode in VALIDATION_CODE ) {
        this.constructValidationMessage(savedAssignment.errorCode, savedAssignment.errorMessage);
      } else {
          this.memberHierarchyErrorMessage = '';
          this.validationErrorInd = false;
          this.duplicateCustomerInd = false;
          this.saveAssignment.realCustomerDTOList.forEach(realCustomer => {
          realCustomer.isCustomerSaved = true;
        });
        this._toaster.showSuccess(saveAssignmentSuccess, '');
        this.assignmentData.isAssignmentSaved = true;
        this.realCustomerDTOKey.forEach(mapKey => {
          const  gridMapObject = this.gridMap.get(mapKey);
            if (gridMapObject.realCustomerName && !gridMapObject.isCustomerSaved) {
              this.gridMap.get(mapKey).isCustomerSaved = true;
            }
          });
          return this._assignmentService.fetchActivatePricingButtonState(String(this.contractPriceProfileSeq))
      }}).subscribe((state) => {
        this.enableActivatePricing.emit(state.enableActivatePricingButton);
      }));
    this.realCustomerSaveList = [];
  }

  constructValidationMessage(errorCode: number, errorMessage?: string) {
    const labelPrefix = 'VALIDATION_CODE.';
    if (errorCode in DUPLICATE_VALIDATION_CODE) {
      this.memberHierarchyErrorMessage = '';
      const labelSuffix = '_DUPLICATE_ON_SAVE';
      this.validationMsg = this._translatorService.translate(labelPrefix + String(errorCode) + labelSuffix);
      this.duplicateList = errorMessage;
      this.duplicateCustomerInd = true;
      this.validationErrorInd = false;
    } else if ( errorCode in VALIDATION_CODE) {
      this.memberHierarchyErrorMessage = '';
      this.validationMsg = this._translatorService.translate(labelPrefix + String(errorCode));
      this.duplicateCustomerInd = false;
      this.validationErrorInd = true;
      if ( errorCode === VALIDATION_CODE.NOT_A_MEMBER_OF_DEFAULT_CUSTOMER) {
        this.memberHierarchyErrorMessage = errorMessage;
      }
    }
  }

  buildRealCustomerObject() {
    this.saveAssignment.contractPriceProfileId = this.contractPriceProfileId;
    this.saveAssignment.contractPriceProfileSeq = this.contractPriceProfileSeq;
    this.saveAssignment.clmContractStartDate = this.clmContractStartDate;
    this.saveAssignment.clmContractEndDate = this.clmContractEndDate;
    this.saveAssignment.cmgCustomerId = this.assignmentData.gfsCustomerId;
    this.saveAssignment.cmgCustomerType = this.assignmentData.gfsCustomerTypeId;
    this.realCustomerDTOKey = Array.from( this.gridMap.keys());
    this.realCustomerDTOKey.forEach(mapKey => {
    const  gridMapObject = this.gridMap.get(mapKey);
      if (gridMapObject.realCustomerName && !gridMapObject.isCustomerSaved) {
        this.realCustomerSaveList.push(gridMapObject);
      }
    });
    this.saveAssignment.realCustomerDTOList = this.realCustomerSaveList;
  }

  detectDefaultAssignmentDelete($event) {
    this.defaultAssignmentDelete.emit($event);
  }

  createIndex(parentIndex: number) {
    const childIndex = this.cantorPair(parentIndex, this.index);
    return childIndex;
  }

  cantorPair(k1, k2) {
    return ((k1 + k2) * (k1 + k2 + 1)) / 2 + k2;
  }

  onSaveClick() {
    $('#saveAssignmentModal' + this.index).modal('show');
    $('#saveAssignmentModal' + this.index).on('shown.bs.modal', () => {
      this.saveCustomerAssignmentBtn.nativeElement.focus();
      });
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}

