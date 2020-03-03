Feature: Authorization Service
  
  As authorization service provider when the client request for authorization details
  service should all possible action can be performed by user.

  Scenario Outline: <line> Price Profile Editable value return based on user role and contract status
    When the user is logged in as <role> User
    And request is made to save the contract price
    And the status of contract is in <current_cpp_status>
    When the request is made to get authorization details
    Then the cpp status in authorization details returned as <status_desc>

    Examples: 
      | line | role        | current_cpp_status     | status_desc            |
      |    1 | "VIEW_USER" | "DRAFT"                | "Draft"                |
      |    2 | "VIEW_USER" | "HOLD"                 | "On Hold"              |
      |    3 | "VIEW_USER" | "WAITING_FOR_APPROVAL" | "Waiting For Approval" |
      |    4 | "VIEW_USER" | "CONTRACT_APPROVED"    | "Contract Approved"    |
      |    5 | "VIEW_USER" | "PRICING_ACTIVATED"    | "Pricing Activated"    |
      |    6 | "VIEW_USER" | "TERMINATED"           | "Terminated"           |
      |    7 | "VIEW_USER" | "CANCELLED"            | "Cancelled"            |
      |    8 | "VIEW_USER" | "DELETED"              | "Deleted"              |

  Scenario Outline: <line> Authorization Details value return based on user role and contract status
    When the user is logged in as <role> User
    And request is made to save the contract price
    And the status of contract is in <current_cpp_status>
    When the request is made to get authorization details
    Then price profile editable is returned as <price_profile_editable> in authorization details
    And customer assignment editable is returned as <assingment_editable> in authorization details
    And item assignment editable is returned as <assingment_editable> in authorization details
    And cost model editable is returned as <assingment_editable> in authorization details

    Examples: 
      | line | role         | current_cpp_status     | price_profile_editable | assingment_editable |
      |    1 | "VIEW_USER"  | "DRAFT"                | "false"                | "false"             |
      |    2 | "ACCOUNTMGR" | "DRAFT"                | "true"                 | "false"             |
      |    3 | "POWERUSER"  | "DRAFT"                | "true"                 | "false"             |
      |    4 | "ACCOUNTMGR" | "WAITING_FOR_APPROVAL" | "false"                | "false"             |
      |    5 | "POWERUSER"  | "WAITING_FOR_APPROVAL" | "false"                | "false"             |
      |    6 | "ACCOUNTMGR" | "CONTRACT_APPROVED"    | "false"                | "false"             |
      |    7 | "POWERUSER"  | "CONTRACT_APPROVED"    | "false"                | "true"              |
      |    8 | "ACCOUNTMGR" | "HOLD"                 | "false"                | "false"             |
      |    9 | "POWERUSER"  | "HOLD"                 | "false"                | "false"             |
      |   10 | "ACCOUNTMGR" | "PRICING_ACTIVATED"    | "false"                | "false"             |
      |   11 | "POWERUSER"  | "PRICING_ACTIVATED"    | "false"                | "false"             |
      |   12 | "ACCOUNTMGR" | "TERMINATED"           | "false"                | "false"             |
      |   13 | "POWERUSER"  | "TERMINATED"           | "false"                | "false"             |

  Scenario Outline: <line> Check if user Authorized To View Only
    When the user is logged in as <role> User
    When request is made to if user is authorized to view only
    Then the authorized to view only return as <true_or_false>

    Examples: 
      | line | role         | true_or_false |
      |    1 | "VIEW_USER"  | "true"        |
      |    2 | "ACCOUNTMGR" | "false"       |
      |    3 | "POWERUSER"  | "false"       |

  Scenario Outline: <line> Assignment Editable value return based on user role and contract status for amendment
    When the user is logged in as "POWERUSER" User
    And request is made to save the contract price
    And the status of contract is in "CONTRACT_APPROVED"
    When the request is made to get authorization details for amendment with clm status <clm_status>
    Then customer assignment editable is returned as <true_or_false> in authorization details
    Then item assignment editable is returned as <true_or_false> in authorization details

    Examples: 
      | line | clm_status                       | true_or_false |
      |    1 | "EXECUTED"                       | "true"        |
      |    2 | "SUPERSEDED"                     | "true"        |
      |    3 | "APPROVED"                       | "false"       |
      |    4 | "WAITING_FOR_INTERNAL_SIGNATURE" | "false"       |
      |    5 | "WAITING_FOR_EXTERNAL_SIGNATURE" | "false"       |

  Scenario Outline: <line> Assignment Editable value return based on user role and contract status for amendment
    When the user is logged in as <role> User
    And all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    When a request is made to save furtherance information
    And the status of Furtherance is in <furtherance_status>
    When the request is made to check if furtherance is editable
    Then furtherance editable is returned as <true_or_false> in authorization details

    Examples: 
      | line | role         | furtherance_status      | true_or_false |
      |    1 | "VIEW_USER"  | "FURTHERANCE_INITIATED" | "false"       |
      |    2 | "VIEW_USER"  | "FURTHERANCE_SAVED"     | "false"       |
      |    2 | "VIEW_USER"  | "FURTHERANCE_ACTIVATED" | "false"       |
      |    1 | "ACCOUNTMGR" | "FURTHERANCE_INITIATED" | "true"        |
      |    2 | "ACCOUNTMGR" | "FURTHERANCE_SAVED"     | "true"        |
      |    2 | "ACCOUNTMGR" | "FURTHERANCE_ACTIVATED" | "false"       |
      |    1 | "POWERUSER"  | "FURTHERANCE_INITIATED" | "true"        |
      |    2 | "POWERUSER"  | "FURTHERANCE_SAVED"     | "true"        |
      |    2 | "POWERUSER"  | "FURTHERANCE_ACTIVATED" | "false"       |
