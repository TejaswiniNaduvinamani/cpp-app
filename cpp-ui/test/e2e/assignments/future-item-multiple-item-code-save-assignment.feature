@future-item-multiple-item-code-save-assignment
Feature: Assign/Save multiple item codes to a single future item description

  Scenario: User is able to add Multiple Valid Item Level Codes to Future Item Accordions
    Given User is on the Contract Information Pricing Stepper
    When User clicks Assignments tab
      And User clicks Items tab
      And Adds Multiple Valid Item Codes to Future Item Accordions
    Then Saves the Assignments
      And Confirmation Pop-up is displayed to user
