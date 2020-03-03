@pricing-language-cost-price-pack
Feature: User can view CPP pricing language summary for price/pack selection

Scenario: User can view CPP pricing language summary for price/pack selection
  Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Enters mandatory information and navigates to Split Case Fee Stepper
      And Clicks Next Again
    Then Correct pricing language is displayed for Price per pack
