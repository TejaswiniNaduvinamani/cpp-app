@cpp-tab-navigation
Feature: CPP Tab Navigation

  Scenario: Validate Header Title Presence, Logo, Screen Title
    Given User is on the Contract Information Pricing Stepper
    Then Header title is Customer Price Profile
      And Logo is displayed
      And Screen title is Create Price Profile for New Contract

  Scenario: Validate Stepper Presence
    Then Stepper 1 is Customer Information
      And Stepper 2 is Distrubution Centers
      And Stepper 3 is Markup
      And Stepper 4 is Split Case Fee
      And Stepper 5 is Review

  Scenario: User cannot directly navigate to steppers after the current Stepper
    Then User is unable to navigate directly to Subsequent steppers

  Scenario: User can directly navigate to steppers prior to current Stepper
    When User clicks Next
    Then User is able to navigate directly to previous stepper by clicking on it
