@adding-real-customers
Feature: Adding Multiple real (non CMG) Customers on the Assignments Screen

  Scenario: Users can a single real (non CMG) Customers in the Assignments Tab for the default (contract) grid
    Given User is on the Contract Information Pricing Stepper
    When User clicks Assignments tab
    Then Only a single real Customer can be assigned to default grid

  Scenario: Users can multiple real (non CMG) Customers for exception grids
    Then User can add a single real Customer to an exception CMG grid
      And User can add another customer to the same grid

  Scenario: Users can remove Customers for assignment for any grid
    Then The user can delete a Customer from the exception grid
