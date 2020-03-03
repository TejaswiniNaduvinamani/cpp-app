import { element, by } from 'protractor';

import { IdentificationType, CommonTasks } from './common-tasks';

// Component Locators
export const LocatorsM = {

    defaultGridFirstRowMarkupPercentIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[@class="datatable-body-cell-label"]/div[@id="radio-group"]/descendant::span[contains(text(), "%")])[1]'
    },

    defaultGridFirstRowMarkupDollarIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[@class="datatable-body-cell-label"]/div[@id="radio-group"]/descendant::span[contains(text(), "$")])[1]'
    },

    defaultGridFirstRowMarkupType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="markup-type"])[1]'
    },

    defaultGrid2ndRowMarkupType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="markup-type"])[2]'
    },

    expireLowerQuestionYes: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//label[@for="expireLowerYes"]/..'
    },

    expireLowerQuestionNo: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/label[@class="radio-label"][text()="No"]'
    },

    defaultGridFirstRowMarkupField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//input[@class="markup"])[1]'
    },

    pricingEffectiveDate: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'pricing-start-date-data'
    },

    pricingExpirationDate: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'pricing-end-date-data'
    },

    defaultGridFirstRowEffectiveDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//datatable-body-cell[count(//datatable-header-cell[descendant::span' + 
        '[contains(text(), "Effective Date")]]/preceding-sibling::datatable-header-cell) + 1]/descendant::div[last()])[1]'
    },

    defaultGridFirstRowExpirationDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//datatable-body-cell[count(//datatable-header-cell[descendant::span' +
            '[contains(text(), "Expiration Date")]]/preceding-sibling::datatable-header-cell) + 1]/descendant::div[last()])[1]'
    },

    defaultGridCopyToAllButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//datatable-body)[1]/descendant::button[contains(text(), "Copy To All")]'
    },

    defaultGridFirstRowErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="warning-text"])[1]'
    },

    defaultGridSaveButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="markupStructure0"]/descendant::button[contains(text(),"Save Markup")]'
    },

    defaultGridExpandCollapseArrow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//em)[1]'
    },

    addExceptionMarkupStructuresButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/button[contains(text(), "Add Exception Markup Structures")]'
    },

    popUpExceptionMarkupNameField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/input[@id="markup-name"]'
    },

    popUpExceptionAddButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/button[@type="submit"][contains(text(), "Add")]'
    },

    firstExceptionGridLabel: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//span[@class="accordion-title"])[2]'
    },

    firstExceptionGridExpandCollapseArrow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//em)[2]'
    },

    firstExceptionGridFirstRowMarkupField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '((.//datatable-body)[2]/descendant::input)[1]'
    },

    firstExceptionGridFirstRowMarkupDollarIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '((.//datatable-body)[2]/descendant::span[contains(text(), "$")])[1]'
    },

    firstExceptionGridFirstRowMarkupType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '((.//datatable-body)[2]/descendant::select)[1]'
    },

    firstExceptionGridCopyToAllButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//datatable-body)[2]/descendant::button[contains(text(), "Copy To All")]'
    },

    firstExceptionGridSaveButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//button[contains(text(), "Save Markup")])[2]'
    },

    firstExceptionGridSaveButtonAfterLabelChange: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/button[contains(text(), "Markup Saved")]'
    },

    copyMarkupInfoIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[@id="markupStructure1"]/div/img'
    },

    genericHoverOverText: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'tooltip-inner'
    },

    markupBasedOnSellToggle: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[descendant::span[contains(text(), "Markup based on Sell")]]/label/span[@class="slider round"]'
    },

    markupToggleWarningMessage: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'warning-text'
    },

    copyMarkupArrow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//em)[2]'
    },

    copyMarkupFirstDropdown: {
        type: IdentificationType[IdentificationType.Id],
        value: 'copyDropdown1'
    },

    copyMarkupFirstDropdown1stValue: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@id="copyDropdown1"]/option)[2]'
    },

    genericInfoIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div/div[descendant::span[contains(text(), "Replace_Text_Value")]]/descendant::img'
    },

    nextButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/button[contains(text(), "Save and Continue to: SplitCase")]'
    },

    furtheranceNextButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[contains(text(), "Continue to: Split Case Fee")]'
    },

    saveButtonEditException: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//BUTTON[text()="Save"]'
    },

    markupEditLink: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//a[text()="Edit"]'
    },

    firstExceptionMarupName: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//SPAN[text()="2"]/following-sibling::SPAN'
    },

    popUpExceptionEditMarkupNameField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//INPUT[@id="edit-markup"]'
    },

    clickingOutsideOfInputField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '// DIV[@id="markupModalLabel"][contains(text(), "Add")]'
    },

    duplicateNameErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[@class="text-danger help-block"]'
    },

    deleteButtonEditScreen: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/button/*[contains(text(), "Delete")]'
    },

    deleteConfirmationQuestion: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '// DIV[contains(text(), "Are you sure you would like to delete")]'
    },

    deleteButtonConfirmationQuestionYes: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="itemDeleteModal"]/descendant::button[contains(text(), "Delete")]'
    },

    defaultAddItemLevelMarkupButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//BUTTON[contains(text(), "Add Item-Level Markup")]'
    },

    defaultItemLevelDescription: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="markupStructure0"]/descendant::div[@class="item-level-desc"]'
    },

    defaultItemLevelFirstRowMarkupPercentIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::span[text()="%"][1]'
    },

    defaultItemLevelFirstRowMarkupDollarIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::span[text()="$"][1]'
    },

    defaultItemLevelFirstRowEffectiveDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value:
        '(.//app-item-level-markup)[1]/descendant::div[@class="datatable-row-center datatable-row-group"]' + 
        '[1]/descendant::div[@class="pt-1"][2]'
    },

    defaultItemLevelFirstRowExpirationDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value:
        '(.//app-item-level-markup)[1]/descendant::div[@class="datatable-row-center datatable-row-group"]' +
        '[1]/descendant::div[@class="pt-1"][3]'
    },

    defaultItemLevelFirstRowMarkupField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::input[@class="item-markup"][1]'
    },

    defaultItemLevel2ndRowMarkupField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::input[@class="markup"][last()]'
    },

    defaultItemLevelFirstRowMarkupType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::select[1]'
    },

    defaultItemLevelGrid: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]'
    },

    defaultItemLevelGrid2ndRow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-item-level-markup/descendant::datatable-row-wrapper[2]'
    },

    defaultItemLevelFirstRowItemIdField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::input[@class="item-id"][1]'
    },

    defaultItemLevelFirstRowItemDescriptionField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::div[contains(@class, "datatable-body-cell-label")][4]/div'
    },

    defaultItemLevelSecondRowItemIdField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::input[contains(@class, "item-id")][2]'
    },

    itemLevelDeleteButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="itemDeleteModal"]/descendant::button[text()="Delete"]'
    },

    allDefaultInputMarkupFields: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[contains(@id, "markupStructure0")]/descendant::div[@class="datatable-body-cell-label"]/input'
    },

    allDefaultMarkupTypeFields: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//datatable-body)[1]/descendant::select'
    },

    invalidItemCodeErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//span[@class="error-message"][text()="Please enter a valid item code"]'
    },

    inactiveItemCodeErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//span[@class="error-message"][text()="Please enter an active item code"]'
    },

    duplicateItemCodeErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//span[@class="error-message"][text()="This item has already been added"]'
    },

    allDefaultItemLevelRows: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-item-level-markup/descendant::datatable-body-row'
    },

    defaultMarkupMaximumItemLevelMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//span[@class="warning-text"]'
    },

    defaultItemLevelFirstRowNoItemIdCheckbox: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-item-level-markup/descendant::label[1]'
    },

    defaultItemLevel2ndRowNoItemIdCheckbox: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-item-level-markup/descendant::label[4]'
    },

    defaultItemLevelFirstRowItemDescription: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'item-description-input'
    },

    defaultItemLevel2ndRowItemDescription: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//input[@class="item-description-input"])[2]'
    },

    defaultItemLevel2ndRowTrashIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[@class="trash-icon"][@rowindex="1"]'
    },

    firstExceptionaAllInputMarkupFields: {
        type: IdentificationType[IdentificationType.ClassName],
        value: './/div[@class="datatable-body-cell-label"]/input'
    },

    firstExceptionAllInputMarkupUnits: {
        type: IdentificationType[IdentificationType.ClassName],
        value: './/div[@class="datatable-body-cell-label"]/input'
    },

    firstExceptionAllInputMarkupType: {
        type: IdentificationType[IdentificationType.ClassName],
        value: './/div[@class="datatable-body-cell-label"]/input'
    },

    allDefaultItemLevelInputFields: {
        type: IdentificationType[IdentificationType.ClassName],
        value: '//app-item-level-markup/descendant::datatable-body-cell/descendant::input[@type="text"]'
    },

    genericSaveMarkupButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/button[contains(text(), "Save Markup")]'
    },

    defaultItemLevel2ndRowItemIdField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-item-level-markup)[1]/descendant::datatable-body-row[2]/descendant::input[@class="item-id"]'
    },

    markupTypeModalPopup: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@class="modal fade show"]'
    },

    markupTypeModalPopupCloseButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//span[text()="×"])[1]'
    },

    defaultGridMarkupTypeHelperIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//img[@class="info-icon"])[1]'
    },

    defaultItemLevelMarkupTypeHelperIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//img[@class="info-icon"])[2]'
    },

    expireLowerQuestionSelectionYes: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="expireLowerYes"]/following-sibling::label'
    },

    subGroupTrashIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//img[@class="trash-icon"])[1]'
    },

    deleteConfirmationQuestionSubgroup : {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//DIV[contains(text(), "subgroup markup?")]'
    },

    defaultAddSubgroupButton : {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//BUTTON[contains(text(), "Add Subgroup Markup")])[1]'
    },

    default1stSubgroupRowMarkupValue: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="Contract-subgroup-markup"]/descendant::input[@type="text"]'
    },

    defaultItemLevel3rdRowTrashIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[@class="trash-icon"][@rowindex="2"]'
    },

    genericToastMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@class="toast-message"]'
    },

    editButtonFutureItemFurtherance: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//img[@class="pencil-icon"])[1]'
    },

    editPopupFutureItemFurtherance: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//DIV[@id="futureItemAssignmentModalLabel"]'
    },
}

export class MarkupPageObject extends CommonTasks {

    // Pricing Effective Date
    pricingEffectiveDate = this.elementLocator(LocatorsM.pricingEffectiveDate);

    // Pricing Expiration Date
    pricingExpirationDate = this.elementLocator(LocatorsM.pricingExpirationDate);

    // Default Grid First Row Effective Date
    defaultGridFirstRowEffectiveDate = this.elementLocator(LocatorsM.defaultGridFirstRowEffectiveDate);

    // Default Grid First Row Expiration Date
    defaultGridFirstRowExpirationDate = this.elementLocator(LocatorsM.defaultGridFirstRowExpirationDate);

    // Default Grid First Row Markup Field
    defaultGridFirstRowMarkupField = this.elementLocator(LocatorsM.defaultGridFirstRowMarkupField);

    // Default Grid First Row Dollar Icon
    defaultGridFirstRowMarkupDollarIcon = this.elementLocator(LocatorsM.defaultGridFirstRowMarkupDollarIcon);

    // Default Grid First Row Percent Icon
    defaultGridFirstRowMarkupPercentIcon = this.elementLocator(LocatorsM.defaultGridFirstRowMarkupPercentIcon);

    // Default Grid First Row Markup Type
    defaultGridFirstRowMarkupType = this.elementLocator(LocatorsM.defaultGridFirstRowMarkupType);

    // Default Grid 2nd Row Markup Type
    defaultGrid2ndRowMarkupType = this.elementLocator(LocatorsM.defaultGrid2ndRowMarkupType);

    // Expire Lower Toggle Yes
    expireLowerQuestionYes = this.elementLocator(LocatorsM.expireLowerQuestionYes);

    // Expire Lower Toggle No
    expireLowerQuestionNo = this.elementLocator(LocatorsM.expireLowerQuestionNo);

    // All Default Input Markup Fields Xpath
    allDefaultInputMarkupFields = LocatorsM.allDefaultInputMarkupFields.value;

    // All Default Markup Type Fields
    allDefaultMarkupTypeFields = LocatorsM.allDefaultMarkupTypeFields.value;

    // Default Grid Copy To All Button
    defaultGridCopyToAllButton = this.elementLocator(LocatorsM.defaultGridCopyToAllButton);

    // First Row Error Message
    defaultGridFirstRowErrorMessage = this.elementLocator(LocatorsM.defaultGridFirstRowErrorMessage);

    // Add Exception Markup Structures Button
    addExceptionMarkupStructuresButton = this.elementLocator(LocatorsM.addExceptionMarkupStructuresButton);

    // Pop-Up Exception Markup Name Field
    popUpExceptionMarkupNameField = this.elementLocator(LocatorsM.popUpExceptionMarkupNameField);

    // Pop-Up Exception Add Button
    popUpExceptionAddButton = this.elementLocator(LocatorsM.popUpExceptionAddButton);

    // First Exception Markup Label
    firstExceptionMarkupLabel = this.elementLocator(LocatorsM.firstExceptionGridLabel);

    // First Exception Markup Expand Collapse Arrow
    defaultGridExpandCollapseArrow = this.elementLocator(LocatorsM.defaultGridExpandCollapseArrow);

    // Default Markup Save Button
    defaultGridSaveButton = this.elementLocator(LocatorsM.defaultGridSaveButton);

    // First Exception Markup Expand Collapse Arrow
    firstExceptionGridExpandCollapseArrow = this.elementLocator(LocatorsM.firstExceptionGridExpandCollapseArrow);

    // First Exception Grid First Row Markup
    firstExceptionGridFirstRowMarkupField = this.elementLocator(LocatorsM.firstExceptionGridFirstRowMarkupField);

    // First Exception Markup Expand Collapse Arrow
    firstExceptionGridFirstRowMarkupDollarIcon = this.elementLocator(LocatorsM.firstExceptionGridFirstRowMarkupDollarIcon);

    // First Exception Markup Expand Collapse Arrow
    firstExceptionGridFirstRowMarkupType = this.elementLocator(LocatorsM.firstExceptionGridFirstRowMarkupType);

    // First Exception Grid Copy To All Button
    firstExceptionGridCopyToAllButton = this.elementLocator(LocatorsM.firstExceptionGridCopyToAllButton);

    // First Exception Grid Save Button
    firstExceptionGridSaveButton = this.elementLocator(LocatorsM.firstExceptionGridSaveButton);

    // First Exception Grid Save Button After Label Change
    firstExceptionGridSaveButtonAfterLabelChange = this.elementLocator(LocatorsM.firstExceptionGridSaveButtonAfterLabelChange);

    // Markup Based on Sell
    markupBasedOnSellToggle = this.elementLocator(LocatorsM.markupBasedOnSellToggle);

    // Warning Message
    markupToggleWarningMessage = this.elementLocator(LocatorsM.markupToggleWarningMessage);

    // Copy Markup First Dropdown
    copyMarkupFirstDropdown = this.elementLocator(LocatorsM.copyMarkupFirstDropdown);

    // Copy Markup 1st Dropdown 1st Value
    copyMarkupFirstDropdown1stValue = this.elementLocator(LocatorsM.copyMarkupFirstDropdown1stValue);

    // Generic Hover
    genericHoverOver = this.elementLocator(LocatorsM.genericHoverOverText);

    // Newly Added Markup field Values
    firstExceptionaAllInputMarkupFields = LocatorsM.firstExceptionaAllInputMarkupFields.value;

    // Newly Added Markup Unit Values
    firstExceptionAllInputMarkupUnits = LocatorsM.firstExceptionAllInputMarkupUnits.value;

    // Newly added Mark up type
    firstExceptionAllInputMarkupType = LocatorsM.firstExceptionAllInputMarkupType.value;

    // Newly Added markup stucture expand arrow
    copyMarkupArrow = this.elementLocator(LocatorsM.copyMarkupArrow);

    // Copy Markup info Icon
    copyMarkupInfoIcon = this.elementLocator(LocatorsM.copyMarkupInfoIcon);

    // Next Button to go Split Case Stepper
    nextButton = this.elementLocator(LocatorsM.nextButton);

    // Edit Button in the markup screem
    markupEditLink = this.elementLocator(LocatorsM.markupEditLink);

    // Save button in edit mark up popup
    saveButtonEditException = this.elementLocator(LocatorsM.saveButtonEditException);

    // Markup Name
    firstExceptionMarupName = this.elementLocator(LocatorsM.firstExceptionMarupName);

    // Edit Name Feild
    popUpExceptionEditMarkupNameField = this.elementLocator(LocatorsM.popUpExceptionEditMarkupNameField);

    // Clicking outside of Inputfeild
    clickingOutsideOfInputField = this.elementLocator(LocatorsM.clickingOutsideOfInputField);

    // Duplicate MarkupName Error Message
    duplicateNameErrorMessage = this.elementLocator(LocatorsM.duplicateNameErrorMessage);

    // Delete Button Edit Screen
    deleteButtonEditScreen = this.elementLocator(LocatorsM.deleteButtonEditScreen);

    // Delete Confirmation Question Text
    deleteConfirmationQuestion = this.elementLocator(LocatorsM.deleteConfirmationQuestion);

    // Delete Button in Confirmation Question
    deleteButtonConfirmationQuestionYes = this.elementLocator(LocatorsM.deleteButtonConfirmationQuestionYes);

    // Default Add Item Level Markup Button
    defaultAddItemLevelMarkupButton = this.elementLocator(LocatorsM.defaultAddItemLevelMarkupButton);

    // Default Item Level Description
    defaultItemLevelDescription = this.elementLocator(LocatorsM.defaultItemLevelDescription);

    // Default Item Level 2nd Row Trash Icon
    defaultItemLevel2ndRowTrashIcon = this.elementLocator(LocatorsM.defaultItemLevel2ndRowTrashIcon);

    // Default Item Level 2nd Row Item Description
    defaultItemLevel2ndRowItemDescription = this.elementLocator(LocatorsM.defaultItemLevel2ndRowItemDescription);

    // Default Item Level 2nd Row No Item Id Checkbox
    defaultItemLevel2ndRowNoItemIdCheckbox = this.elementLocator(LocatorsM.defaultItemLevel2ndRowNoItemIdCheckbox);

    // Default Item level Grid First Row Percent Icon
    defaultItemLevelFirstRowMarkupPercentIcon = this.elementLocator(LocatorsM.defaultItemLevelFirstRowMarkupPercentIcon);

    // Default Item Level First Row Dollar Icon
    defaultItemLevelFirstRowMarkupDollarIcon = this.elementLocator(LocatorsM.defaultItemLevelFirstRowMarkupDollarIcon);

    // Default Item Level First Row Effective Date
    defaultItemLevelFirstRowEffectiveDate = this.elementLocator(LocatorsM.defaultItemLevelFirstRowEffectiveDate);

    // Default Item Level First Row Expiration Date
    defaultItemLevelFirstRowExpirationDate = this.elementLocator(LocatorsM.defaultItemLevelFirstRowExpirationDate);

    // Default Item Level First Row Markup Field
    defaultItemLevelFirstRowMarkupField = this.elementLocator(LocatorsM.defaultItemLevelFirstRowMarkupField);

    // Default Item Level 2nd Row Markup Field
    defaultItemLevel2ndRowMarkupField = this.elementLocator(LocatorsM.defaultItemLevel2ndRowMarkupField);

    // Default Item Level First Row Markup Type
    defaultItemLevelFirstRowMarkupType = this.elementLocator(LocatorsM.defaultItemLevelFirstRowMarkupType);

    // Default Item Level Grid
    defaultItemLevelGrid = this.elementLocator(LocatorsM.defaultItemLevelGrid);

    // Default Item Level Grid 2nd Row
    defaultItemLevelGrid2ndRow = this.elementLocator(LocatorsM.defaultItemLevelGrid2ndRow);

    // Default Item Level First Item ID Field
    defaultItemLevelFirstRowItemIdField = this.elementLocator(LocatorsM.defaultItemLevelFirstRowItemIdField);

    // Default Item Level First Item Description Field
    defaultItemLevelFirstRowItemDescriptionField = this.elementLocator(LocatorsM.defaultItemLevelFirstRowItemDescriptionField);

    // Default Item Level Second Item ID Field
    defaultItemLevelSecondRowItemIdField = this.elementLocator(LocatorsM.defaultItemLevelSecondRowItemIdField);

    // Item Level Delete Button
    itemLevelDeleteButton = this.elementLocator(LocatorsM.itemLevelDeleteButton);

    // Invalid Item Code error Message
    invalidItemCodeErrorMessage = this.elementLocator(LocatorsM.invalidItemCodeErrorMessage);

    // Inactive Item Code error Message
    inactiveItemCodeErrorMessage = this.elementLocator(LocatorsM.inactiveItemCodeErrorMessage);

    // Duplicate Item Code error Message
    duplicateItemCodeErrorMessage = this.elementLocator(LocatorsM.duplicateItemCodeErrorMessage);

    // All Default Item Level Rows
    allDefaultItemLevelRows = LocatorsM.allDefaultItemLevelRows.value;

    // All Default Item Level Input Fields
    allDefaultItemLevelInputFields = LocatorsM.allDefaultItemLevelInputFields.value;

    // All Default Item Level Rows
    defaultMarkupMaximumItemLevelMessage = this.elementLocator(LocatorsM.defaultMarkupMaximumItemLevelMessage);

    // Default Item Level First Row No Item Id Checkbox
    defaultItemLevelFirstRowNoItemIdCheckbox = this.elementLocator(LocatorsM.defaultItemLevelFirstRowNoItemIdCheckbox);

    // Default Item Level First Row Item Description
    defaultItemLevelFirstRowItemDescription = this.elementLocator(LocatorsM.defaultItemLevelFirstRowItemDescription);

    // Default Item Level 2nd Row Item Id Field
    defaultItemLevel2ndRowItemIdField = this.elementLocator(LocatorsM.defaultItemLevel2ndRowItemIdField);

    // Generic Save Markup Button
    genericSaveMarkupButton = this.elementLocator(LocatorsM.genericSaveMarkupButton);

    // Markup Type Modal Popup
    markupTypeModalPopup = this.elementLocator(LocatorsM.markupTypeModalPopup);

    // Markup Type Modal Popup Close Button
    markupTypeModalPopupCloseButton = this.elementLocator(LocatorsM.markupTypeModalPopupCloseButton);

    // Markup Type Modal Popup Close Button
    defaultGridMarkupTypeHelperIcon = this.elementLocator(LocatorsM.defaultGridMarkupTypeHelperIcon);

    // Default Item Level Markup Type Helper Icon
    defaultItemLevelMarkupTypeHelperIcon = this.elementLocator(LocatorsM.defaultItemLevelMarkupTypeHelperIcon);

    // Exprire Lower Question Yes
    expireLowerQuestionSelectionYes = this.elementLocator(LocatorsM.expireLowerQuestionSelectionYes);

    // Sub Group Delete Icon
    subGroupTrashIcon = this.elementLocator(LocatorsM.subGroupTrashIcon);

    // Deelete Confirmation Question Subgroup Markup
    deleteConfirmationQuestionSubgroup = this.elementLocator(LocatorsM.deleteConfirmationQuestionSubgroup);

    // Deafult Add Subgroup level Id
    defaultAddSubgroupButton = this.elementLocator(LocatorsM.defaultAddSubgroupButton);

    // Default 1stSubgroupRowMarkupValue
    default1stSubgroupRowMarkupValue = this.elementLocator(LocatorsM.default1stSubgroupRowMarkupValue);

    // Default Item Level 3rd Row Trash Icon
    defaultItemLevel3rdRowTrashIcon = this.elementLocator(LocatorsM.defaultItemLevel3rdRowTrashIcon);

    // Generic Toast Message
    genericToastMessage = this.elementLocator(LocatorsM.genericToastMessage);

    // Furtherance Next Button
    furtheranceNextButton = this.elementLocator(LocatorsM.furtheranceNextButton);
    // Edit Icon for Future Item in Furthurance Mode
    editButtonFutureItemFurtherance = this.elementLocator(LocatorsM.editButtonFutureItemFurtherance);

    // Edit Popupfor Future Item in Furthurance Mode
    editPopupFutureItemFurtherance = this.elementLocator(LocatorsM.editPopupFutureItemFurtherance);

    // Helper Text
    identifyGenericInfoIcon(textReplaceValue) {
        return element(by.xpath(LocatorsM.genericInfoIcon.value.replace('Replace_Text_Value', textReplaceValue)));
    }

}
