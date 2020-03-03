@copying-markup-value
Feature: Copy Markup Values From One Grid To Another

  Scenario: User can copy markup values into exception markup structures from other markup grids
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Adds an exception Markup structure
      And Selects Default Grid from Copy Markup option
    Then Markup data gets copied from Default Grid to current Markup structure

  Scenario: User is able to see help text on hovering over info icon 
    Then Correct help text is displayed when user hovers over info icon

  Scenario: User is able to see Copy option and dropdown field for a newly added exception markup structure
    Then Copy Markup option is visible for the exception Markup structure
