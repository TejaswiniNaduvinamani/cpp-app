@furtherance-popup
Feature: Display of Furtherance popup

  Scenario: Furtherance popup is displayed correctly on CPP application
   Given User launches CPP Application for a Furtherance
    Then Furtherance popup is displayed on screen
  
  Scenario: CPP is loaded in Furtherance Mode
    When User clicks on Continue button on Furtherance popup
    Then Stepper 1 is loaded in Furtherance Mode
