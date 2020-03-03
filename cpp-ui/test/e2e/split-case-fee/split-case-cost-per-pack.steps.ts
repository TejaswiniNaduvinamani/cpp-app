import { defineSupportCode } from 'cucumber';
import { browser } from 'protractor';

import { CommonTasks, SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const splitCaseFee = new SplitCaseFeePageObject();
    const expect = chai.expect;

    Then('User can select Split Case Fee based on cost per pack or price per pack', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await CommonTasks.selectValueFromDropdown(splitCaseFee.feeTypeList, '2');
        return await CommonTasks.selectValueFromDropdown(splitCaseFee.feeTypeList, '3');
    });

    Then('Correct Helper Text is displayed for Fee Type', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.actions().mouseMove(splitCaseFee.identifyGenericInfoIcon('Fee Type')).perform();
        return await expect(splitCaseFee.genericHoverOver.getText()).to.eventually.contain
        ('Price Divided by Pack')
    });

});
