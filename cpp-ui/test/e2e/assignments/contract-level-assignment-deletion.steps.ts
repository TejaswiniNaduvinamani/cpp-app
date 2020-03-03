import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, AssignmentsPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {
    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const EC = protractor.ExpectedConditions;

    When('Deletes default grid Assignment', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(assignments.defaultGrid1stRowTrashIcon), CommonTasks.LONGWAIT);
        return await assignments.defaultGrid1stRowTrashIcon.click();
    });

    Then('User is shown a confirmation pop-up that all Assignments from Contract will be removed',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
            await CommonTasks.wait(CommonTasks.TINYWAIT);
            await expect(assignments.defaultAssignmentRemovalPopupConfirmation.isPresent()).to.eventually.be.true;
        });
});
