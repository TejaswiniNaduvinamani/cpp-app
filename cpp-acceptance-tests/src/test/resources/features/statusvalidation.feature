Feature: Status Validation
  
  If the user tries to perform a action
  when contract is not in appropriate status 
  operation should not be allowed and exception should be thrown.

  Scenario Outline: <line> Save contract pricing exception based on contract status
    Given request is made to save the contract price
    When the status of contract is in <current_cpp_status>
    And a request is made to update the saved values of contract princing information
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

  @language-setup @clm_token-setup
  Scenario Outline: <line> Delete pricing exhibit from clm throws exception when contract status is not equal to DRAFT
    Given all pricing information for the contract is completed
    And a request is made to attach exhibit in CLM
    When the status of contract is in <current_cpp_status>
    And a request is made to delete pricing exhibit from clm
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

  @customer_assignment_validation
  Scenario Outline: <line> Save assignment exception based on contract status
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    When a request is made to save assignment with real customer
    Then an exception is returned from the request
    And the exception type is <error_type>

    Examples: 
      | line | current_cpp_status     | error_type         |
      |    1 | "WAITING_FOR_APPROVAL" | "NOT_VALID_STATUS" |
      |    2 | "DRAFT"                | "NOT_VALID_STATUS" |
      |    3 | "HOLD"                 | "NOT_VALID_STATUS" |
      |    4 | "PRICING_ACTIVATED"    | "NOT_VALID_STATUS" |
      |    5 | "TERMINATED"           | "NOT_VALID_STATUS" |
      |    6 | "CANCELLED"            | "NOT_VALID_STATUS" |
      |    7 | "DELETED"              | "NOT_VALID_STATUS" |

  @customer_assignment_validation
  Scenario Outline: <line> Delete assignment exception based on contract status
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    And a request is made to save assignment with real customer
    Then an exception is returned from the request
    And the exception type is <error_type>

    Examples: 
      | line | current_cpp_status     | error_type         |
      |    1 | "WAITING_FOR_APPROVAL" | "NOT_VALID_STATUS" |
      |    2 | "DRAFT"                | "NOT_VALID_STATUS" |
      |    3 | "HOLD"                 | "NOT_VALID_STATUS" |
      |    4 | "PRICING_ACTIVATED"    | "NOT_VALID_STATUS" |
      |    5 | "TERMINATED"           | "NOT_VALID_STATUS" |
      |    6 | "CANCELLED"            | "NOT_VALID_STATUS" |
      |    7 | "DELETED"              | "NOT_VALID_STATUS" |

  @item_assignment_validation
  Scenario Outline: <line> Assign items exception based on contract status
    Given all pricing information for the contract is completed
    When the status of contract is in <current_cpp_status>
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    Then an exception is returned from the request
    And the exception type is <error_type>

    Examples: 
      | line | current_cpp_status     | error_type         |
      |    1 | "WAITING_FOR_APPROVAL" | "NOT_VALID_STATUS" |
      |    2 | "DRAFT"                | "NOT_VALID_STATUS" |
      |    3 | "HOLD"                 | "NOT_VALID_STATUS" |
      |    4 | "PRICING_ACTIVATED"    | "NOT_VALID_STATUS" |
      |    5 | "TERMINATED"           | "NOT_VALID_STATUS" |
      |    6 | "CANCELLED"            | "NOT_VALID_STATUS" |
      |    7 | "DELETED"              | "NOT_VALID_STATUS" |

  @item_assignment_validation
  Scenario Outline: <line> Expire future item assignments exception based on contract status
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    When the status of contract is in <current_cpp_status>
    And a request is made to expire the assignment of a future item
    Then an exception is returned from the request
    And the exception type is <error_type>

    Examples: 
      | line | current_cpp_status     | error_type         |
      |    1 | "WAITING_FOR_APPROVAL" | "NOT_VALID_STATUS" |
      |    2 | "DRAFT"                | "NOT_VALID_STATUS" |
      |    3 | "HOLD"                 | "NOT_VALID_STATUS" |
      |    4 | "PRICING_ACTIVATED"    | "NOT_VALID_STATUS" |
      |    5 | "TERMINATED"           | "NOT_VALID_STATUS" |
      |    6 | "CANCELLED"            | "NOT_VALID_STATUS" |
      |    7 | "DELETED"              | "NOT_VALID_STATUS" |

  @price_activation_validation
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

  @save_exhibit @language-setup @clm_token-setup
  Scenario Outline: <line> Create Exhibit exception based on contract status
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    When a request is made to attach exhibit in CLM
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

  Scenario Outline: <line> Save markup exception based on user role and contract status
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    And a markup added "PRODUCT_TYPE_CASE" with value "5.00"
	When a request is made to save the markup
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

  Scenario Outline: Save markup no exception thrown
    Given all pricing information for the contract is completed
    And the status of contract is in "DRAFT"
    And a markup added "PRODUCT_TYPE_CASE" with value "5.00"
	When a request is made to save the markup
    Then an exception is not returned from the request

  Scenario Outline: <line> Save markup indicators exception based on user role and contract status
    Given request is made to save the contract price
    And the status of contract is in <current_cpp_status>
    When a request is made to save the markup indicators
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

  Scenario Outline: Save markup indicators no exception throwns
    Given request is made to save the contract price
    And the status of contract is in "DRAFT"
    When a request is made to save the markup indicators
    Then an exception is not returned from the request

  Scenario Outline: <line> Add exception markup exception based on user role and contract status
    Given request is made to save the contract price
    And the status of contract is in <current_cpp_status>
    When a request is made to add a markup exception
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

  Scenario: Add exception markup no exception thrown
    Given request is made to save the contract price
    And the status of contract is in "DRAFT"
    When a request is made to add a markup exception
    Then an exception is not returned from the request

  Scenario Outline: <line> Delete markup exception based on user role and contract status
    Given request is made to save the contract price
    And the status of contract is in <current_cpp_status>
    When a request is made to delete the markup
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

  Scenario Outline: Delete markup no exception thrown
    Given request is made to save the contract price
    And the status of contract is in "DRAFT"
    When a request is made to delete the markup
    Then an exception is not returned from the request

  Scenario Outline: <line> Rename the markup from a contract exception based on user role and contract status
    Given request is made to save the contract price
    And the status of contract is in <current_cpp_status>
    And a markup added "PRODUCT_TYPE_CASE" with value "5.15"
	When a request is made to save the markup
    When a request is made to rename the markup
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

  Scenario: Rename the markup from a contract no exception thrown
    Given request is made to save the contract price
    And the status of contract is in "DRAFT"
    And a markup added "PRODUCT_TYPE_CASE" with value "5.15"
	When a request is made to save the markup
    When a request is made to rename the markup
    Then an exception is not returned from the request

  Scenario Outline: <line> Delete the future items markup with assigned items from a contract exception based on user role and contract status
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    When a request is made to delete the existing item "FUTURE_ITEM" markup
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
