import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, AssignmentsPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const EC = protractor.ExpectedConditions;

    When('User clicks Save', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.elementToBeClickable(assignments.firstFutureConceptSaveAssignmentsButton), CommonTasks.MEDIUMWAIT);
        await assignments.firstFutureConceptSaveAssignmentsButton.click();
        return await assignments.firstFutureConceptSaveConfirmation.click();
    });

    When('User adds an item code that already exists and clicks Find', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.elementToBeClickable(assignments.defaultGridExpandCollapseArrowItemsTab), CommonTasks.MEDIUMWAIT);
        await assignments.defaultGridExpandCollapseArrowItemsTab.click();
        await browser.wait(EC.elementToBeClickable(assignments.firstFutureConceptAddAnotherButton), CommonTasks.MEDIUMWAIT);
        await assignments.firstFutureConceptAddAnotherButton.click();
        await browser.wait(EC.elementToBeClickable(assignments.firstFutureConcept2ndItemId), CommonTasks.MEDIUMWAIT);
        await assignments.firstFutureConcept2ndItemId.clear();
        await assignments.firstFutureConcept2ndItemId.sendKeys('166723');
        return await assignments.firstFutureConcept2ndFindButton.click();
    });

    Then('Duplicate error message is shown to the user for that Row', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(assignments.firstFutureConcept2ndItemErrorMessage.isPresent()).to.eventually.be.true;
    });

});
