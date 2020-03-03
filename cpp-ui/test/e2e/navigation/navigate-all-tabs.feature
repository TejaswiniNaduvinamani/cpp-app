@navigate-all-tabs
Feature: Naviagtion of Header Tabs

  Scenario: All three header tabs are displayed on the top
    Given User is on the Contract Information Pricing Stepper
    Then Price Profile, Overview, Assignments tabs are displayed on the top

  Scenario: User is able to click & navigate Overview tab
    When User clicks Overview tab
    Then User sucessfully navigates to Overview tab

  Scenario: User is able to click & navigate Price Profile tab
    When User clicks Price Profile tab
    Then User sucessfully navigates to Price Profile tab

  Scenario: User is able to click & navigate to Assignments tab and see the contract information object
    When User clicks Assignments tab
    Then User sucessfully navigates to Assignments tab
      And Contract Information Object is displayed
