import { element, by } from 'protractor';

import { IdentificationType, CommonTasks } from './common-tasks';

// Component Locators
export const LocatorsAt = {

    assignments1stCMGName: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@id="assignmentStructure0"]/descendant::div[contains(text(), "Customer Name")]'
    },

    assignments1stCMGTypeAndId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//span[@class="pull-right customer-details"][1]'
    },

    assignments1stAllCMGMarkupValues: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/app-markup-display/descendant::div[@class="row"]/div[2]'
    },

    defaultGridCustomerType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//select[@name="customerType"])[1]'
    },

    defaultGridCustomerId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//input[@name="customerID"])[1]'
    },

    defaultGridFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//button[contains(text(), "Find")])[1]'
    },

    defaultGridCustomerName: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[@class="customer-name-value"])[1]'
    },

    firstExceptionGrid1stRowCustomerType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::select[@name="customerType"][1]'
    },

    firstExceptionGrid1stRowCustomerId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::input[@name="customerID"][1]'
    },

    firstExceptionGrid1stRowFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::button[text()="Find"][1]'
    },

    firstExceptionGrid1stRowCustomerName: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::div[@class="customer-name-value"][1]'
    },

    firstExceptionGrid2ndRowCustomerType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::select[@name="customerType"][2]'
    },

    firstExceptionGrid2ndRowCustomerId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::input[@name="customerID"][2]'
    },

    firstExceptionGrid2ndRowFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::button[text()="Find"][2]'
    },

    firstExceptionGrid2ndRowCustomerName: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::div[@class="customer-name-value"][2]'
    },

    firstExceptionGrid1stRowErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::div[contains(@class, "invalid-customer-warning")][1]'
    },

    firstException2ndRowFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::button[text()="Find"][2]'
    },

    firstExceptionGridAllRows: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::button[text()="Find"]'
    },

    firstExceptionGrid1stRowDeleteButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::img[contains(@class, "delete")][1]'
    },

    defaultGridSaveAssignmentsButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[1]/descendant::button[contains(text(), "Save Assignments")][1]'
    },

    firstExceptionGridAddAnotherButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//app-customer-assignments/..)[2]/descendant::button[contains(text(), "Add Customer")]'
    },

    genericAssignmentsItemLevelRow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/tr[descendant::td[contains(text(), "Replace_Text_Value")]]'
    },

    genericSaveSuccessfulMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="toast-container"]'
    },

    itemsTab: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//A[text()="Items"]'
    },

    futureItemExpandCollapsearrow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//em)[1]'
    },

    secondFutureItemExpandCollapseArrow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="futureItemAccordion"]/div[2]/descendant::a[@data-toggle="collapse"]'
    },

    modalPopupSaveButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//div[@class="modal-footer"]/button[text()="Save"])[1]'
    },

    defaultGrid1stRowTrashIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//img[contains(@class, "trash-icon")])[1]'
    },

    genericDeleteConfirmationPopup: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="modal-content"])[1]'
    },

    genericDeleteConfirmationPopupYesButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[contains(@class, "modal-footer")]/descendant::button[text()="Delete"])[1]'
    },

    genericDeleteConfirmationPopupNoButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//u[text()="Cancel"])[1]'
    },

    defaultGrid1stRow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[@id="assignmentStructure0"]/descendant::div[@class="row" and descendant::select])[2]'
    },

    firstFutureConcept3rdItemId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: ''
    },

    firstFutureConceptAddAnotherButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//BUTTON[text()="Add Item"])[1]'
    },

    firstFutureConcept2ndRow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '((//BUTTON[text()="Add Item"])[1]/../../..//INPUT[@id="itemId"])[2]'
    },

    firstFutureConcept1stItemId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '((//BUTTON[text()="Add Item"])[1]/../../..//INPUT[@id="itemId"])[1]'
    },

    firstFutureConcepts1stFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//BUTTON[text()="Find"])[1]'
    },

    firstFutureConcept2ndItemId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '((//BUTTON[text()="Add Item"])[1]/../../..//INPUT[@id="itemId"])[2]'
    },

    firstFutureConcept2ndFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//BUTTON[text()="Find"])[2]'
    },

    firstFutureConceptSaveAssignmentsButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//BUTTON[text()="Save Assignments"])[1]'
    },

    firstFutureConcept3rdItemErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//DIV[text()="Please enter a valid item code"]'
    },

    defaultGridExpandCollapseArrowItemsTab: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//em)[1]'
    },

    itemsTabGenericSaveSuccessfulMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@class="toast-message"][contains(text()="item assignments"]'
    },

    exceptionGridSaveAssignmentsButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//BUTTON[text()="Save Assignments"])[2]'
    },

    firstexceptionGridExpandCollapseArrow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//em)[2]'
    },

    exceptionmodalPopupSaveButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//div[@class="modal-footer"]/button[text()="Save"])[2]'
    },

    activatePriceProfileButton: {
        type: IdentificationType[IdentificationType.ButtonText],
        value: 'Activate Price Profile'
    },

    priceActivationSuccessMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[@class="toast-top-right"]'
    },

    firstFutureConcept1stItemErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="futureItemStructure0"]/descendant::div[@class="item-error"]'
    },

    firstFutureConcept2ndItemErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[@class="item-error"][contains(text(), "already exists")]'
    },

    firstFutureConcept2ndItemInvalidErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[@class="item-error"][contains(text(), "enter a valid item code")]'
    },

    firstFutureConceptSaveButtonErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="futureItemStructure0"]/descendant::span[@class="item-error"]'
    },

    firstFutureConcept1stItemDeleteIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="futureItemStructure0"]/descendant::img[contains(@class, "delete")][1]'
    },

    firstFutureConceptSaveConfirmation: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="saveItemModal0"]/descendant::button[@class="btn save"]'
    },

    firstExceptionSaveButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="assignmentStructure1"]/descendant::button[text()="Save Assignments"]'
    },

    deleteDefaultAssignmentConfirmationButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="assignmentStructure0"]/descendant::button[text()="Delete"]'
    },

    defaultAssignmentRemovalPopupConfirmation: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="deleteDefaultRealCustomer"][@class="modal fade show"]'
    },

    defaultGridFindButtonCount: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="assignmentStructure0"]/descendant::button[text()="Find"]'
    },

    defaultAssignmentRemovalPopupConfirmationYesButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="assignmentStructure0"]/descendant::button[@class="btn delete-customer"]'
    },

    exceptionGridFindButtonCount: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="assignmentStructure1"]/descendant::button[text()="Find"]'
    },

    AssignmentRemovalPopupConfirmation: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(//BUTTON[text()="Delete"])[2]'
    },

    unAssignedMarkupIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//span[@class="circle"]'
    },

    defaultGridAddAnotherButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@id="assignmentStructure0"]/descendant::button[text()="Add Customer"]'
    },

    firstExceptionGrid4thRow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@id="assignmentStructure1"]/descendant::button[text()="Find"]'
    },

    firstExceptionGrid4thRowDeleteButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="assignmentStructure1"]/descendant::img[@alt="clear icon"]'
    },

    itemsTabModalPopupSaveConfimration: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@id="saveItemModal1"]/descendant::div[@class="modal-content"]'
    },

    secondFutureConceptAddItemButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="futureItemStructure1"]/descendant::button[text()="Add Item"]'
    },

    secondFutureConcept1stItemId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="futureItemStructure1"]/descendant::input[@id="itemId"][1]'
    },

    secondFutureConcepts1stFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="futureItemStructure1"]/descendant::button[text()="Find"][1]'
    },

    secondFutureConcept2ndItemId: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="futureItemStructure1"]/descendant::input[@id="itemId"][2]'
    },

    secondFutureConcept2ndFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[@id="futureItemStructure1"]/descendant::button[text()="Find"][2]'
    },

    secondFutureConceptSaveAssignmentsButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@id="futureItemStructure1"]/descendant::button[text()="Save Assignments"]'
    },

    thirdExceptionExpandCollapseArrow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="assignmentAccordion"]/descendant::em[3]'
    },

    thirdException1stRowCustomerType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="assignmentStructure2"]/descendant::select[@name="customerType"]'
    },

    thirdException1stRowCustomerIdField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="assignmentStructure2"]/descendant::input[@name="customerID"]'
    },

    thirdException1stRowFindButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="assignmentStructure2"]/descendant::button[contains(text(), "Find")]'
    },

    thirdExceptionSaveButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="assignmentStructure2"]/descendant::button[contains(text(), "Save Assignments")]'
    },

    thirdExceptionModalPopupSaveButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="saveAssignmentModal2"]/descendant::button[text()="Save"]'
    },

    

}

