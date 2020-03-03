@edit-furthurance-first-stepper
Feature: Display of Furtherance 

  Scenario: Furtherance effective date must always be greater than or equal to current date
    Given User launches CPP Application for a Furtherance
    When User enters Furtherance Effective Date less than Current Date
    Then User sees error message: Furtherance cannot be made effective for dates earlier than today.

  Scenario: Furtherance effective date must not greater than Contract End Date
    When User enters Furtherance Effective Date greater than Contract End Date
    Then User sees error message: Furtherance effective date cannot be greater than the contract end date.

    Scenario: User can only enter valid date formats: mm/dd/yyyy or mm-dd-yyyy in furthurance date field
    When User enters an invalid date format in furthurance date field
    Then User sees error message to enter correct date for in furthurance date field

  Scenario: User is able to Enter/edit Reason for change
    Then User is able to enter Reason for change

  Scenario: User is able to Enter/edit Contract Reference
    Then User is able to enter Contract Reference