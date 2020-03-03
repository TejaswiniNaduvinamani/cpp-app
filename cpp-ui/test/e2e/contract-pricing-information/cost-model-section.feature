@cost-model-section-validation
Feature: Cost Model Validation

  Scenario Outline: Cost Model options are not visible when Price Verification Privileges and Formal Price Audit Privileges are selected as 'No'
    Given User is on the Contract Information Pricing Stepper
    When Answers yes to the question to have both price verification and formal price audit privileges option
      And "<toggle>" is selected as No on the Customer Information Screen
    Then Cost Model Section is not displayed to the User

    Examples:
    | toggle |
    | Price Verification Privileges |
    | Formal Price Audit Privileges |
  
  Scenario Outline: Cost Model options are visible if Price Verification Privileges or Formal Price Audit Privileges is selected as 'Yes'
    Given User is on the Contract Information Pricing Stepper
    When Answers yes to the question to have both price verification and formal price audit privileges option
      And "<toggle>" is selected as Yes on the Customer Information Screen
    Then Cost Model Section is displayed to the User

    Examples:
    | toggle |
    | Price Verification Privileges |
    | Formal Price Audit Privileges |

  Scenario: Hover over info icon for Include Transfer Fees toggle displays help text
    When User hovers over info icon for Transfer Fees Toggle
    Then Help text for Transfer Fees displayed to User

  Scenario: Hover over info icon for GFS Label Assessment Fee toggle displays help text
    When User hovers over info icon for GFS Label Assessment Fee Toggle
    Then Help text for GFS Label Assessment Fee displayed to User

  Scenario: When Include Transfer Fees is No, a warning appears that an extra level of Approval is needed
    When User selects Include Transfer Fee Toggle as No
    Then Warning text is displayed to the User that an extra level of approval is required
