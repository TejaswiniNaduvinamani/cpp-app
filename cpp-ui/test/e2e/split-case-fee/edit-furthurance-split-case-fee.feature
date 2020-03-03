@edit-furthurance-split-case-fee
Feature: Editing of split case fee

  Scenario: User can edit split case fee in Furtherance Mode
    Given User launches CPP Application for a Furtherance
    When User clicks on split case fee stepper
    Then User is able to edit split case fee values
      And User is not able to switch between % or $
