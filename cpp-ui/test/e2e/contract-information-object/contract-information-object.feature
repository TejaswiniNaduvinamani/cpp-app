@contract-information-object
Feature: Contract Information Object

  Scenario: Contract Information rendered correctly on CPP application
    Given User is on the Contract Information Pricing Stepper
    Then Contract Name is correctly displayed on CPP Customer Information screen
      And Contract Type is correctly displayed
      And Contract Start Date is correctly displayed
      And Contract End Date is correctly displayed

  Scenario: Pricing Start and End Dates are not displayed on 1st Stepper
    Then Pricing Start Date is not displayed
      And Pricing End Date is not displayed

  Scenario: Pricing Start & End Date are correctly displayed on 2nd Stepper
    When User navigates to Distribution Centers Stepper
    Then Pricing Start Date is displayed correctly
      And Pricing End Date is displayed correctly
