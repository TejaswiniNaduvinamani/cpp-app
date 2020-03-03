@item-level-markup-same-description
Feature: Item Level Markups With Same Description

  Scenario: Error Message should be displayed to the User when He/She enters a duplicate Future Item description
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And User Adds a Future Item Level Markup for the default grid
      And Adds another Future Item Level Markup with exact same description
    Then Error Message that Item has been already added is displayed

  Scenario: Markup and the markup type can still be updated after saving a Future Item Markup
    When User deletes last added future Item Markup and Saves
    Then Markup value and markup type can still be updated and saved for the first row
