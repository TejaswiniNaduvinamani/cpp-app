@cpp-to-clm-navigation
Feature: Adding Multiple real (non CMG) Customers on the Assignments Screen

  Scenario: Save Assignments Button is enabled when User adds one real customer to the default concept
    Given User is on the Contract Information Pricing Stepper
      When User enters Pricing Information for all 5 steppers
      And User clicks on return to Contract Button
    Then User succesfully navigates to the CLM application
