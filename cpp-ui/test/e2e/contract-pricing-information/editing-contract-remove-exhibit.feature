@editing-contract-remove-exhibit
Feature: Editing a contract in CPP will remove the older pricing exhibit from the contract in CLM

  Scenario: Display confirmation for Pricing Exhibit to be cleared on the first Stepper
    Given User is on the Contract Information Pricing Stepper
    When User clicks on save and continue to distribution centers button
    Then Clear pricing exhibit pop up is displayed to the User
