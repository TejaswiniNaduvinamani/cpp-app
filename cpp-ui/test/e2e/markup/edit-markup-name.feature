@edit-markup-name
Feature: Edit exception Markup Structures

  Scenario: User can rename the newly added markup stucture
    Given User is on the Contract Information Pricing Stepper
      When User clicks Next
        And Clicks Next Again
        And Adds an exception Markup structure
        And Edits the exception Markup structure name
    Then Exception markup name is renamed

  Scenario: User is able to see error messsage for duplicate Exception markup name 
    When User enters a new markup with same name
    Then Correct error messsage is visible on the Page
