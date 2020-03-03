Feature: Furtherance Creation
  
  As a Contract Pricing Profile Application user
  I should be able to get the default and saved values of contract pricing information
  for existing and new contracts,
  and I must be able to save/update the values of contract pricing information for furtherance.

  Scenario: Check for in progress furtherance when no furtherance exists for a contract
    When request is made to check existing furtherance
    Then return in progress flag for furtherance as "false"

  Scenario: Check for in progress furtherance when furtherance already exists for a contract
   	Given a request is made to save furtherance
    And request is made to check existing furtherance
    Then return in progress flag for furtherance as "true"

  Scenario: Get the default values of furtherance for new furtherance
    Given all pricing information for the contract is completed
    And the status of contract is in "PRICING_ACTIVATED"
    When a request is made to fetch the furtherance
    Then the default values of furtherance is returned

  Scenario Outline: <line> Validate the error message in case of an existing contract version in WIP is found
    Given all pricing information for the contract is completed
    And the status of contract is in <current_cpp_status>
    When a request is made to create furtherance
    Then display error message to user as furtherance can not be created

    Examples: 
      | line | current_cpp_status     |
      |    1 | "WAITING_FOR_APPROVAL" |
      |    2 | "CONTRACT_APPROVED"    |
      |    3 | "HOLD"                 |
      |    4 | "DRAFT"                |

  @clm_token-setup
  Scenario Outline: <line> Validate the error message in case of the latest price activated contract version is not in Executed status in CLM
    Given all pricing information for the contract is completed
    And the status of contract is in "PRICING_ACTIVATED"
    When the current status of contract in CLM is <current_clm_status>
    And a request is made to create furtherance
    Then display error message to user as furtherance can not be created

    Examples: 
      | line | current_clm_status               |
      |    1 | "WAITING_FOR_APPROVAL"           |
      |    2 | "APPROVED"                       |
      |    3 | "ON_HOLD"                        |
      |    4 | "DRAFT"                          |
      |    5 | "WAITING_FOR_INTERNAL_SIGNATURE" |
      |    6 | "WAITING_FOR_EXTERNAL_SIGNATURE" |
      |    7 | "REVIEW_PENDING"                 |
      |    8 | "TERMINATED"                     |
      |    9 | "EXPIRED"                        |
      |   10 | "DELETED"                        |
      |   11 | "CANCELLED"                      |

  Scenario: Validate the error message in case of view user
    Given all pricing information for the contract is completed
    And the user is logged in as "VIEW_USER" User
    When a request is made to create furtherance
    Then display error message to user as furtherance can not be created

  @clm_token-setup
  Scenario: Get the furtherance base information for new furtherance
    Given all pricing information for the contract is completed 
	And the status of contract is in "CONTRACT_APPROVED"
	And a request is made to save assignment with real customer 
	And request is made to activate pricing 
    And the current status of contract in CLM is "EXECUTED"
    When a request is made to create furtherance
    Then return furtherance info with new furtherance sequence created

  Scenario: Save the values of furtherance details and validate other contract pricing details are not updated
    Given a request is made to save furtherance
    And a request is made to fetch the furtherance
    Then saved furtherance information with initiated status is returned
    And no change on actual contract pricing details after furtherance

  Scenario: Update the values of furtherance details and validate other contract pricing details are not updated
    Given a request is made to save furtherance
    And a request is made to update the furtherance information
    And a request is made to fetch the furtherance
    Then updated furtherance information is returned
    And no change on actual contract pricing details after furtherance

  Scenario: Delete Item level markup for furtherance
    Given a request is made to save furtherance
    When a request is made to delete "ITEM_TYPE" for furtherance
    Then tracking is added for "DELETED" action for "ITEM_TYPE" type and "MARKUP" table
    And the CIP record for CMG should be expired for "ITEM_TYPE"
    
  Scenario: Update the values of Splitcase fee and track the values
    Given a request is made to save furtherance
    When a request is made to update splitcase in furtherance
    Then the values of splitcase are updated in furtherance
    And tracking is added for "UPDATED" action for "PRODUCT_TYPE" type and "SPLITCASE" table

  Scenario: Fetch items mapped with a future item for furtherance
    Given a request is made to save furtherance
    When a request is made to assign items with a future item for "ITEM_TYPE_FOR_FUTHERANCE"
    And a request is made to fetch items mapped with a future item for furtherance
    Then the mapped item details are returned

  Scenario: Assign item with a future item for furtherance
    Given a request is made to save furtherance
    When a request is made to assign items with a future item for "ITEM_TYPE_FOR_FUTHERANCE"
    Then a request is made to fetch items mapped with a future item for furtherance
    And the mapped item details are returned
    And the values of item markup are saved for future item for furtherance
    Then tracking is added for "ADDED" action for "ITEM_TYPE" type and "MARKUP" table

  Scenario: Validate already existing assign item with a future item for furtherance
    Given a request is made to save furtherance
    And a request is made to assign items with a future item for "ITEM_TYPE_FOR_FUTHERANCE"
    When a request with already existing items is made to assign items with future item for "ITEM_TYPE_FOR_ASSIGNMENT"
    Then the items already exist status returned for "ITEM_TYPE_FOR_ASSIGNMENT"

  Scenario: Validate duplicate items on list for assign item with a future item for furtherance
    Given a request is made to save furtherance
    And a request is made to assign items with a future item for "ITEM_TYPE_FOR_FUTHERANCE"
    When a request with duplicate items is made to assign items with future item for "ITEM_TYPE_FOR_FUTHERANCE"
    Then the items already exist status returned for "ITEM_TYPE_FOR_FUTHERANCE"

  Scenario: Delete new mapped item with a future item for furtherance
    Given a request is made to save furtherance
    And a request is made to assign items with a future item for "ITEM_TYPE_FOR_FUTHERANCE"
    When a request is made to delete "ITEM_TYPE_FOR_FUTHERANCE" with a future item for furtherance
    Then tracking is deleted for "ITEM_TYPE" type and "MARKUP" table for future item
    And the CIP record for CMG should be expired for "ITEM_TYPE_FOR_FUTHERANCE"
    And the future item mapping is expired for "ITEM_TYPE_FOR_FUTHERANCE"

  Scenario: Delete already mapped item with a future item for furtherance
    Given a request is made to save furtherance
    When a request is made to delete "ITEM_TYPE_FOR_ASSIGNMENT" with a future item for furtherance
    Then tracking is added for "DELETED" action for "ITEM_TYPE" type and "MARKUP" table
    And the CIP record for CMG should be expired for "ITEM_TYPE_FOR_ASSIGNMENT"
    And the future item mapping is expired for "ITEM_TYPE_FOR_ASSIGNMENT"

  Scenario: Activate Pricing button for furtherance is enabled when furtherance status is saved
    Given a request is made to save furtherance
    And the status of Furtherance is in "FURTHERANCE_SAVED"
    When a request is made to check activate pricing for furtherance is enabled
    Then return activate pricing flag for furtherance as "true"

  Scenario Outline: <line>  Activate Pricing button for furtherance is disabled when furtherance status is not saved
    Given a request is made to save furtherance
    And the status of Furtherance is in <furtherance_status>
    When a request is made to check activate pricing for furtherance is enabled
    Then return activate pricing flag for furtherance as "false"

    Examples: 
      | line | furtherance_status      |
      |    1 | "FURTHERANCE_INITIATED" |
      |    2 | "FURTHERANCE_ACTIVATED" |

  @clm_token-setup @language-setup
  Scenario: Attach Furtherance Document to CLM
    Given a request is made to save furtherance
    When a request is made to attach furtherance document to clm
    Then the furtherance document is attached in CLM
    And furtherance document GUID is saved in contract

  Scenario: Save markup for furtherance when an item is added
    Given a request is made to save furtherance
    When a request is made to save "ITEM_TYPE_FOR_FUTHERANCE" in markup for furtherance
    Then "ITEM_TYPE_FOR_FUTHERANCE" is saved for furtherance
    And tracking is added for "ADDED" action for "ITEM_TYPE" type and "MARKUP" table

  Scenario Outline: <line> Update markup for Furtherance
		Given a request is made to save furtherance
		When a request is made to update <TYPE_OF_ACCEPTABLE_ITEM> in markup for furtherance
		Then <TYPE_OF_ACCEPTABLE_ITEM> in markup is updated for furtherance  
		And tracking is added for "UPDATED" action for <TYPE_OF_ACCEPTABLE_ITEM> type and "MARKUP" table
		
	Examples: 
      | line | TYPE_OF_ACCEPTABLE_ITEM  |
      |    1 | "ITEM_TYPE" 				|  
      |	   2 | "PRODUCT_TYPE"           |
      |	   3 | "ITEM_TYPE_FOR_SUBGROUP"	|
		
	@clm_token-setup
  Scenario: Create furtherance for Amendment  
    Given all pricing information for the contract is completed 
		And the status of contract is in "CONTRACT_APPROVED" 
		And the current status of contract in CLM is "EXECUTED"
		And a request is made to save assignment with real customer 
		And request is made to activate pricing 
		When an amendment is created in CLM with current status as "DRAFT" 
		And request is sent to launch the amendment in CPP 
		And status of amendment in CLM is "EXECUTED"
		And the status of amendment in CPP is "CONTRACT_APPROVED"
		And request is made to activate pricing for amendment
		When a request is made to create furtherance
		Then return furtherance info with new furtherance sequence created for latest activated agreement
  		

  Scenario: Furtherance Pricing Activation - Validate CLM Status
    Given a request is made to save furtherance
    And the current status of contract in CLM is "WAITING_FOR_APPROVAL"
    When request is made to activate furtherance price activation for clm status as "WAITING_FOR_APPROVAL"
    And display message to user for original contract not in valid clm status

  Scenario: Furtherance Pricing Activation -  Validate CPP Furtherance Status
    Given a request is made to save furtherance
    Then update furtherance status to "FURTHERANCE_INITIATED"
    When request is made to activate furtherance price activation for clm status as "EXECUTED"
    And display message to user for furtherance not in valid furtherance status

  Scenario Outline: <line> Furtherance Pricing Activation - for deleted Item
    Given request is made to save the contract price including cip entries with date range as <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And the current status of contract in CLM is "EXECUTED"
    And a request is made to save furtherance information with effective date as <FURTHERANCE_EFCTV_DATE>
    When a request is made to delete "ITEM_TYPE" for furtherance
    Then tracking is added for "DELETED" action for "ITEM_TYPE" type and "MARKUP" table
    Then update furtherance status to "FURTHERANCE_SAVED"
    When request is made to activate furtherance price activation for clm status as "EXECUTED"
    Then existing customer entries for "REAL_CUSTOMER" in CIP table for "ITEM_TYPE" is not expired and has expiration date as <EXP_DATE_TO_SET>

    Examples: 
      | line | TYPE_OF_SCENARIO                       | CONTRACT_EFCTV_DATE | CONTRACT_EXP_DATE | EXP_DATE_TO_SET | FURTHERANCE_EFCTV_DATE |
      |    1 | "FUTURE EFFECTIVE AND EXPIRATION DATE" | 3 days ago          | 60 days from now  | 9 days from now | 10 days from now       |
      |    2 | "CURRENT EFFECTIVE DATE"               | 3 days ago          | 60 days from now  | 1 days ago      | 0 days from now        |

  Scenario Outline: <line> Furtherance Pricing Activation - for Adding Item
    Given request is made to save the contract price including cip entries with date range as <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And the current status of contract in CLM is <CLM_STATUS>
    And a request is made to save furtherance information with effective date as <FURTHERANCE_EFCTV_DATE>
    And a request is made to add "ITEM_TYPE_FOR_FUTHERANCE" for furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When request is made to activate furtherance price activation for clm status as <CLM_STATUS>
    Then existing customer entries for "REAL_CUSTOMER" in CIP table for "ITEM_TYPE" is not expired and has expiration date as <EXP_DATE_TO_SET>
    Then new entries for the customer for "ITEM_TYPE_FOR_FUTHERANCE" is <SAVED_OR_NOT_SAVED> to CIP tables with effective pricing as <FURTHERANCE_EFCTV_DATE>

    Examples: 
      | line | TYPE_OF_SCENARIO                       | CONTRACT_EFCTV_DATE | CONTRACT_EXP_DATE | EXP_DATE_TO_SET | FURTHERANCE_EFCTV_DATE | SAVED_OR_NOT_SAVED | CLM_STATUS   |
      |    1 | "FUTURE EFFECTIVE AND EXPIRATION DATE" | 3 days ago          | 60 days from now  | farOfDate       | 10 days from now       | SAVED              | "EXECUTED"   |
      |    2 | "CURRENT EFFECTIVE DATE"               | 3 days ago          | 60 days from now  | farOfDate       | 0 days from now        | SAVED              | "EXECUTED"   |
      |    1 | "FUTURE EFFECTIVE AND EXPIRATION DATE" | 3 days ago          | 60 days from now  | farOfDate       | 10 days from now       | SAVED              | "SUPERSEDED" |
      |    2 | "CURRENT EFFECTIVE DATE"               | 3 days ago          | 60 days from now  | farOfDate       | 0 days from now        | SAVED              | "SUPERSEDED" |

  Scenario: Furtherance Pricing Activation - for deleted & then adding Item back to markup
    Given request is made to save the contract price including cip entries with date range as 3_days_ago and 60_days_from_now
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And the current status of contract in CLM is "EXECUTED"
    And a request is made to save furtherance information with effective date as 10_days_from_now
    When a request is made to delete "ITEM_TYPE" for furtherance
    Then tracking is added for "DELETED" action for "ITEM_TYPE" type and "MARKUP" table
    Then update furtherance status to "FURTHERANCE_SAVED"
    And a request is made to add "ITEM_TYPE" for furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When request is made to activate furtherance price activation for clm status as "EXECUTED"
    Then existing customer entries for "REAL_CUSTOMER" in CIP table for "ITEM_TYPE" is not expired and has expiration date as 9_days_from_now
    Then new entries for the customer for "ITEM_TYPE" is SAVED to CIP tables with effective pricing as 10_days_from_now

  Scenario Outline: <line> Furtherance Pricing Activation - for Updating Item
    Given request is made to save the contract price including cip entries with date range as <CONTRACT_EFCTV_DATE> and <CONTRACT_EXP_DATE>
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And the current status of contract in CLM is "EXECUTED"
    And a request is made to save furtherance information with effective date as <FURTHERANCE_EFCTV_DATE>
    And a request is made to update "ITEM_TYPE" for furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When request is made to activate furtherance price activation for clm status as "EXECUTED"
    Then existing customer entries for furtherance for "REAL_CUSTOMER" in CIP table for "ITEM_TYPE" is expired with expiration date as <EXP_DATE_TO_SET>
    Then new entries for the customer for "ITEM_TYPE" is <SAVED_OR_NOT_SAVED> to CIP tables with effective pricing as <FURTHERANCE_EFCTV_DATE>

    Examples: 
      | line | TYPE_OF_SCENARIO                            | CONTRACT_EFCTV_DATE | CONTRACT_EXP_DATE | EXP_DATE_TO_SET   | FURTHERANCE_EFCTV_DATE | SAVED_OR_NOT_SAVED |
      |    1 | "NEAR FUTURE EFFECTIVE AND EXPIRATION DATE" | 3 days ago          | 60 days from now  | 9 days from now   | 10 days from now       | SAVED              |
      |    2 | "FAR OF EFFECTIVE DATE"                     | 3 days ago          | 60 days from now  | 100 days from now | 101 days from now      | SAVED              |

  Scenario: Furtherance Pricing Activation - Update Splitcase Fee
    Given request is made to save the contract price including cip entries with date range as 3_days_ago and 60_days_from_now
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And the current status of contract in CLM is "EXECUTED"
    And a request is made to save furtherance information with effective date as 10_days_from_now
    When a request is made to update splitcase in furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When request is made to activate furtherance price activation for clm status as "EXECUTED"
    And existing splitcase fee is expired with expiration date as 9_days_from_now
    And new record for splitcase fee is created with effective date as 10_days_from_now

  Scenario: Furtherance Pricing Activation - Update SubGroup
    Given request is made to save the contract price including cip entries with date range as 3_days_ago and 60_days_from_now
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And the current status of contract in CLM is "EXECUTED"
    And a request is made to save furtherance information with effective date as 10_days_from_now
    When a request is made to update "ITEM_TYPE_FOR_SUBGROUP" for furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When request is made to activate furtherance price activation for clm status as "EXECUTED"
    Then existing customer entries for furtherance for "REAL_CUSTOMER" in CIP table for "ITEM_TYPE_FOR_SUBGROUP" is expired with expiration date as 9_days_from_now
    And new record for subgroup is created with effective date as 10_days_from_now

  Scenario: Furtherance Pricing Activation - Update SubGroup Multiple times
    Given request is made to save the contract price including cip entries with date range as 3_days_ago and 60_days_from_now
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And the current status of contract in CLM is "EXECUTED"
    And a request is made to save furtherance information with effective date as 10_days_from_now
    When a request is made to update "ITEM_TYPE_FOR_SUBGROUP" for furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When a request is made to update "ITEM_TYPE_FOR_SUBGROUP" for furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When a request is made to update "ITEM_TYPE_FOR_SUBGROUP" for furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When request is made to activate furtherance price activation for clm status as "EXECUTED"
    Then existing customer entries for furtherance for "REAL_CUSTOMER" in CIP table for "ITEM_TYPE_FOR_SUBGROUP" is expired with expiration date as 9_days_from_now
    And new record for subgroup is created with effective date as 10_days_from_now

  Scenario: Furtherance Pricing Activation - Update ProductType
    Given request is made to save the contract price including cip entries with date range as 3_days_ago and 60_days_from_now
    And the status of contract is in "CONTRACT_APPROVED"
    And a request is made to save assignment with real customer
    And request is made to activate pricing
    And the current status of contract in CLM is "EXECUTED"
    And a request is made to save furtherance information with effective date as 10_days_from_now
    When a request is made to update "PRODUCT_TYPE" for furtherance
    Then update furtherance status to "FURTHERANCE_SAVED"
    When request is made to activate furtherance price activation for clm status as "EXECUTED"
    Then existing customer entries for furtherance for "REAL_CUSTOMER" in CIP table for "PRODUCT_TYPE" is expired with expiration date as 9_days_from_now
    And new record for product type is created with effective date as 10_days_from_now
