@edit-furtherance-future-item
Feature: Editing Future Item Mapping in Markup Stepper for Furtherance

  Scenario: User can edit mapped future item in markup steppr for Furtherance
    Given User launches CPP Application for a Furtherance
    When  User navigates to markup stepper
    Then  Edit icon is displayed for future item row
     And  Item mapping Pop-up is displayed on click of Edit Icon
