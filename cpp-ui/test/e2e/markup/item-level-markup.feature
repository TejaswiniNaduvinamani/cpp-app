@item-level-markup
Feature: Item Level Markup Structure

  Scenario: User is able to see item level markup description
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
    Then Correct Item Level Markup description is displayed on Markup Stepper

  Scenario: User is able to view Item level markup grid on Markup Stepper
    When User clicks Add item level Markup button
    Then Item level markup grid is added

  Scenario: User is able to add rows to Item Level Markup grids
    When User clicks Add item level Markup button
    Then A new row is added to item level Markup grid

  Scenario: User can select item level Markup Type as either per Sell Unit or per Case or per Weight when Markup by $ is selected
    When User selects item level Markup by $ for a given row
    Then User can select item level Markup Type as per Sell Unit
      And Item level Markup Type as per Case
      And Item level Markup Type as per Weight

  Scenario: Markup Type is disabled for selection when Markup by % is selected
    When User selects item level Markup by % for a given row
    Then Item level Markup Type is disabled for selection

  Scenario: Effective and Expiration dates for item level markups are same as Pricing Effective and Expiration dates
    Then Effective and Expiration dates for item level markups are same as Pricing Effective and Expiration dates from 1st Stepper

  Scenario Outline: Item level Markups entered are always accurate to two digits after the decimal
    When User enters a "<numeric value>" and tabs out of an item level Markup field
    Then Item level Markup entered is rounded off to two decimal places

    Examples:
    | numeric value |
    | 2             |
    | 2.1           |
    | 0.1023        |
