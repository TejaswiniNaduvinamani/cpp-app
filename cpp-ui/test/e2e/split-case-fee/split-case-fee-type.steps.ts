import { defineSupportCode } from 'cucumber';
import { browser, protractor, element, by } from 'protractor';

import { CommonTasks, MarkupPageObject, SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const splitCaseFee = new SplitCaseFeePageObject();
    const EC = protractor.ExpectedConditions;

    Then('User Should be able to select Fee Type as Price per Pack', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await CommonTasks.selectValueFromDropdown(splitCaseFee.feeTypeList, '2');
    });

    Then('User should be able to select Fee Type as Cost per Pack and Navigate to review stepper', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.selectValueFromDropdown(splitCaseFee.feeTypeList, '3');
        return splitCaseFee.nextButton.click();
    });

});
