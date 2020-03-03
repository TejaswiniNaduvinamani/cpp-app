import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, AssignmentsPageObject, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('Provides and Saves valid item level Markup values', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultAddItemLevelMarkupButton), CommonTasks.LONGWAIT);
        await markup.defaultAddItemLevelMarkupButton.click();
        await browser.wait(EC.presenceOf(markup.defaultItemLevelFirstRowNoItemIdCheckbox), CommonTasks.LONGWAIT);
        await markup.defaultItemLevelFirstRowNoItemIdCheckbox.click();
        await markup.defaultItemLevelFirstRowItemDescription.sendKeys('Future Item Level 1');
        await markup.defaultItemLevelFirstRowItemDescription.sendKeys(protractor.Key.TAB);
        await browser.wait(EC.presenceOf(markup.defaultItemLevelFirstRowMarkupField), CommonTasks.MEDIUMWAIT);
        await markup.defaultItemLevelFirstRowMarkupField.clear();
        await markup.defaultItemLevelFirstRowMarkupField.sendKeys('99.99');
        await markup.defaultItemLevelFirstRowMarkupField.sendKeys(protractor.Key.TAB);
        await markup.defaultAddItemLevelMarkupButton.click();
        await browser.wait(EC.presenceOf(markup.defaultItemLevel2ndRowItemIdField), CommonTasks.LONGWAIT);
        await markup.defaultItemLevel2ndRowItemIdField.sendKeys('166720');
        await markup.defaultItemLevel2ndRowItemIdField.sendKeys(protractor.Key.TAB);
        await browser.wait(EC.presenceOf(markup.defaultItemLevel2ndRowMarkupField), CommonTasks.MEDIUMWAIT);
        await markup.defaultItemLevel2ndRowMarkupField.clear();
        await markup.defaultItemLevel2ndRowMarkupField.sendKeys('99.99');
        await markup.defaultItemLevel2ndRowMarkupField.sendKeys(protractor.Key.TAB);
        return await markup.defaultGridSaveButton.click();
    });

    Then('All correct item level markup data is displayed', async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(assignments.identifyGenericItemLevelRow('F1').isPresent()).to.eventually.be.true;
    });
});
