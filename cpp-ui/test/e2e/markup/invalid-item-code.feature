@invalid-item-code
Feature: Item Level Markup Structure

  Scenario: User able to fetch and view item description
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And User clicks Add item level Markup button
      And User enters a valid item code
    Then Correspoding Item Description is fetched and displayed

  Scenario: User sees error message when a wrong Item code is entered
    When User clicks Add item level Markup button
      And User enters invalid Item code
    Then Error message is displayed to enter a valid Item code

  Scenario: User able to see error message on entering inactive item code
    When User enters inactive item code
    Then Error message displayed to enter active item code

  Scenario: User able to see error message on entering duplicate item code
    When User enters duplicate item code
    Then Error message displayed to enter non duplicate item code
