@cost-schedule-package
Feature: Select Cost Schedule Package

  Scenario: Users must only select from GFS Fiscal Calendar or Gregorian Calendar
    Given User is on the Contract Information Pricing Stepper
    Then Cost Schedule Package is displayed
      And User able to make a selection on GFS Fiscal Calendar
      And Gregorian Calendar