export class AssignmentsPageObject extends CommonTasks {

    // Custmer Name in Assignment Tab
    assignments1stCMGName = this.elementLocator(LocatorsAt.assignments1stCMGName);

    // Custmer Type & Id in Assignment Tab
    assignments1stCMGTypeAndId = this.elementLocator(LocatorsAt.assignments1stCMGTypeAndId);

    // All markup values field in assignment tab
    assignments1stCMGAllMarkupValues = LocatorsAt.assignments1stAllCMGMarkupValues.value;

    // Default Grid Customer Type
    defaultGridCustomerType = this.elementLocator(LocatorsAt.defaultGridCustomerType);

    // Default Grid CustomerId
    defaultGridCustomerId = this.elementLocator(LocatorsAt.defaultGridCustomerId);

    // Default Grid Find Button
    defaultGridFindButton = this.elementLocator(LocatorsAt.defaultGridFindButton);

    // Default Grid Customer Name
    defaultGridCustomerName = this.elementLocator(LocatorsAt.defaultGridCustomerName);

    // First Exception Grid 1st Row Customer Type
    firstExceptionGrid1stRowCustomerType = this.elementLocator(LocatorsAt.firstExceptionGrid1stRowCustomerType);

    // First Exception Grid 1st Row Customer Id
    firstExceptionGrid1stRowCustomerId = this.elementLocator(LocatorsAt.firstExceptionGrid1stRowCustomerId);

