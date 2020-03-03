@select-price-verification-privileges
Feature: Price Verification Privileges for the customer

  Scenario: Users able to select Y/N for Price Verification Privileges
    Given User is on the Contract Information Pricing Stepper
    When Answers yes to the question to have both price verification and formal price audit privileges option
    Then Formal Price Verification Privileges toggle is visible on CPP Customer Information Screen
      And User able to make a selection on the Pricing Verification Privileges toggle

  Scenario: Users can obtain explanation when He/She hovers on the Info Icon
    When User hovers on the info Icon
    Then User will able to see a detailed explanation
