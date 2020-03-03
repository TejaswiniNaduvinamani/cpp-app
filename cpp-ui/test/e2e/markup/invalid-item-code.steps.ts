import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('User enters a valid item code', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultItemLevelFirstRowItemIdField), CommonTasks.LONGWAIT);
        await markup.defaultItemLevelFirstRowItemIdField.sendKeys('166720');
        return await markup.defaultItemLevelFirstRowItemIdField.sendKeys(protractor.Key.TAB);
    });

    When('User enters invalid Item code', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultItemLevelSecondRowItemIdField), CommonTasks.LONGWAIT);
        await markup.defaultItemLevelSecondRowItemIdField.sendKeys('1234');
        return await markup.defaultItemLevelFirstRowItemIdField.sendKeys(protractor.Key.TAB);
    });

    When('User enters duplicate item code', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultItemLevelSecondRowItemIdField), CommonTasks.LONGWAIT);
        await markup.defaultItemLevelSecondRowItemIdField.clear();
        await markup.defaultItemLevelSecondRowItemIdField.sendKeys('166720');
        return await markup.defaultItemLevelFirstRowItemIdField.sendKeys(protractor.Key.TAB);
    });

    When('User enters inactive item code', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultItemLevelSecondRowItemIdField), CommonTasks.LONGWAIT);
        await markup.defaultItemLevelSecondRowItemIdField.clear();
        await markup.defaultItemLevelSecondRowItemIdField.sendKeys('777790');
        return await markup.defaultItemLevelFirstRowItemIdField.sendKeys(protractor.Key.TAB);
    });

    Then('Correspoding Item Description is fetched and displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await expect(markup.defaultItemLevelFirstRowItemDescriptionField.getText()).to.eventually.equal('BLUEBERRY IQF 4-5# GFS');
        return await CommonTasks.wait(CommonTasks.TINYWAIT);
    });

    Then('Error message is displayed to enter a valid Item code', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(markup.invalidItemCodeErrorMessage.getText()).to.eventually.equal('Please enter a valid item code');
    });

    Then('Error message displayed to enter active item code', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(markup.inactiveItemCodeErrorMessage.getText()).to.eventually.equal('Please enter an active item code');
    });

    Then('Error message displayed to enter non duplicate item code', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(markup.duplicateItemCodeErrorMessage.getText()).to.eventually.equal('This item has already been added');
    });

});
