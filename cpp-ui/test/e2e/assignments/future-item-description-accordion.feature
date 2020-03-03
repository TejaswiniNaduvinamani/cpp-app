@future-item-description-accordion
Feature: User can view Future Item Descriptions on the contract as accordions on the Assignments Page

  Scenario: User sees Future Item Descriptions on the contract as accordions in Assignments Tab
    Given User is on the Contract Information Pricing Stepper
    When User clicks Assignments tab
      And User clicks Items tab
    Then Future item descriptions are displayed as accordions 