    // First Exception Grid 1st Row Find Button
    firstExceptionGrid1stRowFindButton = this.elementLocator(LocatorsAt.firstExceptionGrid1stRowFindButton);

    // First Exception Grid 1st Row Customer Name
    firstExceptionGrid1stRowCustomerName = this.elementLocator(LocatorsAt.firstExceptionGrid1stRowCustomerName);

    // First Exception Grid 2nd Row Customer Type
    firstExceptionGrid2ndRowCustomerType = this.elementLocator(LocatorsAt.firstExceptionGrid2ndRowCustomerType);

    // First Exception Grid 2nd Row Customer Id
    firstExceptionGrid2ndRowCustomerId = this.elementLocator(LocatorsAt.firstExceptionGrid2ndRowCustomerId);

    // First Exception Grid 2nd Row Find Button
    firstExceptionGrid2ndRowFindButton = this.elementLocator(LocatorsAt.firstExceptionGrid2ndRowFindButton);

    // First Exception Grid 2nd Row Customer Name
    firstExceptionGrid2ndRowCustomerName = this.elementLocator(LocatorsAt.firstExceptionGrid2ndRowCustomerName);

    // First Exception Grid 1st Row Error Message
    firstExceptionGrid1stRowErrorMessage = this.elementLocator(LocatorsAt.firstExceptionGrid1stRowErrorMessage);

    // First Exception 2nd Row Find Button
    firstException2ndRowFindButton = this.elementLocator(LocatorsAt.firstException2ndRowFindButton);

    // First Exception Grid All Rows
    firstExceptionGridAllRows = LocatorsAt.firstExceptionGridAllRows.value;

    // First Exception Grid 1st Row Delete Button
    firstExceptionGrid1stRowDeleteButton = this.elementLocator(LocatorsAt.firstExceptionGrid1stRowDeleteButton);

    // Default Grid Save Assignments Button
    defaultGridSaveAssignmentsButton = this.elementLocator(LocatorsAt.defaultGridSaveAssignmentsButton);

    // First Exception Grid Add Another Button
    firstExceptionGridAddAnotherButton = this.elementLocator(LocatorsAt.firstExceptionGridAddAnotherButton);

    // Generic Assignments Item Level Row
    genericAssignmentsItemLevelRow = LocatorsAt.genericAssignmentsItemLevelRow.value;

    // Generic Save Successful Message
    genericSaveSuccessfulMessage = this.elementLocator(LocatorsAt.genericSaveSuccessfulMessage);

    // Items Tab
    itemsTab = this.elementLocator(LocatorsAt.itemsTab);

    // Items Tab Future Item acccordion
    futureItemExpandCollapsearrow = this.elementLocator(LocatorsAt.futureItemExpandCollapsearrow);

    // Modal Popup Save Button
    modalPopupSaveButton = this.elementLocator(LocatorsAt.modalPopupSaveButton);

    // Default Grid 1st Row Trash Icon
    defaultGrid1stRowTrashIcon = this.elementLocator(LocatorsAt.defaultGrid1stRowTrashIcon);

