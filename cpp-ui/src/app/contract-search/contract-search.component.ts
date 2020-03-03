import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';

import { AssignmentsService, AuthorizationService, ContractSearchService, ENTER_KEY } from 'app/shared';
import { ContractSearchGridModel } from 'app/contract-search/contract-search.model';

const CPP_ID_PATTERN = /[0-9]/;
const CONTRACT_NAME = 'contractName';
const CUSTOMER_TYPE = 'customerType';
const CPP_ID = 'cppId';

@Component({
  selector: 'app-contract-search',
  templateUrl: './contract-search.component.html',
  styleUrls: ['./contract-search.component.scss']
})
export class ContractSearchComponent implements OnInit, OnDestroy {

  private subscription: Subscription[] = [];
  public customerSearchForm: FormGroup;
  public customerTypes = [];
  public contractName: string;
  public customerId: string;
  public cppId: string;
  public selectedCustomerType: number;
  public contractSearchResponse: ContractSearchGridModel[];
  public disableFind: boolean;
  public noResultIndicator: boolean;
  public enterSearchIndicator: boolean;
  public isViewOnlyUser: boolean;
  public cppUrl: string;
  public showSpinner: boolean;

  constructor(
    private _contractSearchService: ContractSearchService,
    private _formBuilder: FormBuilder,
    private assignmentService: AssignmentsService,
    private authorizationService: AuthorizationService,
    private cdr: ChangeDetectorRef,
  ) { }

  ngOnInit() {
    this._contractSearchService.checkContractSearchPage(true);
    this.loadForm();
    this.fetchCustomerTypes();
    this.enterSearchIndicator = true;
    this.disableFind = true;
    this.subscription.push(
      this.authorizationService.checkForViewOnlyUser().subscribe(viewUser => {
      this.isViewOnlyUser = viewUser.isUserAuthorizedToViewOnly;
    }));
  }

  loadForm() {
    this.customerSearchForm = this._formBuilder.group({
      searchCriteriaRadio:  new FormControl('contractName'),
      contractNameSearch: new FormControl(null),
      customerTypeSearch: new FormControl(-1),
      customerIDSearch: new FormControl(null),
      cppIDSearch: new FormControl(null)
    })
  }

  fetchCustomerTypes() {
    this.subscription.push(
      this.assignmentService.fetchCustomerType().subscribe(customerTypes => {
        this.customerTypes = customerTypes;
      }));
  }

  clearAll() {
    this.customerSearchForm.reset();
    this.customerSearchForm.get('searchCriteriaRadio').setValue('contractName');
    this.disableFind = true;
  }

  onFindContract() {
    if ( !this.disableFind ) {
    const searchCriteria = this.customerSearchForm.get('searchCriteriaRadio').value;
    this.contractName = this.customerSearchForm.get('contractNameSearch').value;
    this.customerId = this.customerSearchForm.get('customerIDSearch').value;
    this.selectedCustomerType = this.customerSearchForm.get('customerTypeSearch').value;
    this.cppId = this.customerSearchForm.get('cppIDSearch').value;
    this.noResultIndicator = false;
    this.disableFind = true;
    this.contractSearchResponse = [];
    this.showSpinner = true;
    if (searchCriteria === CONTRACT_NAME) {
      this.searchByContractName();
    } else if (searchCriteria === CUSTOMER_TYPE) {
      this.searchByCustomerType();
    } else if (searchCriteria === CPP_ID) {
      this.searchByCppId();
    }
    this.cdr.detectChanges();
    }
  }


  searchByContractName() {
    this.subscription.push(
      this._contractSearchService.searchContractByContractName(this.contractName.trim())
      .subscribe (response => {
        this.contractSearchResponse = response;
        this.setPostSearchIndicators();
      }));
  }

  searchByCustomerType() {
    this.subscription.push(
      this._contractSearchService.searchContractByCustomer(this.customerId.trim(), this.selectedCustomerType)
      .subscribe (response => {
        this.contractSearchResponse = response;
        this.setPostSearchIndicators();
      }));
  }

  searchByCppId() {
    this.subscription.push(
      this._contractSearchService.searchContractByCppId(this.cppId.trim())
      .subscribe (response => {
        this.contractSearchResponse = response;
        this.setPostSearchIndicators();
      }));
  }

  setPostSearchIndicators() {
    this.enterSearchIndicator = false;
    this.showSpinner = false;
    this.disableFind = false;
    if (!this.contractSearchResponse.length) {
      this.noResultIndicator = true;
      this.enterSearchIndicator = false;
    }
  }

  clearForm() {
    this.customerSearchForm.get('contractNameSearch').setValue(null);
    this.customerSearchForm.get('customerIDSearch').setValue(null);
    this.customerSearchForm.get('customerTypeSearch').setValue(-1);
    this.customerSearchForm.get('cppIDSearch').setValue(null);
    this.disableFind = true;
  }

  isFindDisabled($event, searchCriteria) {
    if (searchCriteria === CONTRACT_NAME && $event.target.value.length < 3) {
      this.disableFind = true;
    } else if ((searchCriteria === CPP_ID || searchCriteria === CUSTOMER_TYPE) && $event.target.value.length < 1) {
      this.disableFind = true;
    } else {
      this.disableFind = false;
    }
  }

  onCppIdKeyPress($event) {
    const inputChar = String.fromCharCode($event.charCode);
    if (($event.keyCode === ENTER_KEY || !CPP_ID_PATTERN.test(inputChar))) {
      $event.preventDefault();
    }
  }

  ngOnDestroy() {
    this._contractSearchService.checkContractSearchPage(false);
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}
