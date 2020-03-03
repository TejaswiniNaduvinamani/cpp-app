@markup-navigation
Feature: Markup Navigation

  Scenario: User can navigate to the page for entering markups
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
    Then Markup screen is displayed

  Scenario: User should select at least one Distribution Center to navigate to the Markup Stepper
    Given User is on the Contract Information Pricing Stepper
    When User Clicks Next
      And Clicks Next Again
    Then Message saying at least one distribution center must be selected is displayed
