@markup-based-on-sell
Feature: Select If Price Calculation Should be 'Markup on sell'
  
  Scenario: The default value for 'Markup based on Sell' is 'Yes'
  Given User is on the Contract Information Pricing Stepper
    When User clicks Next
      And Clicks Next Again
  Then Default value for Markup based on Sell toggle is Yes

  Scenario: When 'Markup based on sell' as 'No', then warning message is displayed
  When User selects markup based on sell Toggle as No
  Then Warning message is displayed to the User
