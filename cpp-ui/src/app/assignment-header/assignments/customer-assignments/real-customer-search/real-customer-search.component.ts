import { Component, OnInit, Input, DoCheck, Output, EventEmitter, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { AssignmentMarkupModel } from 'app/assignment-header/assignments/assignments.model';
import { AssignmentsService } from 'app/shared/services/assignments/assignments.service';
import { DeleteRealCustomerModel } from 'app/assignment-header';
import { RealCustomerModel } from './../customer-assignments.model';

import { BACKSPACE_KEY, ENTER_KEY, IMAGE_PATH, ToasterService,
  TranslatorService, OK, DUPLICATE_VALIDATION_CODE, VALIDATION_CODE } from '../../../../shared';
declare var $;

const CUSTOMER_ID_PATTERN = /[0-9]/;

@Component({
  selector: 'app-real-customer-search',
  templateUrl: './real-customer-search.component.html',
  styleUrls: ['./real-customer-search.component.scss']
})
export class RealCustomerSearchComponent implements OnInit, DoCheck, OnDestroy {

  @ViewChild('deleteRealCustomerBtn') public deleteRealCustomerBtn: ElementRef;
  @ViewChild('deleteDefaultRealCustomerBtn') public deleteDefaultRealCustomerBtn: ElementRef;

  @Input() gridMapKey;
  @Input() assignmentDetails: AssignmentMarkupModel;
  @Input() realCustomerFields: number;
  @Input() realCustomerTypes;
  @Input() index: number;
  @Input() contractPriceProfileSeq: number;
  @Input() realCustomerData: RealCustomerModel;
  @Input() isViewMode: boolean;
  @Output() deleteField = new EventEmitter<string>();
  @Output() customerExistCheckEmitter = new EventEmitter<Object>();
  @Output() defaultAssignmentDelete = new EventEmitter<boolean>();
  @Output() enableActivatePricing = new EventEmitter<boolean>();
  selectedCustomerTypeModel = -1;
  private _subscription: Subscription[] = [];
  public customerTypes;
  public customerId: string;
  public allowClear: boolean;
  public customerName: string;
  public realCustomer: string;
  public imageDir = IMAGE_PATH;
  public duplicateCustomerFound: boolean;
  public concept: string;
  public cppId: number;
  public cppSeq: string;
  public contractType: string;
  public contractDetails;
  public validationMsg: string;

  constructor(
    private _assignmentService: AssignmentsService,
    private _toaster: ToasterService,
    private _translatorService: TranslatorService
  ) {}

  ngOnInit() {
    this.customerTypes = this.realCustomerTypes;
    this.contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.cppSeq = String(this.contractDetails.cppseqid);
    this.contractType = this.contractDetails.contractType;
    if (this.realCustomerData.isCustomerSaved) {
      this.selectedCustomerTypeModel = Number(
        this.realCustomerData.realCustomerType
      );
    }
  }

  ngDoCheck() {
    if (this.realCustomerFields > 1) {
      this.allowClear = true;
    } else {
      this.allowClear = false;
    }
    this.customerTypes = this.realCustomerTypes;
  }

  deleteSearchField() {
    this.deleteField.emit(this.gridMapKey);
  }

  onCustomerKeyPress($event) {
    const inputChar = String.fromCharCode($event.charCode);
    if (
      ($event.keyCode !== BACKSPACE_KEY &&
        $event.keyCode !== ENTER_KEY &&
        !CUSTOMER_ID_PATTERN.test(inputChar)) ||
      $event.target.value.length > 8
    ) {
      $event.preventDefault();
    }
  }

  onKeyupCustomerId($event) {
    this.customerId = ($event.target.value).trim();
    this.onDropDownChange();
  }

  onBlurCustomerId($event) {
    this.customerId = ($event.target.value).trim();
  }

  onDropDownChange() {
    this.realCustomerData.realCustomerName = null;
  }

  findRealCustomer() {
    this.realCustomerData.duplicateCustomerFound = false;
    if (!this.customerId || this.selectedCustomerTypeModel === -1 || this.realCustomerData.isCustomerSaved) {
      return;
    }
    const customerType = String(this.selectedCustomerTypeModel);
    if (this.validateForDuplicate(this.customerId, customerType)) {
      this.realCustomerData.validationError = false;
      return;
    } else {
      this._subscription.push(this._assignmentService.findRealCustomer(this.customerId, customerType,
        this.cppSeq, this.assignmentDetails.gfsCustomerId, String(this.assignmentDetails.gfsCustomerTypeId) )
          .subscribe( response => {
        this.customerName = response.customerName;
         if (response.validationCode === OK) {
          this.realCustomerData.duplicateCustomerFound = false;
          this.realCustomer = response.customerName;
          this.realCustomerData.realCustomerName = this.realCustomer;
          this.realCustomerData.realCustomerId = this.customerId;
          this.realCustomerData.isCustomerSaved = false;
          this.realCustomerData.validationError = false;
          this.realCustomerData.realCustomerType = String(this.selectedCustomerTypeModel);
          this.assignmentDetails.isAssignmentSaved = false;
          } else {
           this.constructValidationMessage(response.validationCode, response.validationMessage);
         }
      }
    ))}
  }

  validateForDuplicate(customerId: string, customerType: string): boolean {
    this.customerExistCheckEmitter.emit({
      key: this.gridMapKey,
      realCustomerId: customerId,
      realCustomerType: customerType
    });
    if (this.realCustomerData.duplicateCustomerFound) {
      this.concept = this.assignmentDetails.markupName;
      this.cppId = this.contractDetails.cppid;
      return true;
    } else {
      return false;
    }
  }

  constructValidationMessage(validationCode: number, validationMsg?: string) {
    if (validationCode in DUPLICATE_VALIDATION_CODE) {
      const validationDetails = JSON.parse(validationMsg);
      this.concept = validationDetails.conceptName;
      this.cppId = validationDetails.contractPriceProfileId;
      this.realCustomerData.duplicateCustomerFound = true;
      this.realCustomerData.validationError = false;
    } else if ( validationCode in VALIDATION_CODE) {
      const label = 'VALIDATION_CODE.';
      this.validationMsg = this._translatorService.translate(label + String(validationCode));
      this.realCustomerData.validationError = true;
      this.realCustomerData.duplicateCustomerFound = false;
    }
  }

  isFindGrayedOut() {
    if (!this.customerId || this.selectedCustomerTypeModel === -1) {
      return true;
    } else {
      return false;
    }
  }

  onDeleteClick() {
    if (!this.assignmentDetails.isDefault) {
      $('#deleteRealCustomer' + this.index).modal('show');
      $('#deleteRealCustomer' + this.index).on('shown.bs.modal', () => {
        this.deleteRealCustomerBtn.nativeElement.focus();
        });
    } else {
      $('#deleteDefaultRealCustomer').modal('show');
      $('#deleteDefaultRealCustomer').on('shown.bs.modal', () => {
        this.deleteDefaultRealCustomerBtn.nativeElement.focus();
        });
    }
  }

  deleteRealCustomer() {
    const deleteAssignmentSuccess = this._translatorService.translate('TOASTER_MESSAGES.ASSIGNMENT_DELETE_SUCCESS');
    const deleteDefaultConceptAssignmentSuccess =
     this._translatorService.translate('TOASTER_MESSAGES.ASSIGNMENT_DEFAULT_DELETE_SUCCESS');
    const deleteRealCustomer$: Observable<DeleteRealCustomerModel> = this._assignmentService.deleteRealCustomer(
      this.realCustomerData.realCustomerId,
      this.realCustomerData.realCustomerType,
      String(this.cppSeq),
      this.assignmentDetails.gfsCustomerId,
      String(this.assignmentDetails.gfsCustomerTypeId)
    );
    const fetchPricingButtonState$ = this._assignmentService.fetchActivatePricingButtonState(this.cppSeq);

    deleteRealCustomer$
      .mergeMap(deleteResponse => {
        return fetchPricingButtonState$.map(pricingState => {
          return pricingState.enableActivatePricingButton;
        });
      })
      .subscribe(enableActivatePricing => {
        this.deleteField.emit(this.gridMapKey);
        if (!this.assignmentDetails.isDefault) {
          this._toaster.showSuccess(deleteAssignmentSuccess, '');
        } else {
          this.defaultAssignmentDelete.emit(true);
          this._toaster.showSuccess(deleteDefaultConceptAssignmentSuccess, '');
        }
        this.enableActivatePricing.emit(enableActivatePricing);
      });
  }

  ngOnDestroy() {
    this._subscription.forEach(sub => sub.unsubscribe());
  }
}
