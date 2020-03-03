@cpp-id-version-number-display-all-steppers
Feature: User can view CPP_ID and Version number on all CPP screens

Scenario: CPP id and version number are displayed on every stepper
    Given User launches CPP Application
    Then CPP Id and version number are displayed on Contract Pricing stepper
     And CPP Id and version number are displayed on Distribution center stepper
     And CPP Id and version number are displayed on Markup stepper
     And CPP Id and version number are displayed on Split Case Fee stepper
     And CPP Id and version number are displayed on Review stepper
     And CPP Id and version number are displayed on Assignments tab
