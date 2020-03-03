import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, AssignmentsPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const EC = protractor.ExpectedConditions;

    When('User assigns Real Customer to Exception Grid', { timeout: CommonTasks.LONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(assignments.thirdExceptionExpandCollapseArrow), CommonTasks.MEDIUMWAIT);
        await assignments.thirdExceptionExpandCollapseArrow.click();
        await browser.wait(EC.presenceOf(assignments.thirdException1stRowCustomerType), CommonTasks.MEDIUMWAIT);
        await CommonTasks.selectValueFromDropdown(assignments.thirdException1stRowCustomerType, '2: 22');
        await assignments.thirdException1stRowCustomerIdField.sendKeys('12345');
        await assignments.thirdException1stRowFindButton.click();
    });

    Then('Save Button is disabled for the Default Grid', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(assignments.defaultGridSaveAssignmentsButton.getAttribute('disabled')).to.eventually.equal('true');
    });

    Then('Save Button is enabled for the Exception Grid', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(assignments.thirdExceptionSaveButton.getAttribute('disabled')).to.eventually.equal(null);
    });

});
