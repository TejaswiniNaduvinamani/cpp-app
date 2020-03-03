@activate-price-profile-button
Feature: Validating behaviour of Activate Price Profile Button

  Scenario: Activate Price Profile Button is disabled when no real customers are assigned
    Given User launches CPP Application
    When User clicks Assignments tab
      And User completes all Customer Assignments
    Then Activate Price Profile Button is enabled

  Scenario: When user clicks Activate Price Profile Button
    When User clicks Activate Price Profile Button
    Then Success Message is displayed and Price Activation Button is disabled