    // Generic Delete Confirmation Popup
    genericDeleteConfirmationPopup = this.elementLocator(LocatorsAt.genericDeleteConfirmationPopup);

    // Generic Delete Confirmation Popup Yes Button
    genericDeleteConfirmationPopupYesButton = this.elementLocator(LocatorsAt.genericDeleteConfirmationPopupYesButton);

    // Generic Delete Confirmation Popup No Button
    genericDeleteConfirmationPopupNoButton = this.elementLocator(LocatorsAt.genericDeleteConfirmationPopupNoButton);

    // Default Grid 1st Row
    defaultGrid1stRow = this.elementLocator(LocatorsAt.defaultGrid1stRow);

    // First Future Concept 3rd Item Id
    firstFutureConcept3rdItemId = this.elementLocator(LocatorsAt.firstFutureConcept3rdItemId);

    // First Future Concept Add Another Button
    firstFutureConceptAddAnotherButton = this.elementLocator(LocatorsAt.firstFutureConceptAddAnotherButton);

    // First Future Concept 2nd Row
    firstFutureConcept2ndRow = this.elementLocator(LocatorsAt.firstFutureConcept2ndRow);

    // FirstFutureConcept1stItemId
    firstFutureConcept1stItemId = this.elementLocator(LocatorsAt.firstFutureConcept1stItemId);

    // FirstFutureConcepts1stFindButton
    firstFutureConcepts1stFindButton = this.elementLocator(LocatorsAt.firstFutureConcepts1stFindButton);

    // FirstFutureConcept2ndItemId
    firstFutureConcept2ndItemId = this.elementLocator(LocatorsAt.firstFutureConcept2ndItemId);

    // FirstFutureConcepts2ndFindButton
    firstFutureConcept2ndFindButton = this.elementLocator(LocatorsAt.firstFutureConcept2ndFindButton);

    // FirstFutureConceptSaveAssignmentsButton
    firstFutureConceptSaveAssignmentsButton = this.elementLocator(LocatorsAt.firstFutureConceptSaveAssignmentsButton);

    // FirstFutureConcept3rdItemErrorMessage
    firstFutureConcept3rdItemErrorMessage = this.elementLocator(LocatorsAt.firstFutureConcept3rdItemErrorMessage);

    // Items Tab Generic Save Successful Message
    itemsTabGenericSaveSuccessfulMessage = this.elementLocator(LocatorsAt.itemsTabGenericSaveSuccessfulMessage);

    // Default grid expand collaps row items tab
    defaultGridExpandCollapseArrowItemsTab = this.elementLocator(LocatorsAt.defaultGridExpandCollapseArrowItemsTab);

    // Exception grid save button
    exceptionGridSaveAssignmentsButton = this.elementLocator(LocatorsAt.exceptionGridSaveAssignmentsButton);

    // First Exception Grid Expand Collapse Arrow
    firstexceptionGridExpandCollapseArrow = this.elementLocator(LocatorsAt.firstexceptionGridExpandCollapseArrow);

    // Modal popup save button exception grid
    exceptionmodalPopupSaveButton = this.elementLocator(LocatorsAt.exceptionmodalPopupSaveButton);

    // Activate Price Profile Button
    activatePriceProfileButton = this.elementLocator(LocatorsAt.activatePriceProfileButton);

    // Activate Price Profile Button
    priceActivationSuccessMessage = this.elementLocator(LocatorsAt.priceActivationSuccessMessage);

    // Default Future Item 1st Item Error Message
    firstFutureConcept1stItemErrorMessage = this.elementLocator(LocatorsAt.firstFutureConcept1stItemErrorMessage);

    // Default Future Save Button Error Message
    firstFutureConceptSaveButtonErrorMessage = this.elementLocator(LocatorsAt.firstFutureConceptSaveButtonErrorMessage);

    // Default Future Item 1st Item Delete Icon
    firstFutureConcept1stItemDeleteIcon = this.elementLocator(LocatorsAt.firstFutureConcept1stItemDeleteIcon);

    // First Future Concept Save Confirmation
    firstFutureConceptSaveConfirmation = this.elementLocator(LocatorsAt.firstFutureConceptSaveConfirmation);

    // First Exception Save Button
    firstExceptionSaveButton = this.elementLocator(LocatorsAt.firstExceptionSaveButton);

    // First exception markup confirmation question
    defaultAssignmentRemovalPopupConfirmation = this.elementLocator(LocatorsAt.AssignmentRemovalPopupConfirmation);

