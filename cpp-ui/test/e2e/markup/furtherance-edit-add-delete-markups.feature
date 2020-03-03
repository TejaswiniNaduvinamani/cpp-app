@furtherance-edit-add-delete-markups
Feature: Editing product markup values, subgroup and edit/add/delete item level markups in Furtherance mode

  Scenario: User can edit values for existing product type, subgroup and item level markups on the markup stepper
    Given User launches CPP Application for a Furtherance
      And Clicks Continue to enter Furtherance mode
    When Enters mandatory Furtherance information on the first stepper
      And Clicks Next
      And Clicks Next Again
    Then Existing markup values are editable
      And Subgroup Markups are editable
      And Item Level Markups are editable

  Scenario: User can add/delete real items and only Delete Future Items
    Then Real Item Level markup rows can be added
      And Real Items Level markup rows can be deleted
      And Future Item Level Markup rows can be deleted
