import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, AssignmentsPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {
    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const EC = protractor.ExpectedConditions;

    When('User completes all Customer Assignments', { timeout: CommonTasks.LONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(assignments.thirdExceptionExpandCollapseArrow), CommonTasks.MEDIUMWAIT);
        await assignments.thirdExceptionExpandCollapseArrow.click();
        await browser.wait(EC.presenceOf(assignments.thirdException1stRowCustomerType), CommonTasks.MEDIUMWAIT);
        await CommonTasks.selectValueFromDropdown(assignments.thirdException1stRowCustomerType, '2: 22');
        await assignments.thirdException1stRowCustomerIdField.sendKeys('12345');
        await assignments.thirdException1stRowFindButton.click();
        await assignments.thirdExceptionSaveButton.click();
        return await assignments.thirdExceptionModalPopupSaveButton.click();
    });

    When('User clicks Activate Price Profile Button', { timeout: CommonTasks.LONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(assignments.activatePriceProfileButton), CommonTasks.MEDIUMWAIT);
        return await assignments.activatePriceProfileButton.click();
    });

    Then('Activate Price Profile Button is enabled', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(assignments.activatePriceProfileButton.isEnabled()).to.eventually.be.true;
    });

    Then('Success Message is displayed and Price Activation Button is disabled', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(assignments.priceActivationSuccessMessage.isPresent()).to.eventually.be.true &&
        expect(assignments.activatePriceProfileButton.isEnabled()).to.eventually.be.false;
    });

});
