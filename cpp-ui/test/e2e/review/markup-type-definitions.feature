@markup-type-definitions
Feature: Markup Type Definitions

  Scenario: Markup Type Definitions displayed correctly for "Sell Unit"
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Only selects Sell Unit as Markup Type for all the rows
    Then Review Screen displays Markup Type Definitions for only Sell Unit

  Scenario: Markup Type Definitions displayed correctly for "Per Case"
    When User goes back and adds Per Case Markup Type for any of the rows
    Then Review screen displays Markup Type Definitions for both Sell Unit and Per Case

  Scenario: Markup Type Definitions displayed correctly for "Per Weight"
    When User goes back and adds Per Weight Markup Type for any of the rows
    Then Review screen displays Markup Type Definitions for Sell Unit, Per Case and Per Weight Markup Types
