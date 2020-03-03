@cpp-user-flow-contract-type
Feature: CPP User Flow Based On Contract Type

  Scenario: Show All Steppers, Toggles & Cost Schedule Center Options for Regional Contract Type
    Given User is on the Contract Information Pricing Stepper
    When Answers Yes to the question to display Price Verification & Audit Privileges Toggles
    Then All Steppers are displayed on CPP Customer Information Screen
      And Formal Price Audit Privileges toggle is displayed
      And Schedule for Cost Changes section is displayed
