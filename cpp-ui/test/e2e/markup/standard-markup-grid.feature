@standard-markup-grid
Feature: Standard Markup Grid

  Scenario: User can select either Markup by % or Markup by $
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
    Then User can select either Markup by % or Markup by $

  Scenario: User can select Markup Type as either per Sell Unit or per Case when Markup by $ is selected
    When User selects Markup by $ for a given row
    Then User can select Markup Type as per Sell Unit
    And Markup Type as per Case

  Scenario: Markup Type is disabled for selection when Markup by % is selected
    When User selects Markup by % for a given row
    Then Markup Type is disabled for selection

  Scenario: User can select 'Yes' or 'No' from 'Expire Lower' question
    Then User can select Yes for Expire Lower question
    And User can select No for Expire Lower question

  Scenario: Effective and Expiration dates for markups are same as Pricing Effective and Expiration dates
    Then Effective and Expiration dates for all markups are same as Pricing Effective and Expiration dates from 1st Stepper

  Scenario: 'Save Markup' button is inactive until markups for all product types are entered
    Then Save markup button is active only after all product type rows have been entered

  Scenario: Markup value, markup unit and markup type are copied down to all rows when Copy To All button is selected
    When User provides a given Markup value, Unit and Type for the first row
    Then All values are copied down to other rows when user clicks Copy To All

  Scenario Outline: Markups entered are always accurate to two digits after the decimal
    When User enters a "<numeric value>" and tabs out of a Markup field
    Then Markup entered is rounded off to two decimal places

    Examples:
    | numeric value |
    | 2             |
    | 2.1           |
    | 0.1023        |

  Scenario: Warning Message is displayed for a markup value less than 5%
    When User enters markup value less than 5%
    Then Warning Message is displayed for that row

  Scenario: Warning Message is displayed for a markup value greater than 45%
    When User enters markup value greater than 45%
    Then Warning Message is displayed for that row

  Scenario: Warning Message is displayed for a markup value less than 0.05$
    When User enters markup value less than 0.05$
    Then Warning Message is displayed for that row

  Scenario: Warning Message is displayed for a markup value greater than 5$
    When User enters markup value greater than 5$
    Then Warning Message is displayed for that row
