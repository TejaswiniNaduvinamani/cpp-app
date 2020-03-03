@assigning-real-customers
Feature: Adding Multiple real (non CMG) Customers on the Assignments Screen

  Scenario: Save Button is disabled when Default Grid has a Real Customer Assigned
    Given User is on the Contract Information Pricing Stepper
    When User clicks Assignments tab
    Then Save Button is disabled for the Default Grid

  Scenario: Save Button is enabled when Exception Grid has Real Customers Assigned
    When User assigns Real Customer to Exception Grid
    Then Save Button is enabled for the Exception Grid
