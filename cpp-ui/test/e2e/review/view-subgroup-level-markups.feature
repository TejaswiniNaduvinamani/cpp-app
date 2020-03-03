@view-subgroup-level-markups
Feature: View Subgroup Level Markups On Review Screen

  Scenario: User can view Subgroup-level markups entered on 'Markup' stepper in the 'Review' stepper accurately.
    Given User is on the Contract Information Pricing Stepper
      When User clicks Next
      And Clicks Next Again
      And Enters mandatory information and navigates to Split Case Fee Stepper
      And Clicks Next Again
    Then Subgroup-level markups are displayed correctly on Review screen
