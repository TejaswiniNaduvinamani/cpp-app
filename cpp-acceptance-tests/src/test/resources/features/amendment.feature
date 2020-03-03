#Author: mbharti@deloitte.com
@clm_token-setup 
Feature: Amendment to a contract 

	As a Contract Pricing Profile Application user
  	I should be able to create an Amendment on an existing Contract.

Scenario: Amendment is not created in CPP because of an existing contract which is in In-Progress state 
	Given all pricing information for the contract is completed 
	And the status of contract is in "CONTRACT_APPROVED" 
	When an amendment is created in CLM with current status as "DRAFT" 
	And request is sent to launch the amendment in CPP 
	Then an exception is returned of type IN_PROGREESS_VERSION_FOUND 
	
Scenario: Amendment is not created in CPP because of its status in CLM is not found to be in Draft 
	Given all pricing information for the contract is completed 
	And the status of contract is in "CONTRACT_APPROVED" 
	And a request is made to save assignment with real customer 
	And request is made to activate pricing 
	When an amendment is created in CLM with current status as "APPROVED" 
	And request is sent to launch the amendment in CPP 
	Then an exception is returned of type CLM_STATUS_NOT_DRAFT 
 
Scenario: 
	Amendment is not created in CPP because of existing furtherance which is in Initiated or Saved status 
	Given all pricing information for the contract is completed 
	And the status of contract is in "CONTRACT_APPROVED" 
	And a request is made to save assignment with real customer 
	And request is made to activate pricing 
	When a request is made to save furtherance information 
	And an amendment is created in CLM with current status as "DRAFT" 
	And request is sent to launch the amendment in CPP 
	Then an exception is returned of type IN_PROGRESS_FURTHERANCE_FOUND 
	
Scenario: Amendment is not created in CPP because of its effective date passing the contract end date
	Given all pricing information for the contract is completed 
	And the status of contract is in "CONTRACT_APPROVED" 
	And a request is made to save assignment with real customer 
	And request is made to activate pricing 
	When an amendment is created in CLM with effectiveDate passing contract expiration date and its status as "DRAFT" 
	And request is sent to launch the amendment in CPP 
	Then an exception is returned of type INVALID_AMENDMENT_EFFECTIVE_DATE 
		
Scenario: Amendment is successsfully created in CPP for an existing contract 
	Given all pricing information for the contract is completed 
	And the status of contract is in "CONTRACT_APPROVED" 
	And a request is made to save assignment with real customer 
	And request is made to activate pricing 
	When an amendment is created in CLM with current status as "DRAFT" 
	And request is sent to launch the amendment in CPP 
	Then amendment created successfully 
	And all the contract data of previous active version is copied 
