import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, AssignmentsPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const EC = protractor.ExpectedConditions;

    When('User enters invalid Item Codes and clicks Find', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await assignments.firstFutureConcept2ndItemId.clear();
        await assignments.firstFutureConcept2ndItemId.sendKeys('166721');
        return await assignments.firstFutureConcept2ndFindButton.click();
    });

    Then('Add Another button enables rows to add Item Codes for Future Markup Accordions', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(assignments.defaultGridExpandCollapseArrowItemsTab), CommonTasks.LONGWAIT);
        await assignments.defaultGridExpandCollapseArrowItemsTab.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await assignments.firstFutureConceptAddAnotherButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(assignments.firstFutureConcept2ndRow.isPresent()).to.eventually.be.true;
    });

    Then('User is able to add Multiple Valid Item Level Codes to Future Item Accordions', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await assignments.firstFutureConcept2ndItemId.sendKeys('166720');
        await assignments.firstFutureConcept2ndFindButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
    });

    Then('Save Assignments Button is enabled in Items tab', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(assignments.firstFutureConceptSaveAssignmentsButton.isEnabled()).to.eventually.be.true;
    });

    Then('Invalid Item Code Error message is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(assignments.firstFutureConcept2ndItemInvalidErrorMessage.isPresent()).to.eventually.be.true;
    });

    Then('Save Assignments Button is disabled', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(assignments.firstFutureConceptSaveAssignmentsButton.isEnabled()).to.eventually.be.false;
    });

});
