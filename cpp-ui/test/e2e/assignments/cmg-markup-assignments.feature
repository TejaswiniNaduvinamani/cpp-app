@cmg-markup-assignments
Feature: User can view all CMG markups for a contract on the "Assignments" Page

  Scenario: User can view CMG Customer Type, Customer Name and Customer Id fields on Assignments Page
    Given User is on the Contract Information Pricing Stepper
    When When User clicks Assignments tab
    Then Customer Type, Customer Name, Customer Id are displayed on the Page

  Scenario: User is able to see markup data values from the Markup Stepper displayed on the right hand corner
    Then Correct markup data is displayed
