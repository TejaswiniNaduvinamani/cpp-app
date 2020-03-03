@exception-markup-structures
Feature: Multiple Exception Markup Structures

  Scenario: User can add exception markup structures in addition to the default markup structure
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Adds an additional Exception Markup structure
      And Provides Exception Makrup Structure Name in the pop-up and clicks Add
    Then A new exception markup grid is added

  Scenario: User can expand or collapse markup grids as needed
    Then User can expand or collapse both the default as well as the exception markup grid

  Scenario: User can view unsaved data filled for a grid at any time
    When User expands default markup grid and provides valid markup data
     And Collapses the grid
    Then Data entered by User remains intact when user expands the grid again

  Scenario: When user saves a Markup grid succesfully, markup button label changes to Markup Saved
    When User completely fills an exception grid and clicks Save
    Then Markup button label changes to Markup Saved

