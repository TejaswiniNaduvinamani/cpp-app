@split-case-fee-grid
Feature: Validating Split Case Fee terms for the customer

Scenario: User can select split case fee by % and the default value should be 35%
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Provides and Saves valid Markup values
      And Clicks Next to reach Split Case Fee stepper
    Then User Should able to select fee by % on any row
      And Default value should be 35%

Scenario: User is shown warning message when split case fee is above 5$
    When User selects $ as split case fee for any row
    Then User sees warning message for exceeding Split Case Fee above 5 dollars

Scenario: User is shown warning message when split case fee is above 49%
    When User selects % as split case Fee for any row
      And Enter a value above 49
    Then User sees a warning message that fee should ideally be below 49%

Scenario: User can see warning message if the split case is 0.00
    When User selects % as split case Fee for any row
      And Enters zero value
    Then Extra level approval warning message is displayed

Scenario: The effective and expiration dates are the same as pricing effective and expiration dates from the first stepper
    Then Effective and expiration dates are same as pricing effective and expiration dates from first stepper

Scenario: User can enter split case fees for the first row and copy values to all rows using 'Copy Down' button
    When User selects % as split case Fee for any row
      And Enter a valid value
      And Clicks on Copy to All button
    Then The value and fee type should be copied to all rows below
