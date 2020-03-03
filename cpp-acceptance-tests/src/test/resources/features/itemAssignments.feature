Feature: Item Assignments
  
  As a Contract Pricing Profile Application Power user
  I should be able to all item assignment operation in 
  Item Assignment tab.

  Scenario: Get the list of all future items for a contract
    Given request is made to save the contract price
    And a markup added "FUTURE_ITEM" with value "10.43"
	When a request is made to save the markup
    When a request is made to fetch all future items
    Then the future items are returned

  Scenario: Assign items with future items and concept
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    When a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    Then an item is assigned to future item and concept
    And the values of item markup are saved for future item

  Scenario: Validate already existing assign items for save assignment
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    When a request with already existing items is made to assign items with future item
    Then the items already exist status returned for "ITEM_TYPE_FOR_ASSIGNMENT"

  Scenario: Validate duplicate items on list for save assignment
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    When a request with duplicate items is made to assign items with future item
    Then the items already exist status returned for "ITEM_TYPE_FOR_ASSIGNMENT"

  Scenario: Expire future items assigned to a concept
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    When a request is made to expire the assignment of a future item
    Then the future item mapping is expired

  Scenario: Assign items no exception thrown
    Given all pricing information for the contract is completed
    When the status of contract is in "CONTRACT_APPROVED"
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    Then an exception is not returned from the request

  Scenario: Expire future item assignments no exception thrown
    Given all pricing information for the contract is completed
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to assign items "ITEM_CASE" with "FUTURE_ITEM" future item
    When a request is made to expire the assignment of a future item
    Then an exception is not returned from the request
