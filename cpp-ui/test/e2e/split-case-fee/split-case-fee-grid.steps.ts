import { defineSupportCode } from 'cucumber';
import { browser, protractor, element, by } from 'protractor';

import { CommonTasks, MarkupPageObject, SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const splitCaseFee = new SplitCaseFeePageObject();
    const EC = protractor.ExpectedConditions;

    When('User selects $ as split case fee for any row', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(splitCaseFee.firstRowDollarIcon), CommonTasks.LONGWAIT);
        return await splitCaseFee.firstRowDollarIcon.click();
    });

    When('Enter a value above 49', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(splitCaseFee.SplitCaseFeeFirstRow), CommonTasks.LONGWAIT);
        await splitCaseFee.SplitCaseFeeFirstRow.clear();
        return await splitCaseFee.SplitCaseFeeFirstRow.sendKeys('56');
    });

    When('User selects % as split case Fee for any row', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(splitCaseFee.firstRowPercentIcon), CommonTasks.LONGWAIT);
        return await splitCaseFee.firstRowPercentIcon.click();
    });

    When('Enters zero value', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(splitCaseFee.firstRowPercentIcon), CommonTasks.LONGWAIT);
        await splitCaseFee.SplitCaseFeeFirstRow.clear();
        return await splitCaseFee.SplitCaseFeeFirstRow.sendKeys('0');
    });

    When('Enter a valid value', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await splitCaseFee.SplitCaseFeeFirstRow.clear();
        await splitCaseFee.SplitCaseFeeFirstRow.sendKeys('45');
        return await splitCaseFee.copyToAllButton.click();
    });

    When('Provides and Saves valid Markup values', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('2');
        await markup.defaultGridFirstRowMarkupDollarIcon.click();
        await CommonTasks.selectValueFromDropdown(markup.defaultGridFirstRowMarkupType, '1');
        await markup.defaultGridCopyToAllButton.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        return await markup.defaultGridSaveButton.click();
    });

    When('Clicks Next to reach Split Case Fee stepper', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await markup.nextButton.click();
    });

    When('Clicks on Copy to All button', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await splitCaseFee.copyToAllButton.click();
    });

    Then('User Should able to select fee by % on any row', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.firstRowPercentIcon.click();
    });

    Then('Default value should be 35%', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await element.all(by.xpath(splitCaseFee.allSplitCaseFeeInputFields)).each(function (item) {
            expect(item.getAttribute('value')).to.eventually.equal('35.00');
        });
    });

    Then('User sees warning message for exceeding Split Case Fee above 5 dollars', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(splitCaseFee.recommendedWarning.isPresent()).to.be.eventually.true;
    });

    Then('User sees a warning message that fee should ideally be below 49%', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await splitCaseFee.SplitCaseFeeFirstRow.sendKeys(protractor.Key.TAB);
        return await expect(splitCaseFee.recommendedWarning.isPresent()).to.be.eventually.true;
    });    

    Then('Extra level approval warning message is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await splitCaseFee.SplitCaseFeeFirstRow.sendKeys(protractor.Key.TAB);
        return await expect(splitCaseFee.approvalWarning.isPresent()).to.be.eventually.true;
    });

    Then('Effective and expiration dates are same as pricing effective and expiration dates from first stepper', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await splitCaseFee.pricingEffectiveDate.getText().then(function (value) {
            expect(splitCaseFee.firstRowEffectiveDate.getText()).to.eventually.equal(value);
        });

        return await splitCaseFee.pricingExpirationDate.getText().then(function (value) {
            expect(splitCaseFee.firstRowExpirationDate.getText()).to.eventually.equal(value);
        });
    });

    Then('The value and fee type should be copied to all rows below', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await element.all(by.xpath(splitCaseFee.allSplitCaseFeeInputFields)).each(function (item) {
            expect(item.getAttribute('value')).to.eventually.equal('45.00');
        });
    });
});
