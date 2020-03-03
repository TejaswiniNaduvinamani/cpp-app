@validate-pricing-effective-and-expiration-dates
Feature: Validate Pricing Effective And Expiration Dates

  Scenario: Pricing Effective & Expiration Dates are the same as Contract Start & Contract End Dates
    Given User is on the Contract Information Pricing Stepper
    Then Pricing Effective & Exipration Dates are displayed correctly on CPP Information Screen
     And They are equal to Contract Start Date and Far Out Date respectively

  Scenario: Pricing effective is always be less than or equal to contract start date
    When User enters Pricing Effective Date greater than Contract Start Date
     And User clicks Next
    Then User sees error message: Pricing must be effective before or on the same day as Contract Start Date

  Scenario: Pricing effective date must always be greater than or equal to current date
    When User enters Pricing Effective Date less than Current Date
     And User clicks Next
    Then User sees error message: Pricing cannot be made effective for dates earlier than today.

  Scenario: User can only enter valid date formats: mm/dd/yyyy or mm-dd-yyyy
    When User enters an invalid date format
     And User clicks Next
    Then User sees error message to enter correct date format
