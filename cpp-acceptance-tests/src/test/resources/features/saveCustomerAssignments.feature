Feature: Save Customer Assignments
  
   As a Contract Pricing Profile Application Power user
  I should be save/delete the customer assignment to concepts
  in Assignment Tab.

  Scenario: Save assignment of concept with real customers
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    When a request is made to save assignment with real customer
    Then a real customer is assigned to markup

  Scenario: Validate save customer for multiple customer assignments on the default/contract level concept
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    When a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    Then response received from save assignment with validation error "HAS_MULTIPLE_CUSTOMER_MAPPING"

  Scenario: Validate save customer for customer already mapped to concept
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    When a request is made to save assignment for "EXCEPTION_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    Then response received from save assignment with validation error "CUSTOMER_ALREADY_MAPPED_TO_CONCEPTS"

  Scenario: Validate save customer not mapped to default/contract level concept
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    When a request is made to save assignment for "EXCEPTION_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    Then response received from save assignment with validation error "NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT"

  Scenario: Validate that real customer is in hieararchy
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    And "REAL_CUSTOMER_UNIT" is member of "REAL_CUSTOMER"
    When a request is made to save assignment for "EXCEPTION_CMG_CUSTOMER" with real customer "REAL_CUSTOMER_UNIT"
    Then a real customer "REAL_CUSTOMER_UNIT" is assigned to markup "EXCEPTION_CMG_CUSTOMER"

  Scenario: Validate find customer for customer hierachy
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    When a request is made to save assignment for "EXCEPTION_CMG_CUSTOMER" with real customer "REAL_CUSTOMER_UNIT"
    Then response received from save assignment with validation error "NOT_A_MEMBER_OF_DEFAULT_CUSTOMER"

  Scenario: Validate save customer for customer already mapped to active contract
    Given there exists a contract with pricing date range as 3 days ago and 30 days from now
    And the status of contract is in "CONTRACT_APPROVED" for existing sequence
    And a request is made to save assignment with real customer with param 3 days ago and 30 days from now
    And request is made to save new contract price with date range as 2 days ago and 60 days from now
    And a request is made to save the markup for product for new contract
    And the status of contract is in "CONTRACT_APPROVED"
    When a request is made to save assignment with real customer 3 days ago and 30 days from now for contract
    Then customer already mapped to other contract exception is thrown for save assignment

  Scenario Outline: <line> Validate save customer for customer already mapped to active contract
    Given there exists a contract with pricing date range as <EXIST_CUST_EFCTV_DT> and <EXIST_CUST_EXP_DT>
    And the status of contract is in "CONTRACT_APPROVED" for existing sequence
    And a request is made to save assignment with real customer with param <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And request is made to save new contract price with date range as <EFCTV_DATE_TO_SET> and <EXP_DATE_TO_SET>
    And a request is made to save the markup for product for new contract
    And the status of contract is in "CONTRACT_APPROVED"
    When a request is made to save assignment with real customer <EXIST_CUST_EFCTV_DT> and <EXIST_CUST_EXP_DT> for contract
    Then customer already mapped to other contract exception is thrown for save assignment

    Examples: 
      | line | TYPE_OF_SCENARIO                                         | CONTRACT_EFCTV_DATE | CONTRACT_EXP_DATE | EXIST_CUST_EFCTV_DT | EXIST_CUST_EXP_DT | EXP_DATE_TO_SET   | EFCTV_DATE_TO_SET |
      |    1 | "FUTURE EFFECTIVE AND EXPIRATION DATE"                   | 3 days ago          | 60 days from now  | 30 days from now    | 90 days from now  | 3 days ago        | 0 days ago        |
      |    2 | "OVERLAPPING EXPIRATION DATES"                           | 3 days ago          | 60 days from now  | 30 days ago         | 3 days from now   | 1 day ago         | 0 days ago        |
      |    3 | "FUTURE EFFECTIVE DATE WITH OVERLAPPING EXPIRATION DATE" | 3 days ago          | 60 days from now  | 1 days from now     | 7 days from now   | 7 days from now   | 0 days ago        |
      |    4 | "OVERLAPPING EFFECTIVE DATE"                             | 3 days ago          | 60 days from now  | 7 days from now     | 21 days from now  | 1 days ago        | 0 days ago        |
      |    5 | "OVERLAPPING EFFECTIVE AND EXPIRATION DATES "            | 3 days ago          | 60 days from now  | 1 days ago          | 30 days from now  | 1 days ago        | 0 days ago        |
      |    6 | "PAST DATES"                                             | 0 days ago          | 60 days from now  | 7 days ago          | 6 days ago        | 6 days ago        | 0 days ago        |
      |    7 | "OVERLAPPING EXPIRATION DATE"                            | 3 days ago          | 60 days from now  | 3 days ago          | 7 days from now   | 1 days ago        | 0 days ago        |
      |    8 | "FUTURE EFFECTIVE AND EXPIRY DATE"                       | 1 days from now     | 60 days from now  | 90 days from now    | 120 days from now | 120 days from now | 1 days from now   |

  Scenario: Fetch all saved assignment list of real customers associated
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When a request is made to fetch assigned real customer list
    Then the assigned real customer list is returned

  Scenario: Delete customer mapping for default concept
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And a request is made to assign customer in exception concept
    When a request is made to delete customer mapping assigned with default concept
    Then all the customer mappings assigned with all concepts are deleted

  Scenario: Delete customer mapping for exception concept
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And a request is made to assign customer in exception concept
    When a request is made to delete customer mapping assigned with exception concept
    Then the customer mapping assigned with exception concept is deleted

  Scenario: Save assignment no exception thrown
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    When a request is made to save assignment with real customer
    Then an exception is not returned from the request

  Scenario: Delete assignment no exception thrown
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    Then an exception is not returned from the request
