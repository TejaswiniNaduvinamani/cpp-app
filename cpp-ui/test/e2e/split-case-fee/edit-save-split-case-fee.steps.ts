import { defineSupportCode } from 'cucumber';
import { browser, protractor, element, by } from 'protractor';

import { CommonTasks, SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const splitCaseFee = new SplitCaseFeePageObject();
    const arrayListBefore = [], arrayListAfter = [];

    When('Clicks Next to reach Review stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await element.all(by.xpath(splitCaseFee.allSplitCaseFeeInputFields)).each(function (item) {
            arrayListBefore.push(item.getText());
        });
        return await splitCaseFee.nextButton.click();
    });

    When('User goes back to the Split case fee stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await browser.navigate().back();
    });

    Then('All split case fee data are correctly displayed', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        CommonTasks.wait(CommonTasks.TINYWAIT);
        await element.all(by.xpath(splitCaseFee.allSplitCaseFeeInputFields)).each(function (item) {
            arrayListAfter.push(item.getText());
        });
        return await expect(CommonTasks.compareArray(arrayListBefore, arrayListAfter).valueOf()).to.be.true;
    });

});
