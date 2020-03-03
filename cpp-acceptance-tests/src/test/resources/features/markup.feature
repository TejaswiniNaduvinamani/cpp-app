Feature: Markup
  
  As a Contract Pricing Profile Application user
  I should be able to get the default and saved values of markup
  fee for existing and new contracts,
  and I must be able to save the values of markup

  Scenario: Get the default values of markup for new contract
    Given request is made to save the contract price
    When a request is made to get the default values of markup
    Then the default markup structure values is returned

  Scenario: Create a Exception markup for new contract
    Given request is made to save the contract price
    When a request is made to add a markup exception
    Then the newly added exception is returned

  Scenario: Get the default values of markup exception for new contract
    Given request is made to save the contract price
    And a request is made to add a markup exception
    When a request is made to get the default values of markup exception
    Then the default values of markup exception is returned

  Scenario: Delete the existing items markup from a contract
    Given request is made to save the contract price
    And a markup added "ITEM_CASE" with value "5.15"
    When a request is made to save the markup
    When a request is made to delete the existing item "ITEM_CASE" markup
    Then the existing "ITEM_CASE" markup are deleted

  Scenario: Delete the markup from a contract
    Given request is made to save the contract price
    And a markup added "PRODUCT_TYPE_CASE" with value "5.15"
    When a request is made to save the markup
    And a request is made to add a markup exception
    When a request is made to delete the markup
    Then the existing "PRODUCT_TYPE_CASE" markup are deleted

  Scenario: Delete the future items markup from a contract
    Given request is made to save the contract price
    And a markup added "FUTURE_ITEM" with value "5.15"
    When a request is made to save the markup
    When a request is made to delete the existing item "FUTURE_ITEM" markup
    Then the existing "FUTURE_ITEM" markup are deleted

  Scenario: Delete the future items markup with assigned items from a contract
    Given all pricing information for the contract is completed
    And the status of contract is in "DRAFT"
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    When a request is made to delete the existing item "FUTURE_ITEM" markup
    Then the future items markup and assigned items are deleted

  Scenario: Rename the markup from a contract
    Given all pricing information for the contract is completed
    And a request is made to add a markup exception
    When a request is made to rename the markup

  Scenario: Update the Future Item Markups with assigned items from a contract
    Given request is made to save the contract price
    And a markup added "FUTURE_ITEM" with value "10.43"
    When a request is made to save the markup
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    When the status of contract is in "DRAFT"
    And a markup added "FUTURE_ITEM" with value "11.43"
    When a request is made to save the markup
    Then the values of "FUTURE_ITEM" markup are saved with value "11.43"
    And the values of assigned item markup are updated for future item

  Scenario: Fetch markup based on sell for a contract
    Given request is made to save the contract price
    And a request is made to save the markup indicators
    When a request is made to fetch the markup indicators
    Then the value of markup indicators returned

  Scenario: Get the subgroup information for new contract
    Given request is made to save the contract price
    When a request is made to get subgroup information for "VALID_SUBGROUP_ID"
    Then the subgroup information is returned

  Scenario Outline: <line> Validate invalid and duplicate subgroup for new contract
    Given all pricing information for the contract is completed
    When a request is made to get subgroup information for <subgroup_id>
    Then exception is thrown with validation error <error_type>

    Examples: 
      | line | subgroup_id           | error_type               |
      |    1 | "INVALID_SUBGROUP_ID" | "INVALID_SUBGROUP"       |
      |    2 | "VALID_SUBGROUP_ID"   | "SUBGROUP_ALREADY_EXIST" |

  Scenario: Delete the existing subgroup markup from a contract
    Given request is made to save the contract price
    And a markup added "SUBGROUP_CASE" with value "5.15"
    When a request is made to save the markup
    When a request is made to delete the existing subgroup "SUBGROUP_CASE" markup
    Then the existing "SUBGROUP_CASE" markup are deleted

  Scenario Outline: <line> Save the  Markups for a contract
    Given request is made to save the contract price
    And a markup added <markup> with value <markup_value>
    When a request is made to save the markup
    Then the values of <markup> markup are saved with value <markup_value>

    Examples: 
      | line | markup              | markup_value |
      |    1 | "PRODUCT_TYPE_CASE" | "5.15"       |
      |    2 | "PRODUCT_TYPE_SELL" | "6.16"       |
      |    3 | "ITEM_CASE"         | "7.17"       |
      |    4 | "ITEM_SELL"         | "8.18"       |
      |    5 | "SUBGROUP_CASE"     | "9.19"       |
      |    6 | "SUBGROUP_SELL"     | "10.10"      |
      |    7 | "FUTURE_ITEM"       | "11.11"      |

  Scenario Outline: <line> Update the Markups for a contract
    Given request is made to save the contract price
    And a markup added <markup> with value <markup_value>
    And a request is made to save the markup
    And a markup added <markup> with value <updated_markup_value>
    And a request is made to save the markup
    Then the values of <markup> markup are saved with value <updated_markup_value>

    Examples: 
      | line | markup              | markup_value | updated_markup_value |
      |    1 | "PRODUCT_TYPE_CASE" | "5.15"          | "11.11"              |
      |    2 | "PRODUCT_TYPE_SELL" | "6.16"          | "12.12"              |
      |    3 | "ITEM_CASE"         | "7.17"          | "13.13"              |
      |    4 | "ITEM_SELL"         | "8.18"          | "14.14"              |
      |    5 | "SUBGROUP_CASE"     | "9.19"          | "15.15"              |
      |    6 | "SUBGROUP_SELL"     | "10.10"         | "16.16"              |
      |    7 | "FUTURE_ITEM"       | "11.11"         | "17.17"              |
