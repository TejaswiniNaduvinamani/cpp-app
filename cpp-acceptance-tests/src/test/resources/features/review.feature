@language-setup @clm_token-setup
Feature: Review
  
  As a Contract Pricing Profile Application user
  I should be able to get the all the data saved
  for a existing contract.

  Scenario: Get all the data for a contract in review
    Given all pricing information for the contract is completed
    When a request is made to get the all saved values of a contract for Review
    Then all saved values of a contract is returned

  Scenario: Attach Exhibit to CLM
    Given all pricing information for the contract is completed
    When a request is made to attach exhibit in CLM
    Then the exhibit is attached in CLM
    And exhibit sys id is saved in contract

  Scenario: Create Exhibit no exception thrown
    Given all pricing information for the contract is completed
    And the status of contract is in "DRAFT"
    When a request is made to attach exhibit in CLM
    Then an exception is not returned from the request
