@price-verification-audit-privileges-selection
Feature: Price Verification Audit Privileges Selection

  Scenario: User can answer 'Yes' or 'No' to a question to see/unsee Price Verification and Formal Price Audit Privileges Toggles
    Given User is on the Contract Information Pricing Stepper
    Then Question whether to show price verification and formal price audit privileges toggle or not is displayed

  Scenario: No toggle should be visible when User answers 'No' to the question to display the Toggles
    Given User is on the Contract Information Pricing Stepper
    Then No toggle should be displayed

  Scenario: Both Toggles should be visible on Selection of Distribution Agreement National & Distribution Agreement Regional contract type
    Given User is on the Contract Information Pricing Stepper
      And Answers Yes to the question to display Price Verification & Audit Privileges Toggles
    Then Both Price Verification and Audit Privileges Toggles is displayed
