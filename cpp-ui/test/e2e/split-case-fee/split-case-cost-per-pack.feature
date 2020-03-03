@split-case-cost-per-pack
Feature: User can enter Split Case Fee Type as cost/pack or price/pack

  Scenario: Cost/pack or price/pack can be selected by the user on the Split Case Fee Stepper
    Given User launches CPP Application
    When User clicks Next
      And Clicks Next Again
      And Clicks Next to reach Split Case Fee stepper
    Then User can select Split Case Fee based on cost per pack or price per pack

  Scenario: Correct Helper Text should be displayed against Fee Type
    Then Correct Helper Text is displayed for Fee Type
