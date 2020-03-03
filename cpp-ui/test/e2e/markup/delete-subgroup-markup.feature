@delete-subgroup-markup
Feature: Delete SubGroup Markup Structures

Scenario: User can view the delete confirmation question on delete operation of Subgroup
  Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Clicks On Trash Icon of SubGroup Markup
  Then Correct Delete confirmation question is displayed for SubGroup Markup