    // Default Grid Find Button Count
    defaultGridFindButtonCount = LocatorsAt.defaultGridFindButtonCount.value;

    // Default Assignment Removal Popup Confirmation Yes Button
    defaultAssignmentRemovalPopupConfirmationYesButton = this.elementLocator(LocatorsAt.defaultAssignmentRemovalPopupConfirmationYesButton);

    // Exception Grid Find Button Count
    exceptionGridFindButtonCount = LocatorsAt.exceptionGridFindButtonCount.value

    // Default Trash Icon Count
    dafaultGridTrashIconCount = LocatorsAt.defaultGrid1stRowTrashIcon.value

    // Unassigned Markup count
    unAssignedMarkupIcon = this.elementLocator(LocatorsAt.unAssignedMarkupIcon);

    // Default Grid Add Another Button
    defaultGridAddAnotherButton = this.elementLocator(LocatorsAt.defaultGridAddAnotherButton);

    // First Exception Grid 4th Row
    firstExceptionGrid4thRow = this.elementLocator(LocatorsAt.firstExceptionGrid4thRow);

    // First Exception Grid 4th Row Delete Button
    firstExceptionGrid4thRowDeleteButton = this.elementLocator(LocatorsAt.firstExceptionGrid4thRowDeleteButton);

    // First Future Concept 2nd Item Error Message
    firstFutureConcept2ndItemErrorMessage = this.elementLocator(LocatorsAt.firstFutureConcept2ndItemErrorMessage);

    // Second Future Item Expand Collapse Arrow
    secondFutureItemExpandCollapseArrow = this.elementLocator(LocatorsAt.secondFutureItemExpandCollapseArrow);

    // Items Tab Modal Pop up Save Confimration
    itemsTabModalPopupSaveConfimration = this.elementLocator(LocatorsAt.itemsTabModalPopupSaveConfimration);

    // Second Future Concept Add Item Button
    secondFutureConceptAddItemButton = this.elementLocator(LocatorsAt.secondFutureConceptAddItemButton);

    // Second Future Concept 1st Item Id
    secondFutureConcept1stItemId = this.elementLocator(LocatorsAt.secondFutureConcept1stItemId);

    // Second Future Concepts 1st Find Button
    secondFutureConcepts1stFindButton = this.elementLocator(LocatorsAt.secondFutureConcepts1stFindButton);

    // Second Future Concept 2nd Item Id
    secondFutureConcept2ndItemId = this.elementLocator(LocatorsAt.secondFutureConcept2ndItemId);

    // Second Future Concepts 2nd Find Button
    secondFutureConcept2ndFindButton = this.elementLocator(LocatorsAt.secondFutureConcept2ndFindButton);

    // Second Future Concept Save Assignments Button
    secondFutureConceptSaveAssignmentsButton = this.elementLocator(LocatorsAt.secondFutureConceptSaveAssignmentsButton);

    // First Future Concept 2nd Item Invalid Error Message
    firstFutureConcept2ndItemInvalidErrorMessage = this.elementLocator(LocatorsAt.firstFutureConcept2ndItemInvalidErrorMessage);

    // Third Exception Expand Collapse Arrow
    thirdExceptionExpandCollapseArrow = this.elementLocator(LocatorsAt.thirdExceptionExpandCollapseArrow);

    // Third Exception 1st Row Customer Type
    thirdException1stRowCustomerType = this.elementLocator(LocatorsAt.thirdException1stRowCustomerType);

    // Third Exception 1st Row Customer Id Field
    thirdException1stRowCustomerIdField = this.elementLocator(LocatorsAt.thirdException1stRowCustomerIdField);

    // Third Exception 1st Row Find Button
    thirdException1stRowFindButton = this.elementLocator(LocatorsAt.thirdException1stRowFindButton);

    // Third Exception Save Button
    thirdExceptionSaveButton = this.elementLocator(LocatorsAt.thirdExceptionSaveButton);

    // Third Exception Modal Popup Save Button
    thirdExceptionModalPopupSaveButton = this.elementLocator(LocatorsAt.thirdExceptionModalPopupSaveButton);

    // Select Generic Toggle
    identifyGenericItemLevelRow(textReplaceValue: any) {
        return element(by.xpath(LocatorsAt.genericAssignmentsItemLevelRow.value.replace('Replace_Text_Value', textReplaceValue)))
    }
}
