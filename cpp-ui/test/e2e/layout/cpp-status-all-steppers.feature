@cpp-status-display-all-steppers
Feature: User can view CPP_STATUS on all CPP screens

Scenario: CPP Status is displayed on every stepper
    Given User is on the Contract Information Pricing Stepper
    Then CPP Status is displayed on Contract Pricing stepper
     And CPP Status is displayed on Distribution center stepper
     And CPP Status is displayed on Markup stepper
     And CPP Status is displayed on Split Case Fee stepper
     And CPP Status is displayed on Review stepper
     And CPP Status is displayed on Assignments tab
