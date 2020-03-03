Feature: Cost Model
	As a Contract Pricing Profile Application user
	I should be able to select and save a different cost model for product type, 
	subgroup, or item during assignments before activation on Cost Model tab.
	
	Scenario: Fetch ProductTypes for Cost Model
		Given all pricing information for the contract is completed
		When a request is made to fetch product type
		Then list of Product types is returned
		
	Scenario: Fetch all active Cost Model
		Given all pricing information for the contract is completed
		When a request is made to fetch active cost model
		Then list of active cost model is returned
		
	Scenario: Update cost model for product type
		Given all pricing information for the contract is completed
		And the status of contract is in "CONTRACT_APPROVED"
		When a request is made to update cost model for product type
		Then product type will be updated with selected cost model
		
	Scenario: Update cost model for item
		Given all pricing information for the contract is completed
		And the status of contract is in "CONTRACT_APPROVED"
		When a request is made to update cost model for item
		Then item will be updated with selected cost model
		
	Scenario: Update cost model for subgroup
		Given all pricing information for the contract is completed
		And the status of contract is in "CONTRACT_APPROVED"
		When a request is made to update cost model for subgroup
		Then subgroup will be updated with selected cost model
		
	Scenario: Update cost model for product type when no change has been made
		Given all pricing information for the contract is completed
		And the status of contract is in "CONTRACT_APPROVED"
		When a request is made to update cost model with no change
		Then no product type will be updated
		
	Scenario Outline: <line>Update cost model for product type when contract status is not in draft status
		Given all pricing information for the contract is completed
		And the status of contract is in <current_cpp_status>
		When a request is made to update cost model for product type
		And the exception type is <error_type>
		
		Examples: 
      | line | current_cpp_status     | error_type         |
      |    1 | "WAITING_FOR_APPROVAL" | "NOT_VALID_STATUS" |
      |    2 | "HOLD"                 | "NOT_VALID_STATUS" |
      |    3 | "PRICING_ACTIVATED"    | "NOT_VALID_STATUS" |
      |    4 | "TERMINATED"           | "NOT_VALID_STATUS" |
      |    5 | "CANCELLED"            | "NOT_VALID_STATUS" |
      |    6 | "DELETED"              | "NOT_VALID_STATUS" |
      |	   7 | "EXPIRED"			  			| "NOT_VALID_STATUS" |
      |	   8 | "DRAFT"			  				| "NOT_VALID_STATUS" |
 