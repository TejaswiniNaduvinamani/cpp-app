Feature: Contract Pricing
  
  As a Contract Pricing Profile Application user
  I should be able to get the default and saved values of contract princing information
  for existing and new contracts,
  and I must be able to save/update the values of contract princing information.

  Scenario: Get the default values of contract pricing for new contract
    When a request is made to get the default values of contract princing information
    Then the default values of contract princing information is returned

  Scenario: Save the values of contract pricing for new contract
    When request is made to save the contract price
    Then the values of contract pricing are saved

  @clm_token-setup 
  Scenario: Get the values of contract from CLM
    When request is made to get the CPP Information
    Then the latest values of contract from CLM is returned

  Scenario: Fetch the saved pricing values of contract pricing for existing contract
    Given request is made to save the contract price
    When a request is made to get the existing values of contract princing information
    Then the existing values of contract princing information is returned

  Scenario: Update and saved pricing values of contract pricing for existing contract
    Given request is made to save the contract price
    When a request is made to update the saved values of contract princing information
    Then the existing values of contract princing information is updated

  Scenario Outline: Save contract pricing no exception thrown
    Given request is made to save the contract price
    When the status of contract is in "DRAFT"
    And a request is made to update the saved values of contract princing information
    Then an exception is not returned from the request

  @language-setup @clm_token-setup 
  Scenario: Delete pricing exhibit from clm
    Given all pricing information for the contract is completed
    And a request is made to attach exhibit in CLM
    When a request is made to delete pricing exhibit from clm
    Then the pricing exhibit is deleted from clm
