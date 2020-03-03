import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, AssignmentsPageObject, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('User Adds and Saves a Future Item Level Markup for the default grid', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultAddItemLevelMarkupButton), CommonTasks.LONGWAIT);
        await markup.defaultAddItemLevelMarkupButton.click();
        await markup.defaultItemLevelFirstRowNoItemIdCheckbox.click();
        await markup.defaultItemLevelFirstRowItemDescription.sendKeys('Future Item Level 1');
        await markup.defaultItemLevelFirstRowItemDescription.sendKeys(protractor.Key.TAB);
        await browser.wait(EC.presenceOf(markup.defaultItemLevelFirstRowMarkupField), CommonTasks.MEDIUMWAIT);
        await markup.defaultItemLevelFirstRowMarkupField.clear();
        await markup.defaultItemLevelFirstRowMarkupField.sendKeys('99.99');
        await markup.defaultItemLevelFirstRowMarkupField.sendKeys(protractor.Key.TAB);
        return await markup.defaultGridSaveButton.click();
    });

    When('User clicks Items tab', async () => {
        await assignments.itemsTab.click();
    });

    Then('Future item descriptions are displayed as accordions', async () => {
        await assignments.defaultGridExpandCollapseArrowItemsTab.click();
        return await assignments.secondFutureItemExpandCollapseArrow.click();
    });

});
