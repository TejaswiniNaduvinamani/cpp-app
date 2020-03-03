@future-item-level-markup
Feature: Future Item Level Markups

  Scenario: User can select a check-box called "No Item ID" to imply that the item code currently doesn't exist
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And User clicks Add item level Markup button
    Then User is able to select No Item Id checkbox

  Scenario: If "No Item ID" checkbox is selected, users are allowed to type free text to provide a description of the item
    Then User is allowed to type free text as item description
