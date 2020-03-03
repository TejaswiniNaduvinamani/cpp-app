import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';

import { ContractSearchComponent } from './contract-search.component';
import { ContractSearchGridModel } from 'app/contract-search';
import { ContractSearchService, AuthorizationService, AssignmentsService, ENTER_KEY } from 'app/shared';

describe('ContractSearchComponent', () => {

  const CONTRACT_NAME_SEARCH_RESPONSE: Array<ContractSearchGridModel> = [
    new ContractSearchGridModel('Brinker International', 123, 1, 'Heinz', 'No', '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308',
    'ICMDistributionAgreementRegional',  new Date('2018-01-20'), 'Approved',
    '/furtheranceinformation?agreementId=c03c2f79-9192-4216-b098-821f88342875&contractType=ICMDistributionAgreementRegional')
  ];

  const CUSTOMER_TYPES = ['Family', 'PMG', 'SSM', 'CMG'];

  let component: ContractSearchComponent;
  let fixture: ComponentFixture<ContractSearchComponent>;
  let authService: AuthorizationService;
  let assignmentService: AssignmentsService;
  let contractSearchService: ContractSearchService;


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContractSearchComponent],
      providers: [
        ContractSearchService,
        FormBuilder,
        AuthorizationService,
        AssignmentsService
      ]
    }).overrideTemplate(ContractSearchComponent, '');

    fixture = TestBed.createComponent(ContractSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    assignmentService = TestBed.get(AssignmentsService);
    authService = TestBed.get(AuthorizationService);
    contractSearchService = TestBed.get(ContractSearchService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should check for view only user', () => {

    // GIVEN
    spyOn(authService, 'checkForViewOnlyUser').and.returnValue(Observable.of({isUserAuthorizedToViewOnly: true}));

    // WHEN
    component.ngOnInit();

    // THEN
    expect(authService.checkForViewOnlyUser).toHaveBeenCalled();
    expect(component.isViewOnlyUser).toBeTruthy();
  });

  it('should fetch customer types', () => {

    // GIVEN
    spyOn(assignmentService, 'fetchCustomerType').and.returnValue(Observable.of([CUSTOMER_TYPES]));

    // WHEN
    component.ngOnInit();

    // THEN
    expect(assignmentService.fetchCustomerType).toHaveBeenCalled();
  });

  it('should clear the form on click of clear all button', () => {
    // WHEN
    component.clearAll();

    // THEN
    expect(component.disableFind).toBeTruthy();
    expect(component.customerSearchForm.get('searchCriteriaRadio').value).toBe('contractName');
  });

  it('should find the contract when find button is clicked for contract name', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('contractName');
    component.customerSearchForm.get('contractNameSearch').setValue('brinkers');
    spyOn(contractSearchService, 'searchContractByContractName').and.returnValue(Observable.of(CONTRACT_NAME_SEARCH_RESPONSE));

    // WHEN
    component.disableFind = false;
    component.onFindContract();

    // THEN
    expect(contractSearchService.searchContractByContractName).toHaveBeenCalledWith('brinkers');
    expect(component.disableFind).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.contractSearchResponse).toBeTruthy();
  });

  it('should find the contract when find button is clicked for customer type', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('customerType');
    component.customerSearchForm.get('customerTypeSearch').setValue(22);
    component.customerSearchForm.get('customerIDSearch').setValue('2345');
    spyOn(contractSearchService, 'searchContractByCustomer').and.returnValue(Observable.of(CONTRACT_NAME_SEARCH_RESPONSE));

    // WHEN
    component.disableFind = false;
    component.onFindContract();

    // THEN
    expect(contractSearchService.searchContractByCustomer).toHaveBeenCalledWith('2345', 22);
    expect(component.disableFind).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.contractSearchResponse).toBeTruthy();
  });

  it('should find the contract when find button is clicked for cpp Id type', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('cppId');
    component.customerSearchForm.get('cppIDSearch').setValue('2345');
    spyOn(contractSearchService, 'searchContractByCppId').and.returnValue(Observable.of(CONTRACT_NAME_SEARCH_RESPONSE));

    // WHEN
    component.disableFind = false;
    component.onFindContract();

    // THEN
    expect(contractSearchService.searchContractByCppId).toHaveBeenCalledWith('2345');
    expect(component.disableFind).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.contractSearchResponse).toBeTruthy();
  });

  it('should enable find button when contract name length is greater than three', () => {
    // GIVEN
    const event = { 'target': { 'value': 'Brinker' } };

    // WHEN
    component.isFindDisabled(event, 'contractName');

    // THEN
    expect(component.disableFind).toBeFalsy();
  });

  it('should keep find Button disabled when contract name search field is empty', () => {
    // GIVEN
    const event = { 'target': { 'value': 'Br' } };

    // WHEN
    component.isFindDisabled(event, 'contractName');

    // THEN
    expect(component.disableFind).toBeTruthy();
  });

  it('should keep find Button disabled when customer type search field is empty', () => {
    // GIVEN
    const event = { 'target': { 'value': '' } };

    // WHEN
    component.isFindDisabled(event, 'customerType');

    // THEN
    expect(component.disableFind).toBeTruthy();
  });

  it('should keep find Button disabled when cpp Id search field is empty', () => {
    // GIVEN
    const event = { 'target': { 'value': '' } };

    // WHEN
    component.isFindDisabled(event, 'cppId');

    // THEN
    expect(component.disableFind).toBeTruthy();
  });

  it('should clear the form when radio button selection is changed', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('customerType');

    // WHEN
    component.clearForm();

    // THEN
    expect(component.customerSearchForm.get('searchCriteriaRadio').value).toBe('customerType');
    expect(component.customerSearchForm.get('customerIDSearch').value).toBe(null);
    expect(component.customerSearchForm.get('customerTypeSearch').value).toBe(-1);
    expect(component.customerSearchForm.get('cppIDSearch').value).toBe(null);
  });

  it('should search contracts by contract name when contract name radio button is selected', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('contractName');
    component.contractName = 'Brinkers';
    component.noResultIndicator = false;
    component.disableFind = true;
    spyOn(contractSearchService, 'searchContractByContractName').and.returnValue(Observable.of(CONTRACT_NAME_SEARCH_RESPONSE));

    // WHEN
    component.searchByContractName();

    // THEN
    expect(contractSearchService.searchContractByContractName).toHaveBeenCalledWith('Brinkers');
    expect(component.customerSearchForm.get('searchCriteriaRadio').value).toBe('contractName');
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.disableFind).toBeFalsy();
  });

  it('should display no results found text when no results are found on search when contract name radio button is selected', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('contractName');
    component.contractName = 'Brinkers';
    component.noResultIndicator = false;
    component.disableFind = true;
    spyOn(contractSearchService, 'searchContractByContractName').and.returnValue(Observable.of([]));

    // WHEN
    component.searchByContractName();

    // THEN
    expect(contractSearchService.searchContractByContractName).toHaveBeenCalledWith('Brinkers');
    expect(component.customerSearchForm.get('searchCriteriaRadio').value).toBe('contractName');
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.disableFind).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.noResultIndicator).toBeTruthy();
  });

  it('should search contracts by cpp Id when cpp Id radio button is selected', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('cppId');
    component.cppId = '1234';
    component.noResultIndicator = false;
    component.disableFind = true;
    spyOn(contractSearchService, 'searchContractByCppId').and.returnValue(Observable.of(CONTRACT_NAME_SEARCH_RESPONSE));

    // WHEN
    component.searchByCppId();

    // THEN
    expect(contractSearchService.searchContractByCppId).toHaveBeenCalledWith('1234');
    expect(component.customerSearchForm.get('searchCriteriaRadio').value).toBe('cppId');
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.disableFind).toBeFalsy();
  });

  it('should display no results found text when no results are found on search by cpp Id', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('cppId');
    component.cppId = '1234';
    component.noResultIndicator = false;
    component.disableFind = true;
    spyOn(contractSearchService, 'searchContractByCppId').and.returnValue(Observable.of([]));

    // WHEN
    component.searchByCppId();

    // THEN
    expect(contractSearchService.searchContractByCppId).toHaveBeenCalledWith('1234');
    expect(component.customerSearchForm.get('searchCriteriaRadio').value).toBe('cppId');
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.disableFind).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.noResultIndicator).toBeTruthy();
  });

  it('should search contracts by customer Type and customer Id when customer type radio button is selected', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('customerType');
    component.selectedCustomerType = 12;
    component.customerId = '6524'
    component.noResultIndicator = false;
    component.disableFind = true;
    spyOn(contractSearchService, 'searchContractByCustomer').and.returnValue(Observable.of(CONTRACT_NAME_SEARCH_RESPONSE));

    // WHEN
    component.searchByCustomerType();

    // THEN
    expect(contractSearchService.searchContractByCustomer).toHaveBeenCalledWith('6524', 12);
    expect(component.customerSearchForm.get('searchCriteriaRadio').value).toBe('customerType');
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.disableFind).toBeFalsy();
  });

  it('should display no results found text when no results are found on search by customer', () => {
    // GIVEN
    component.customerSearchForm.get('searchCriteriaRadio').setValue('customerType');
    component.selectedCustomerType = 12;
    component.customerId = '6524'
    component.noResultIndicator = false;
    component.disableFind = true;
    spyOn(contractSearchService, 'searchContractByCustomer').and.returnValue(Observable.of([]));

    // WHEN
    component.searchByCustomerType();

    // THEN
    expect(contractSearchService.searchContractByCustomer).toHaveBeenCalledWith('6524', 12);
    expect(component.customerSearchForm.get('searchCriteriaRadio').value).toBe('customerType');
    expect(component.enterSearchIndicator).toBeFalsy();
    expect(component.disableFind).toBeFalsy();
    expect(component.showSpinner).toBeFalsy();
    expect(component.noResultIndicator).toBeTruthy();
  });

});
