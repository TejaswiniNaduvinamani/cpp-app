@duplicate-item-code-same-concept-assignment
Feature: A single item code cannot be assigned multiple times to the same concept

  Scenario: User is able to see error message when saving duplicate Item Level Codes to same concept
    Given User is on the Contract Information Pricing Stepper
    When User clicks Assignments tab
      And User clicks Items tab
      And User adds an item code that already exists and clicks Find
    Then Duplicate error message is shown to the user for that Row
