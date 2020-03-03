@split-case-fee-type
Feature: Selecting Split Case Fee Type on Split Case Fee Stepper

Scenario: Users can select Fee Type to choose a basis for split case fee
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Enters mandatory information and navigates to Split Case Fee Stepper
    Then User Should be able to select Fee Type as Price per Pack
      And User should be able to select Fee Type as Cost per Pack and Navigate to review stepper
