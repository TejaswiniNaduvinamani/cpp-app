@delete-exception-markup
Feature: Delete Exception Markup Structures

Scenario: User can view the delete confirmation question on delete operation
  Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Adds an exception Markup structure
      And Delete the exception Markup structure name
  Then Correct confirmation question is displayed

Scenario: Deleted exception markup stucture is no longer displayed on screen
  Then Deleted exception markup stucture is no longer displayed on screen
