@contract-level-assignment-deletion
Feature: Deleting the Contract Level Real Customer Assignment

  Scenario: User is shown confirmation pop-up when User deletes Default grid assignment, that Exception grid Assignments will also be deleted
    Given User is on the Contract Information Pricing Stepper
    When User clicks Assignments tab
      And Deletes default grid Assignment
    Then User is shown a confirmation pop-up that all Assignments from Contract will be removed
