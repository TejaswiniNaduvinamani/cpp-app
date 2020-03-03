@furtherance-split-case-fee-save
Feature: Save and Continue of Split Case Fee values for a Furtherance

  Scenario: User reaches Review Stepper 
    Given User launches CPP Application for a Furtherance
      And Clicks Continue to enter Furtherance mode
    When User enters mandatory information and navigates to Markup Stepper
    Then Next button label is correctly displayed
      And User is navigated to Split Case Fee stepper on click of this button
