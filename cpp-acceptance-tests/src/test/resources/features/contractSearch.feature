Feature: Search Contracts

	As a Contract Pricing Profile Application user
	I should be able to search contracts by Contract Name or by Customer or by CPP ID
	and should be able to click on the hyper link and navigate to CPP.
	 
	Scenario: Search Contract by Contract Name for which furtherance is created
		Given a request is made to save furtherance
		When a request is made to search contracts by "contract name for cucumber test"
		Then list of contracts returned with furtherance flag as Yes
	
	Scenario: Search Contract by Contract Name for which no furtherance is created
		Given all pricing information for the contract is completed
		When a request is made to search contracts by "contract name for cucumber test"
		Then list of contracts returned with furthernace flag as No
		
	Scenario: Search Contract by Contract Name when no contracts present
		When a request is made to search contracts by "contract does not exist"
		Then empty list is returned
		
	Scenario: Search Contract by Contract Name when the contract status is deleted 
		Given all pricing information for the contract is completed
		And the status of contract is in "DELETED"
		When a request is made to search contracts by "contract name for cucumber test"
		Then empty list is returned
		
	Scenario: Search Contract by Customer for which furtherance is created
		Given a request is made to save furtherance
		When a request is made to search contracts by customer "REAL_CUSTOMER"  
		Then list of contracts returned with furtherance flag as Yes
		
	Scenario: Search Contract by Customer for which no furtherance is created
		Given all pricing information for the contract is completed
		And the status of contract is in "CONTRACT_APPROVED"
		And a request is made to save assignment with real customer
		When a request is made to search contracts by customer "REAL_CUSTOMER" 
		Then list of contracts with customer name assigned returned with furthernace flag as No
	
	Scenario: Search Contract by Customer when no contracts present 
		When a request is made to search contracts by customer "REAL_CUSTOMER" 
		Then empty list is returned
		
	Scenario: Search Contract by Customer when the contract status is deleted 
		Given all pricing information for the contract is completed
		And the status of contract is in "CONTRACT_APPROVED"
		And a request is made to save assignment with real customer
		And the status of contract is in "DELETED"
		When a request is made to search contracts by customer "REAL_CUSTOMER" 
		Then empty list is returned
		
	Scenario: Search Contract by CPP ID for which furtherance is created
		Given a request is made to save furtherance
		When a request is made to search contracts by CPP ID "999999999"
		Then list of contracts returned with furtherance flag as Yes
	
	Scenario: Search Contract by CPP ID for which no furtherance is created
		Given all pricing information for the contract is completed
		When a request is made to search contracts by CPP ID "999999999"
		Then list of contracts returned with furthernace flag as No
		
	Scenario: Search Contract by CPP ID when no contracts present 
		When a request is made to search contracts by CPP ID "1234"
		Then empty list is returned
		
	Scenario: Search Contract by CPP ID when the contract status is deleted 
		Given all pricing information for the contract is completed
		And the status of contract is in "DELETED"
		When a request is made to search contracts by CPP ID "999999999"
		Then empty list is returned
	