@adding-multiple-item-level-markups
Feature: Adding Multiple Item Level Markups

  Scenario: Users can add one item level markup at a time
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And User clicks Add item level Markup button Twenty Five times
    Then Twenty Five rows are added to item level markup grid one at a time

  Scenario: Users are limited to 25 item level markup entries per grid
    Then Add Item Level Markup button is disabled

  Scenario: Users are shown a message that only 25 item level markups may be entered
    When User clicks Add item level Markup button
    Then Message is displayed informing user that only 25 item level markups can be added

  Scenario: The default value in the 'Markup' column is blank
    Then Default value in Markup column is blank
