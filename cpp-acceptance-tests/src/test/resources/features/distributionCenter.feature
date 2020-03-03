Feature: Distribution Center
  
  As a Contract Pricing Profile Application user
  I should be able to get the complete list of
  distribution centers for existing and new contracts,
  and I must be able to save/update selected distribution centers.

  Scenario: Get the list of Distribution Center for new contract
    When a request is made to get the list of all distribution centers
    Then a list of all distribution center is returned

  Scenario: Get the list of Distribution Center for existing contract
    Given request is made to save the contract price
    And request is made to save the distribution center for contract price
    When a request is made to get the list of saved distribution centers
    Then a list of all saved distribution center is returned

  Scenario: Save the list of Distribution Center for a contract
    Given request is made to save the contract price
    When request is made to save the distribution center for contract price
    Then the selected distribution centers are saved

  Scenario: Update the list of Distribution Center for a contract
    Given request is made to save the contract price
    And request is made to save the distribution center for contract price
    When some distribution centers are updated
    Then the selected distribution centers are saved

  Scenario: Delete the list of Distribution Center for a contract
    Given request is made to save the contract price
    And request is made to save the distribution center for contract price
    When some distribution centers are deselected
    Then the deselected distribution centers are deleted
    And new distribution centers are saved

  Scenario Outline: <line> Save distribution centers exception based on contract status
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    When request is made to save the distribution center for contract price
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

  Scenario Outline: Save distribution centers no exception thrown
    Given all pricing information for the contract is completed
    And the status of contract is in "DRAFT"
    When request is made to save the distribution center for contract price
    Then an exception is not returned from the request
