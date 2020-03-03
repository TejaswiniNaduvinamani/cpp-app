Feature: ActivatePricing
  
  As a Contract Pricing Profile Application user
  I should be able to activate pricing for an approved contract.

  Scenario: Validate contract status to be approved
    Given all pricing information for the contract is completed
    When request is made to activate pricing
    Then display message to user for contract is in draft status and not editable

  Scenario: Validate customer to contract mapping
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    When request is made to activate pricing
    Then display message to user for contract not ready for activation and has missing concept to customer mapping

  Scenario: Activate Pricing for a valid contract when there is no existing pricing for the customer
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When request is made to activate pricing
    Then status of contract updated as "PRICING_ACTIVATED"
    Then display message to user for activation of contract

  Scenario Outline: <line> Activate Pricing for a valid contract, when there is existing pricing for the customer
    Given there exists a contract with pricing date range as <EXIST_CUST_EFCTV_DT> and <EXIST_CUST_EXP_DT>
    And request is made to save new contract price with date range as <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When request is made to activate pricing
    Then existing customer pricing entries for "REAL_CUSTOMER" in PRC_PROF table are expired with expiration date as <EXP_DATE_TO_SET>
    Then existing customer entries for "REAL_CUSTOMER" in CIP table are expired with expiration date as <EXP_DATE_TO_SET>
    Then entries for real customer "REAL_CUSTOMER" are saved in PRC tables with effective pricing start date as <EFCTV_DATE_TO_SET>
    Then new entries for the customer "REAL_CUSTOMER" are saved to CIP tables for product type markup with effective pricing start date as <EFCTV_DATE_TO_SET>
    Then status of contract updated as "PRICING_ACTIVATED"

    Examples: 
      | line | TYPE_OF_SCENARIO                                         | CONTRACT_EFCTV_DATE | CONTRACT_EXP_DATE | EXIST_CUST_EFCTV_DT | EXIST_CUST_EXP_DT | EXP_DATE_TO_SET   | EFCTV_DATE_TO_SET |
      |    1 | "FUTURE EFFECTIVE AND EXPIRATION DATE"                   | 3 days ago          | 60 days from now  | 90 days from now    | 3 days ago        | 3 days ago        | 0 days ago        |
      |    2 | "OVERLAPPING EXPIRATION DATES"                           | 3 days ago          | 60 days from now  | 30 days ago         | 3 days from now   | 1 day ago         | 0 days ago        |
      |    3 | "FUTURE EFFECTIVE DATE WITH OVERLAPPING EXPIRATION DATE" | 3 days ago          | 60 days from now  | 90 days from now    | 7 days from now   | 7 days from now   | 0 days ago        |
      |    4 | "OVERLAPPING EFFECTIVE DATE"                             | 3 days ago          | 60 days from now  | 7 days from now     | 21 days from now  | 1 days ago        | 0 days ago        |
      |    5 | "OVERLAPPING EFFECTIVE AND EXPIRATION DATES "            | 3 days ago          | 60 days from now  | 1 days ago          | 30 days from now  | 1 days ago        | 0 days ago        |
      |    6 | "PAST DATES"                                             | 0 days ago          | 60 days from now  | 7 days ago          | 6 days ago        | 6 days ago        | 0 days ago        |
      |    7 | "OVERLAPPING EXPIRATION DATE"                            | 3 days ago          | 60 days from now  | 3 days ago          | 7 days from now   | 1 days ago        | 0 days ago        |
   
    @tobefixed
    Scenario:
      |    8 | "FUTURE EFFECTIVE AND EXPIRY DATE"                       | 1 days from now     | 60 days from now  | 90 days from now    | 120 days from now | 120 days from now | 1 days from now   |

  Scenario Outline: <line> Activate Pricing for a valid contract when there is exists an item with Bid reason code
    Given there exists contract with an item with Bid reason code for date range as <EXIST_CUST_EFCTV_DT> and <EXIST_CUST_EXP_DT>
    And request is made to save the contract price including cip entries with date range as <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When request is made to activate pricing
    Then existing bid pricing in cip for "REAL_CUSTOMER" is not expired and has expiration date as <EXIST_CUST_EXP_DT>
    Then new entries for the customer are <SAVED_OR_NOT_SAVED> to CIP tables with effective pricing as <EFCTV_DATE_TO_SET>
    Then status of contract updated as "PRICING_ACTIVATED"
    Then display message to user for activation of contract

    Examples: 
      | line | TYPE_OF_SCENARIO                                         | CONTRACT_EFCTV_DATE | CONTRACT_EXP_DATE | EXIST_CUST_EFCTV_DT | EXIST_CUST_EXP_DT | EFCTV_DATE_TO_SET | SAVED_OR_NOT_SAVED |
      |    1 | "FUTURE EFFECTIVE AND EXPIRATION DATE"                   | 3 days ago          | 60 days from now  | 90 days from now    | 3 days ago        | 0 days ago        | SAVED              |
      |    2 | "OVERLAPPING EXPIRATION DATES"                           | 3 days ago          | 60 days from now  | 30 days ago         | 3 days from now   | 0 days ago        | NOT_SAVED          |
      |    3 | "FUTURE EFFECTIVE DATE WITH OVERLAPPING EXPIRATION DATE" | 3 days ago          | 60 days from now  | 90 days from now    | 7 days from now   | 0 days ago        | SAVED              |
      |    4 | "OVERLAPPING EFFECTIVE DATE"                             | 3 days ago          | 60 days from now  | 7 days from now     | 21 days from now  | 0 days ago        | NOT_SAVED          |
      |    5 | "OVERLAPPING EFFECTIVE AND EXPIRATION DATES "            | 3 days ago          | 60 days from now  | 1 days ago          | 30 days from now  | 0 days ago        | NOT_SAVED          |
      |    6 | "PAST DATES"                                             | 0 days ago          | 60 days from now  | 7 days ago          | 6 days ago        | 0 days ago        | SAVED              |
      |    7 | "OVERLAPPING EXPIRATION DATE"                            | 3 days ago          | 60 days from now  | 3 days ago          | 7 days from now   | 0 days ago        | NOT_SAVED          |
  
   @tobefixed
    Scenario:
      |    8 | "FUTURE EFFECTIVE AND EXPIRY DATE"                       | 1 days from now     | 60 days from now  | 90 days from now    | 120 days from now | 1 days from now   | SAVED              |

  Scenario Outline: <line> Activate pricing exception based on contract status
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    And a request is made to save assignment with real customer
    When request is made to activate pricing
    Then an exception is returned from the request
    And the exception type is <error_type>

    Examples: 
      | line | current_cpp_status     | error_type         |
      |    1 | "DRAFT"                | "INVALID_CONTRACT" |
      |    2 | "WAITING_FOR_APPROVAL" | "INVALID_CONTRACT" |
      |    3 | "HOLD"                 | "INVALID_CONTRACT" |
      |    4 | "PRICING_ACTIVATED"    | "INVALID_CONTRACT" |
      |    5 | "TERMINATED"           | "INVALID_CONTRACT" |
      |    6 | "CANCELLED"            | "INVALID_CONTRACT" |
      |    7 | "DELETED"              | "INVALID_CONTRACT" |

  Scenario: Activate pricing no exception returned on contract approved status
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When request is made to activate pricing
    Then an exception is not returned from the request

  Scenario: Activate Pricing is enabled
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When request is made to check if activate pricing is enabled
    Then activate pricing is "ENABLED"

  Scenario: Activate Pricing is disabled
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    When request is made to check if activate pricing is enabled
    Then activate pricing is "DISABLED"

  Scenario: Activate Pricing is disabled
    Given request is made to save new contract price with date range as 7 days ago and 2 days ago
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When request is made to check if activate pricing is enabled
    Then activate pricing is "DISABLED"

  @clm_token-setup
  Scenario: Pricing not activated for an amendment because of its status in CLM is not EXECUTED
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And status of amendment in CLM is "DRAFT"
    And request is sent to launch the amendment in CPP
    When request is made to activate pricing for amendment
    Then an exception message is displayed for CLM status not in Executed state

  @clm_token-setup
  Scenario Outline: Successfully activate Pricing for an amendment
    Given request is made to save new contract price with date range as 30 days ago and 13 days from now
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    And request is made to activate pricing
    And status of amendment in CLM is "DRAFT"
    And amendment effective date in CLM is set to 13 days from now
    And contract expiration date in CLM is set to 60 days from now 
    And request is sent to launch the amendment in CPP
    And the status of amendment in CPP is "CONTRACT_APPROVED"
    And a request is made to delete assignment for amendment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    And a request is made to save assignment for amendment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER_UNIT"
    And status of amendment in CLM is <clm_status>
    When request is made to activate pricing for amendment
    Then existing customer pricing entries for "REAL_CUSTOMER" in PRC_PROF table for an pricing activated contract are expired with expiration date as 12 days from now
    Then existing customer entries for "REAL_CUSTOMER" in CIP table are expired with expiration date as 12 days from now
    Then entries for real customer "REAL_CUSTOMER_UNIT" are saved in PRC tables with effective pricing start date as 13 days from now
    Then new entries for the customer "REAL_CUSTOMER_UNIT" are saved to CIP tables for product type markup with effective pricing start date as 13 days from now
    Then status of amendment updated as "PRICING_ACTIVATED"

    Examples: 
      | line |   clm_status |              
      |    1 |   "EXECUTED" |
      |    2 |   "SUPERSEDED" |

  Scenario Outline: <line> Activate Pricing for a valid contract when there is exists a different item with Bid reason code
    Given there exists contract with an item with Bid reason code for date range as <EXIST_CUST_EFCTV_DT> and <EXIST_CUST_EXP_DT>
    And request is made to save the contract price including cip entries for "ITEM_TYPE_FOR_ASSIGNMENT" with date range as <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When request is made to activate pricing
    Then existing bid pricing in cip for "REAL_CUSTOMER" is not expired and has expiration date as <EXIST_CUST_EXP_DT>
    Then new entries for the customer for "ITEM_TYPE_FOR_ASSIGNMENT" is <SAVED_OR_NOT_SAVED> to CIP tables with effective pricing as <EFCTV_DATE_TO_SET>
    Then status of contract updated as "PRICING_ACTIVATED"
    Then display message to user for activation of contract

    Examples: 
      | line | TYPE_OF_SCENARIO                       | CONTRACT_EFCTV_DATE | CONTRACT_EXP_DATE | EXIST_CUST_EFCTV_DT | EXIST_CUST_EXP_DT | EFCTV_DATE_TO_SET | SAVED_OR_NOT_SAVED |
      |    1 | "FUTURE EFFECTIVE AND EXPIRATION DATE" | 3 days ago          | 60 days from now  | 3 days ago          | 60 days from now  | 0 days from now   | SAVED              |

  Scenario Outline: <line> Activate Pricing for a valid contract, when there is existing pricing for the customer for a different itemId which shouldnt be expired
    Given there exists a contract with item "ITEM_TYPE" with pricing date range as <EXIST_CUST_EFCTV_DT> and <EXIST_CUST_EXP_DT>
    And request is made to save the contract price including cip entries for "ITEM_TYPE_FOR_ASSIGNMENT" with date range as <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    When request is made to activate pricing
    Then existing customer pricing entries for "REAL_CUSTOMER" in PRC_PROF table are expired with expiration date as <EXP_DATE_TO_SET>
    Then existing customer entries for "REAL_CUSTOMER" in CIP table are expired with expiration date as <EXP_DATE_TO_SET>
    Then existing customer entries for "REAL_CUSTOMER" in CIP table for "ITEM_TYPE" is not expired and has expiration date as <EXIST_CUST_EXP_DT>
    Then entries for real customer "REAL_CUSTOMER" are saved in PRC tables with effective pricing start date as <EFCTV_DATE_TO_SET>
    Then new entries for the customer "REAL_CUSTOMER" are saved to CIP tables for product type markup with effective pricing start date as <EFCTV_DATE_TO_SET>
    Then new entries for the customer for "ITEM_TYPE_FOR_ASSIGNMENT" is <SAVED_OR_NOT_SAVED> to CIP tables with effective pricing as <EFCTV_DATE_TO_SET>
    Then status of contract updated as "PRICING_ACTIVATED"

    Examples: 
      | line | TYPE_OF_SCENARIO                       | CONTRACT_EFCTV_DATE | CONTRACT_EXP_DATE | EXIST_CUST_EFCTV_DT | EXIST_CUST_EXP_DT | EXP_DATE_TO_SET | EFCTV_DATE_TO_SET | SAVED_OR_NOT_SAVED |
      |    1 | "FUTURE EFFECTIVE AND EXPIRATION DATE" | 3 days ago          | 60 days from now  | 2 days ago          | 61 days from now  | 1 days ago      | 0 days ago        | SAVED              |
