Feature: Find Customer For Assignments
  
  As a Contract Pricing Profile Application Power user
  I should be find the customer to assign to concepts
  in Assignment Tab.

  Scenario: Get the all Markup values of Assignment tab
    Given all pricing information for the contract is completed
    When a request is made to get the default values of Pricing
    Then the Pricing value is returned

  Scenario: Get the list of valid customer types for copying pricing
    When a request is made to get the list of valid customer types
    Then the list of valid customer types is returned

  Scenario: Validate find customer for customer having inactive member
    Given all pricing information for the contract is completed
    When a request is made to find the customer "REAL_CUSTOMER_INACTIVE_MEMBER" to assign to "DEFAULT_CMG_CUSTOMER"
    Then response received from find customer with validation error "INACTIVE_CUSTOMER_FOUND"
    
  Scenario: Find the valid customer for copying pricing
    Given all pricing information for the contract is completed
    When a request is made to find the customer Pricing with valid customer id
    Then the valid customer name is returned

  Scenario: Validate find customer for multiple customer assignments on the default/contract level concept
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    When a request is made to find the customer "REAL_CUSTOMER_EXCEPTION" to assign to "DEFAULT_CMG_CUSTOMER"
    Then response received from find customer with validation error "HAS_MULTIPLE_CUSTOMER_MAPPING"

  Scenario: Validate find customer for customer already mapped to concept
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    When a request is made to find the customer "REAL_CUSTOMER" to assign to "EXCEPTION_CMG_CUSTOMER"
    Then response received from find customer with validation error "CUSTOMER_ALREADY_MAPPED_TO_CONCEPTS"

  Scenario: Validate find customer for customer hierachy
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    When a request is made to find the customer "REAL_CUSTOMER_UNIT" to assign to "EXCEPTION_CMG_CUSTOMER"
    Then response received from find customer with validation error "NOT_A_MEMBER_OF_DEFAULT_CUSTOMER"

  Scenario: Validate find customer for customer hierachy
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    And "REAL_CUSTOMER_UNIT" is member of "REAL_CUSTOMER"
    When a request is made to find the customer "REAL_CUSTOMER_UNIT" to assign to "EXCEPTION_CMG_CUSTOMER"
    Then customer name of "REAL_CUSTOMER_UNIT" is returned

  Scenario: Validate find customer for customer not mapped to default/contract level concept
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    When a request is made to find the customer "REAL_CUSTOMER" to assign to "EXCEPTION_CMG_CUSTOMER"
    Then response received from find customer with validation error "NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT"

  Scenario: Validate find customer when customer unit is mapped to default/contract level concept
    Given all pricing information for the contract is completed
    And a request is made to save markup exception
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER_UNIT"
    When a request is made to find the customer "REAL_CUSTOMER" to assign to "EXCEPTION_CMG_CUSTOMER"
    Then response received from find customer with validation error "CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE"

  Scenario: Find the invalid customer for copying pricing
    Given all pricing information for the contract is completed
    When a request is made to find the customer "REAL_CUSTOMER_INACTIVE" to assign to "DEFAULT_CMG_CUSTOMER"
    Then response received from find customer with validation error "INACTIVE_CUSTOMER_FOUND"
    And Blank customer name is returned from find customer request

  Scenario: Find the invalid customer for copying pricing
    Given all pricing information for the contract is completed
    When a request is made to find the customer "REAL_CUSTOMER_INVALID" to assign to "DEFAULT_CMG_CUSTOMER"
    Then response received from find customer with validation error "INVALID_CUSTOMER_FOUND"
    And Blank customer name is returned from find customer request

  Scenario Outline: <line> Validate find customer for customer already mapped to active contract
    Given there exists a contract with pricing date range as <EXIST_CUST_EFCTV_DT> and <EXIST_CUST_EXP_DT>
    And the status of contract is in "CONTRACT_APPROVED" for existing sequence
    And a request is made to save assignment with real customer with param <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And request is made to save new contract price with date range as <EXIST_CUST_EFCTV_DT> and <EXIST_CUST_EXP_DT>
    When a request is made to find the customer Pricing with valid customer id for new contract
    Then customer already mapped to active contract exception is thrown

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
