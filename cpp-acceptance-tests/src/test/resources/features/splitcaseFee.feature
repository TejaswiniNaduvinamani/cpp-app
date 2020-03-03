Feature: Splitcase
  
  As a Contract Pricing Profile Application user
  I should be able to get the default and saved values of splitcase
  fee for existing and new contracts,
  and I must be able to save or update the values of splitcase.

  Scenario: Get the default values of splitcase for new contract
    Given request is made to save the contract price
    When a request is made to get the default values of splitcase
    Then the default values of splitcase is returned

  Scenario: Save the splitcase values for a contract
    Given request is made to save the contract price
    When request is made to save splitcase values
    Then the values of splitcase are saved

  Scenario: Get the splitcase values for existing contract
    Given request is made to save the contract price
    And request is made to save splitcase values
    When a request is made to get the saved values of splitcase
    Then a list of all saved values of splitcase is returned

  Scenario: Update the splitcase values for a contract
    Given request is made to save the contract price
    And request is made to save splitcase values
    When request is made to edit values of splitcase is entered
    Then the values of splitcase are updated

  Scenario Outline: <line> Save splitcase exception based on user role and contract status
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    When request is made to save splitcase values
    Then an exception is returned from the request
    And the exception type is <error_type>

      Examples: 
      | line | current_cpp_status     | error_type         |
      |    1 | "WAITING_FOR_APPROVAL" | "NOT_VALID_STATUS" |
      |    2 | "CONTRACT_APPROVED"    | "NOT_VALID_STATUS" |
      |    3 | "HOLD"                 | "NOT_VALID_STATUS" |
      |    4 | "PRICING_ACTIVATED"    | "NOT_VALID_STATUS" |
      |    5 | "TERMINATED"           | "NOT_VALID_STATUS" |
      |    6 | "CANCELLED"            | "NOT_VALID_STATUS" |
      |    7 | "DELETED"              | "NOT_VALID_STATUS" |
      
  Scenario Outline: Save splitcase no exception thrown
    Given all pricing information for the contract is completed
    And the status of contract is in "DRAFT"
    When request is made to save splitcase values
    Then an exception is not returned from the request
    
  Scenario Outline: <line> Save the splitcase values for a contract with cost/pack or price/pack selection
    Given request is made to save the contract price
    When request is made to save splitcase values with fee type <fee_type>
    Then the values of splitcase are saved with <fee_value>
    
      Examples: 
      | line | fee_type | fee_value   							|
      |    1 | "2" 			| "FULL_CASE_PRICE_DIV_PACK"|
      |    2 | "3" 			| "COST_DIV_PACK" 				  |
     
