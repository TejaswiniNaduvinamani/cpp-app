@distribution-centers-selection
Feature: Distribution Centers Selection

  Scenario: There is no restriction on number of distribution centers that can be selected
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
    Then Distribution Centers Screen is Displayed
      And User can make any number of selections on the Distribution Centers

  Scenario: Users can deselect all distribution centers using Clear All button
    Then Users can deselect all distribution centers using Clear All button

  Scenario: Users can select all distribution centers using Select All button
    Then Users can select all distribution centers using Select All button
