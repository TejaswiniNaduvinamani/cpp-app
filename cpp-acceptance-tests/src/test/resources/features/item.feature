Feature: Item

	As a Contract Pricing Profile Application user
	I should be able to get the item information 
	for given item id

Scenario: Get the item information of an item for new contract
 		Given all pricing information for the contract is completed
		When a request is made to get item information 
		Then the item information is returned
		
Scenario: Validate find item information for item assignment
 		Given all pricing information for the contract is completed
		When a request is made to get item information for item assignment
		Then the item already exist in database 