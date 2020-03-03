@item-level-markup-concepts
Feature: User can view all item level markups for a contract on the "Assignments" tab

  Scenario: Item level markups are also shown for Concepts on the "Assignments" Page
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Enters mandatory information and navigates to Split Case Fee Stepper
      And User clicks Assignments tab
    Then All correct item level markup data is displayed
