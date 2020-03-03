import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, AssignmentsPageObject, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const EC = protractor.ExpectedConditions;

    When('Adds Multiple Valid Item Codes to Future Item Accordions', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await browser.wait(EC.presenceOf(assignments.secondFutureItemExpandCollapseArrow), CommonTasks.LONGWAIT);
        await assignments.secondFutureItemExpandCollapseArrow.click();
        await browser.wait(EC.presenceOf(assignments.secondFutureConcept1stItemId), CommonTasks.LONGWAIT);
        await assignments.secondFutureConcept1stItemId.sendKeys('166719');
        await assignments.secondFutureConcepts1stFindButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await assignments.secondFutureConceptAddItemButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await assignments.secondFutureConcept2ndItemId.sendKeys('166720');
        await assignments.secondFutureConcept2ndFindButton.click();
        return await CommonTasks.wait(CommonTasks.TINYWAIT);
    });

    Then('Saves the Assignments', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await assignments.secondFutureConceptSaveAssignmentsButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
    });

    Then('Confirmation Pop-up is displayed to user', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(assignments.itemsTabModalPopupSaveConfimration.isPresent()).to.eventually.be.true;
    });

});
