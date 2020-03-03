@formal-audit-pricing-privileges
Feature: Select Formal Audit Pricing Privileges

  Scenario: Users able to select Y/N for Formal Price Audit Privileges
    Given User is on the Contract Information Pricing Stepper
    When Answers Yes to the question to display Price Verification & Audit Privileges Toggles
    Then Formal Price Audit Privileges toggle is displayed correctly on CPP Customer Information Screen
      And User able to make a selection on the toggle
