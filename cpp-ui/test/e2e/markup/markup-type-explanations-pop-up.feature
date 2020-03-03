@markup-type-explanations
Feature: Users can view explanations for markup type calculations on a Pop-up

  Scenario: Markup Type pop-up opens correctly on clicking info icon on the Default Grid
    Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
      And Clicks on Info icon against Markup Type Column in the Default Grid
    Then Pop-up is correctly displayed

  Scenario: Markup Type pop-up opens correctly on clicking info icon on the Item Level Grid
    When User closes the pop-up and clicks on Info icon against Markup Type Column in the Item Level Grid
    Then Pop-up is correctly displayed
