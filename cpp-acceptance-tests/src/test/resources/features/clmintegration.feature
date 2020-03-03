@clm_token-setup
Feature: Clm Integration
  
  As a Integration process
  When a action message received from CLM. 
  Status should be updated in CPP.

  Scenario: Update Cpp Url When the Contract Is Created
    When the message with action "PUBLISHED" is sent for pricing contract type
    Then cpp url is updated in the contract

  Scenario: Update Cpp Url When the Amendment Is Created With Amendment Pricing Selected
    Given the amendment is created in CLM
    And pricing applicable for amendment is set to "Yes"
    When the message with action "AMENDMENT_PUBLISHED" is sent for amendment pricing contract type
    Then amendment cpp url is updated in the amendment contract

  Scenario: Do not Update Cpp Url When the Amendment Is Created With Amendment Pricing not Selected
    Given the amendment is created in CLM
    And pricing applicable for amendment is set to "No"
    When the message with action "AMENDMENT_PUBLISHED" is sent for amendment pricing contract type
    Then cpp url is not updated in the contract

  Scenario: Do not Update Cpp Url When the Contract Is Created and pricing is not required
    Given the contract is created as pricing not required
    When the message with action "PUBLISHED" is sent for pricing contract type
    Then cpp url is not updated in the contract

  Scenario: Do not Update Cpp Url When the Contract type is not pricingtype
    When the message with action "PUBLISHED" is sent for non pricing contract type
    Then cpp url is not updated in the contract

  Scenario: Update Furtherance Url When the contract is excuted
    Given request is made to save the contract price
    And the status of contract is in "PRICING_ACTIVATED"
    And the message with action "EXECUTED" is sent for pricing contract type
    Then furtherance cpp url is updated in the contract

  Scenario Outline: <line> Update CPP Status to In Sync with CLM Status when action message received
    Given request is made to save the contract price
    And the status of contract is in <current_cpp_status>
    When the current status of contract in CLM is <clm_status>
    And the message with action <action> is sent for pricing contract type
    Then status of contract updated as <updated_cpp_status>

    Examples: 
      | line | current_cpp_status     | action                | clm_status             | updated_cpp_status     |
      |    1 | "DRAFT"                | "SENT_FOR_APPROVAL"   | "WAITING_FOR_APPROVAL" | "WAITING_FOR_APPROVAL" |
      |    2 | "WAITING_FOR_APPROVAL" | "APPROVED"            | "APPROVED"             | "CONTRACT_APPROVED"    |
      |    3 | "WAITING_FOR_APPROVAL" | "UPDATED"             | "DRAFT"                | "DRAFT"                |
      |    4 | "CONTRACT_APPROVED"    | "UPDATED"             | "DRAFT"                | "DRAFT"                |
      |    5 | "DRAFT"                | "CANCELLED"           | "CANCELLED"            | "CANCELLED"            |
      |    6 | "WAITING_FOR_APPROVAL" | "CANCELLED"           | "CANCELLED"            | "CANCELLED"            |
      |    7 | "CONTRACT_APPROVED"    | "CANCELLED"           | "CANCELLED"            | "CANCELLED"            |
      |    8 | "CANCELLED"            | "REVERT_CANCELLATION" | "DRAFT"                | "DRAFT"                |
      |    9 | "CANCELLED"            | "REVERT_CANCELLATION" | "WAITING_FOR_APPROVAL" | "WAITING_FOR_APPROVAL" |
      |   10 | "CANCELLED"            | "REVERT_CANCELLATION" | "APPROVED"             | "CONTRACT_APPROVED"    |
      |   11 | "DRAFT"                | "ON_HOLD"             | "ON_HOLD"              | "HOLD"                 |
      |   12 | "WAITING_FOR_APPROVAL" | "ON_HOLD"             | "ON_HOLD"              | "HOLD"                 |
      |   13 | "CONTRACT_APPROVED"    | "ON_HOLD"             | "ON_HOLD"              | "HOLD"                 |
      |   14 | "DRAFT"                | "SENT_FOR_REVIEW"     | "REVIEW_PENDING"       | "HOLD"                 |
      |   15 | "WAITING_FOR_APPROVAL" | "SENT_FOR_REVIEW"     | "REVIEW_PENDING"       | "HOLD"                 |
      |   16 | "CONTRACT_APPROVED"    | "SENT_FOR_REVIEW"     | "REVIEW_PENDING"       | "HOLD"                 |
      |   17 | "HOLD"                 | "REVERT_ON_HOLD"      | "DRAFT"                | "DRAFT"                |
      |   18 | "HOLD"                 | "REVERT_ON_HOLD"      | "WAITING_FOR_APPROVAL" | "WAITING_FOR_APPROVAL" |
      |   19 | "HOLD"                 | "REVERT_ON_HOLD"      | "APPROVED"             | "CONTRACT_APPROVED"    |
      |   20 | "CONTRACT_APPROVED"    | "TERMINATED"          | "TERMINATED"           | "TERMINATED"           |
      |   21 | "CONTRACT_APPROVED"    | "EXPIRED"             | "EXPIRED"              | "EXPIRED"              |

  Scenario: Update Status to Deleted when contract is DELETED
    Given request is made to save the contract price
    When the message with action "DELETED" is sent for pricing contract type
    Then status of contract updated as "DELETED"

  Scenario Outline: <line> Expire all price when price activated contract is <new_cpp_status>
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    Then status of contract updated as "PRICING_ACTIVATED"
    When the current status of contract in CLM is <clm_status>
    And the message with action <clm_action> is sent for pricing contract type
    Then status of contract updated as <new_cpp_status>
    Then all pricing data for the contract is expired

    Examples: 
      | line | clm_status   | clm_action   | new_cpp_status |
      |    1 | "TERMINATED" | "TERMINATED" | "TERMINATED"   |
      |    2 | "CANCELLED"  | "CANCELLED"  | "CANCELLED"    |
      |    3 | "DELETED"    | "DELETED"    | "DELETED"      |

  Scenario Outline: Do terminate latest version when contract is terminated.
    Given request is made to save new contract price with date range as 30 days ago and 13 days from now
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    And request is made to activate pricing
    And status of amendment in CLM is "DRAFT"
    And amendment effective date in CLM is set to 13 days from now
    And contract expiration date in CLM is set to 60 days from now
    And request is sent to launch the amendment in CPP
    And the status of amendment in CPP is "CONTRACT_APPROVED"
    And status of amendment in CLM is "EXECUTED"
    When request is made to activate pricing for amendment
    Then status of amendment updated as "PRICING_ACTIVATED"
    When the current status of contract in CLM is <clm_status>
    And status of amendment in CLM is <amendment_clm_status>
    And the message with action <clm_action> is sent for pricing contract type
    Then status of contract updated as <new_cpp_status>
    And status of amendment updated as <amendment_new_cpp_status>
    And all pricing data for the contract amendment <is_is_not> expired

    Examples: 
      | line | clm_status   | amendment_clm_status | clm_action   | new_cpp_status | amendment_new_cpp_status | is_is_not |
      |    1 | "TERMINATED" | "TERMINATED"         | "TERMINATED" | "TERMINATED"   | "TERMINATED"             | is        |
      |    2 | "EXPIRED"    | "EXPIRED"            | "EXPIRED"    | "EXPIRED"      | "EXPIRED"                | is        |
      |    2 | "EXPIRED"    | "EXECUTED"           | "EXPIRED"    | "EXPIRED"      | "PRICING_ACTIVATED"      | is not    |

  Scenario Outline: <line> Do Revert Cancellation for Previous Amendment when new Amendment in <new_amendment_cpp_status>.
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment for "DEFAULT_CMG_CUSTOMER" with real customer "REAL_CUSTOMER"
    When request is made to activate pricing
    Then the status of contract is in "PRICING_ACTIVATED"
    When an amendment is created in CLM with current status as "DRAFT"
    And request is sent to launch the amendment in CPP
    Then status of amendment updated as "DRAFT"
    When status of amendment in CLM is "CANCELLED"
    And the message with action "CANCELLED" is sent for amendment pricing contract type
    Then status of amendment updated as "CANCELLED"
    When an amendment "AMENDMENT2" is created in CLM with current status as "DRAFT"
    And request is sent to launch the amendment "AMENDMENT2" in CPP
    And the status of amendment "AMENDMENT2" in CPP is <new_amendment_cpp_status>
    When status of amendment in CLM is "DRAFT"
    And the message with action "REVERT_CANCELLATION" is sent for amendment pricing contract type
    Then status of amendment updated as <old_amendment_cpp_status>

    Examples: 
      | line | new_amendment_cpp_status | old_amendment_cpp_status |
      |    1 | "DRAFT"                  | "CANCELLED"              |
      |    2 | "WAITING_FOR_APPROVAL"   | "CANCELLED"              |
      |    3 | "CONTRACT_APPROVED"      | "CANCELLED"              |
      |    4 | "HOLD"                   | "CANCELLED"              |
      |    4 | "TERMINATED"             | "DRAFT"                  |
      |    2 | "EXPIRED"                | "DRAFT"                  |
      |    3 | "CANCELLED"              | "DRAFT"                  |
      |    4 | "DELETED"                | "DRAFT"                  |

  Scenario: Update CMG name when contract name is changed
    Given request is made to save the contract price
    And the contract name changed in CLM
    When the message with action "UPDATED" is sent for pricing contract type
    Then CMG name is updated

  Scenario: Update CMG name when contract name is changed
    Given request is made to save the contract price
    And the contract name changed in CLM
    When the message with action "UPDATED" is sent for pricing contract type
    Then CMG name is updated
