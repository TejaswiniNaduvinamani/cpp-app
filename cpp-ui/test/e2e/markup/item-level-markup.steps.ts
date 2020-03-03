import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    let markupValue;
    const EC = protractor.ExpectedConditions;

    When('User clicks Add item level Markup button', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultAddItemLevelMarkupButton), CommonTasks.LONGWAIT);
        return await markup.defaultAddItemLevelMarkupButton.click();
    });

    When('User selects item level Markup by $ for a given row', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultItemLevelFirstRowMarkupDollarIcon), CommonTasks.LONGWAIT);
        return await markup.defaultItemLevelFirstRowMarkupDollarIcon.click();
    });

    When('User selects item level Markup by % for a given row', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultItemLevelFirstRowMarkupPercentIcon), CommonTasks.LONGWAIT);
        return await markup.defaultItemLevelFirstRowMarkupPercentIcon.click();
    });

    When('User enters a {string} and tabs out of an item level Markup field', {
            timeout: CommonTasks.VERYLONGWAIT }, async (number: string) => {
        markupValue = parseFloat(number);
        await browser.wait(EC.presenceOf(markup.defaultItemLevelFirstRowMarkupField), CommonTasks.LONGWAIT);
        await markup.defaultItemLevelFirstRowMarkupField.clear();
        await markup.defaultItemLevelFirstRowMarkupField.sendKeys(number);
        return await markup.defaultItemLevelFirstRowMarkupField.sendKeys(protractor.Key.TAB);
    });

    Then('Correct Item Level Markup description is displayed on Markup Stepper', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(markup.defaultItemLevelDescription.getText()).to.eventually.equal(
            'Contract Item level Markups Will Override Subgroup and Product Type Markups');
    });

    Then('Item level markup grid is added', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(markup.defaultItemLevelGrid.isPresent()).to.eventually.be.true;
    });

    Then('A new row is added to item level Markup grid', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(markup.defaultItemLevelGrid2ndRow.isPresent()).to.eventually.be.true;
    });

    Then('Item level Markup entered is rounded off to two decimal places', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.defaultItemLevelFirstRowMarkupField.getAttribute('value')).to.eventually.equal(markupValue.toFixed(2));
    });

    Then('User can select item level Markup Type as per Sell Unit', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await CommonTasks.selectValueFromDropdown(markup.defaultItemLevelFirstRowMarkupType, '1');
    });

    Then('Item level Markup Type as per Case', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await CommonTasks.selectValueFromDropdown(markup.defaultItemLevelFirstRowMarkupType, '2');
    });

    Then('Item level Markup Type as per Weight', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await CommonTasks.selectValueFromDropdown(markup.defaultItemLevelFirstRowMarkupType, '3');
    });

    Then('Item level Markup Type is disabled for selection', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        return await expect(markup.defaultItemLevelFirstRowMarkupType.getAttribute('disabled')).to.eventually.equal('true');
    });

    Then('Effective and Expiration dates for item level markups are same as Pricing Effective and Expiration dates from 1st Stepper', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.pricingEffectiveDate.getText().then(function (value) {
            expect(markup.defaultItemLevelFirstRowEffectiveDate.getText()).to.eventually.equal(value);
        });

        return await markup.pricingExpirationDate.getText().then(function (value) {
            expect(markup.defaultItemLevelFirstRowExpirationDate.getText()).to.eventually.equal(value);
        });
    });

});
