@future-item-multiple-item-codes-addition
Feature: User can view Future Item Descriptions on the contract as accordions on the Assignments Page

  Scenario: User able to add multiple Rows for assigning Item level Markups to Future Items on the Items tab by clicking "Add Item" button
    Given User is on the Contract Information Pricing Stepper
    When User clicks Assignments tab
      And User clicks Items tab
    Then Add Another button enables rows to add Item Codes for Future Markup Accordions

  Scenario: User is able to add Multiple Valid Item Level Codes to Future Item Accordions
    Then User is able to add Multiple Valid Item Level Codes to Future Item Accordions
      And Save Assignments Button is enabled in Items tab

  Scenario: User is unable to add Item Level Codes with Status Code other than "DS", "AC" and "NE"
    When User enters invalid Item Codes and clicks Find
    Then Invalid Item Code Error message is displayed
      And Save Assignments Button is disabled
