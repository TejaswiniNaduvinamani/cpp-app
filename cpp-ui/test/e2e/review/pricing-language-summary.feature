@pricing-language-summary
Feature: Review CPP Pricing Language As Summary

Scenario: User can view CPP pricing language summary for GFS Fiscal calender
    Given User is on the Contract Information Pricing Stepper
    When User enters all CPP pricing information with cost schedule package as GFS Fiscal calender
    Then Correct pricing language is displayed for GFS Cost

Scenario: User can view CPP pricing language summary for all Markup values entered
    Then Correct pricing language is displayed for all Markup values entered

Scenario: User can view CPP pricing language summary for split case fee values entered
    Then Correct pricing language is displayed for split case values entered

Scenario: User can view CPP pricing language summary for distribution centers selected
    Then Correct pricing language is displayed for distribution centers selected

Scenario: User can view CPP pricing language summary for Gregorian Calendar
    Given User is on the Contract Information Pricing Stepper
    When User enters all CPP pricing information with cost schedule package as Gregorian Calendar
    Then Correct pricing language is displayed for Gregorian Calendar

Scenario: User can view CPP pricing language summary on selecting markup based on sell to Yes
    Given User is on the Contract Information Pricing Stepper
    When User enters all CPP pricing information with selecting markup based on sell to Yes
    Then Correct pricing language is displayed for markup based on sell selected as Yes

Scenario: User can view CPP pricing language summary on selecting markup based on sell to No
    Given User is on the Contract Information Pricing Stepper
    When User enters all CPP pricing information with selecting markup based on sell to No
    Then Correct pricing language is displayed for markup based on sell selected as No

Scenario: No Pricing language is displayed on selecting Formal Price Audit Privileges to No
    Given User is on the Contract Information Pricing Stepper
    When User enters all CPP pricing information with Formal Price Audit Privileges selected as No
    Then No Pricing language is displayed for Formal Price Audit Privileges selected as No

Scenario: No Pricing language is displayed on selecting Price Verification privileges to No
    Then No Pricing language is displayed for Price Verification privileges selected as No

Scenario: User can view CPP pricing language summary on selecting Formal Price Audit Privileges to Yes
    Given User is on the Contract Information Pricing Stepper
    When User enters all CPP pricing information with Formal Price Audit Privileges selected as Yes
    Then Correct pricing language is displayed for Formal Price Audit Privileges selected as Yes

Scenario: User can view CPP pricing language summary on selecting Price Verification privileges to Yes
    Then Correct pricing language is displayed for Price Verification privileges selected as Yes

Scenario: User can view CPP pricing language summary for last received cost Plus
    Then Correct pricing language is displayed when GFS Label Assessment Fee & Transfer Fee are selected

Scenario: User can view CPP pricing language summary for Last Received Audit
    Given User is on the Contract Information Pricing Stepper
    When User enters all CPP pricing information with only Transfer fee selected
    Then Correct pricing language is displayed when only Transfer fee is selected

Scenario: User can view CPP pricing language summary for Last Received Plus
    Given User is on the Contract Information Pricing Stepper
    When User enters all CPP pricing information with only GFS Label Assessment Fee selected
    Then Correct pricing language is displayed when only GFS Label Assessment Fee is selected
