@edit-save-split-case-fee
Feature: User can edit and save attributes on 'Split Case Fees' Stepper

  Scenario: Data saved on Split case stepper are same after navigating back to the same stepper
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Clicks Next to reach Split Case Fee stepper
      And Enter a value above 49
      And Clicks on Copy to All button
      And Clicks Next to reach Review stepper
      And User goes back to the Split case fee stepper
    Then All split case fee data are correctly displayed
