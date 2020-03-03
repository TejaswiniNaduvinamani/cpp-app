import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, DistributionCentersPageObject, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const distributionCenters = new DistributionCentersPageObject();
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('Selects first Distribution Center', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(distributionCenters.getDistributionCenter('1')), CommonTasks.LONGWAIT);
        return await distributionCenters.clickFirstDistributionCenter();
    });

    When('User Adds a Future Item Level Markup for the default grid', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultAddItemLevelMarkupButton), CommonTasks.LONGWAIT);
        await markup.defaultAddItemLevelMarkupButton.click();
        await markup.defaultItemLevelFirstRowNoItemIdCheckbox.click();
        await markup.defaultItemLevelFirstRowItemDescription.sendKeys('Future Item Level 1');
        await markup.defaultItemLevelFirstRowItemDescription.sendKeys(protractor.Key.TAB);
        await browser.wait(EC.presenceOf(markup.defaultItemLevelFirstRowMarkupField), CommonTasks.MEDIUMWAIT);
        await markup.defaultItemLevelFirstRowMarkupField.clear();
        await markup.defaultItemLevelFirstRowMarkupField.sendKeys('99.99');
        return await markup.defaultItemLevelFirstRowMarkupField.sendKeys(protractor.Key.TAB);
    });

    When('Adds another Future Item Level Markup with exact same description', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultAddItemLevelMarkupButton), CommonTasks.LONGWAIT);
        await markup.defaultAddItemLevelMarkupButton.click();
        await markup.defaultItemLevel2ndRowNoItemIdCheckbox.click();
        await markup.defaultItemLevel2ndRowItemDescription.sendKeys('Future Item Level 1');
        await markup.defaultItemLevel2ndRowMarkupField.clear();
        await markup.defaultItemLevel2ndRowMarkupField.sendKeys('0.01');
        return await markup.defaultItemLevel2ndRowMarkupField.sendKeys(protractor.Key.TAB);
    });

    When('User deletes last added future Item Markup and Saves', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultItemLevel2ndRowTrashIcon), CommonTasks.MEDIUMWAIT);
        await markup.defaultItemLevel2ndRowTrashIcon.click();
        await markup.itemLevelDeleteButton.click();
        return await markup.defaultGridSaveButton.click();
    });

    Then('Error Message that Item has been already added is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(markup.invalidItemCodeErrorMessage.getText()).to.eventually.equal('This item has already been added');
    });

    Then('Markup value and markup type can still be updated and saved for the first row', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.defaultItemLevelFirstRowMarkupField.clear();
        await markup.defaultItemLevelFirstRowMarkupField.sendKeys('10.01');
        await markup.defaultItemLevelFirstRowMarkupDollarIcon.click();
        return await markup.defaultGridSaveButton.click();
    });

});
