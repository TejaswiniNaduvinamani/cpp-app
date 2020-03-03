import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    let markupValue;

    When('User selects Markup by $ for a given row', async () => {
        return await markup.defaultGridFirstRowMarkupDollarIcon.click();
    });

    When('User selects Markup by % for a given row', async () => {
        return await markup.defaultGridFirstRowMarkupPercentIcon.click();
    });

    When('User provides a given Markup value, Unit and Type for the first row', async () => {
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('2');
        await markup.defaultGridFirstRowMarkupDollarIcon.click();
        return await CommonTasks.selectValueFromDropdown(markup.defaultGridFirstRowMarkupType, '1');
    });

    When('User enters a {string} and tabs out of a Markup field', async (number: string) => {
        markupValue = parseFloat(number);
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys(number);
        return await markup.defaultGridFirstRowMarkupField.sendKeys(protractor.Key.TAB);
    });

    Then('Markup entered is rounded off to two decimal places', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        const value = markup.defaultGridFirstRowMarkupField.getAttribute('value');
        return await expect(value).to.eventually.equal(markupValue.toFixed(2));
    });

    When('User enters markup value less than 5%', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('4');
        return await markup.defaultGridFirstRowMarkupPercentIcon.click();
    });

    When('User enters markup value greater than 45%', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('45.01');
        return await markup.defaultGridFirstRowMarkupPercentIcon.click();
    });

    When('User enters markup value less than 0.05$', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('0.04');
        return await markup.defaultGridFirstRowMarkupDollarIcon.click();
    });

    When('User enters markup value greater than 5$', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('99');
        return await markup.defaultGridFirstRowMarkupDollarIcon.click();
    });

    Then('User can select either Markup by % or Markup by $', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        await markup.defaultGridFirstRowMarkupPercentIcon.click();
        return await markup.defaultGridFirstRowMarkupDollarIcon.click();
    });

    Then('User can select Markup Type as per Sell Unit', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await CommonTasks.selectValueFromDropdown(markup.defaultGridFirstRowMarkupType, '1');
    });

    Then('Markup Type as per Case', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await CommonTasks.selectValueFromDropdown(markup.defaultGridFirstRowMarkupType, '2');
    });

    Then('Markup Type is disabled for selection', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        return await expect(markup.defaultGridFirstRowMarkupType.getAttribute('disabled')).to.eventually.equal('true');
    });

    Then('User can select Yes for Expire Lower Question', { timeout: CommonTasks.LONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        return await markup.expireLowerQuestionYes.click();
    });

    Then('User can select No for Expire Lower Question', { timeout: CommonTasks.LONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        return await markup.expireLowerQuestionNo.click();
    });

    Then('Effective and Expiration dates for all markups are same as Pricing Effective and Expiration dates from 1st Stepper', {
        timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.pricingEffectiveDate.getText().then(function (value) {
            expect(markup.defaultGridFirstRowEffectiveDate.getText()).to.eventually.equal(value);
        });

        return await markup.pricingExpirationDate.getText().then(function (value) {
            expect(markup.defaultGridFirstRowExpirationDate.getText()).to.eventually.equal(value);
        });
    });

    Then('Save markup button is active only after all product type rows have been entered', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        return await expect(markup.defaultGridSaveButton.getAttribute('disabled')).to.not.be.null;
    });

    Then('All values are copied down to other rows when user clicks Copy To All', { timeout: CommonTasks.EXTENSIVEWAIT },  async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.defaultGridCopyToAllButton.click();
        return await element.all(by.xpath(markup.allDefaultInputMarkupFields)).each(function (item) {
            // tslint:disable-next-line:no-unused-expression
            expect(item.getAttribute('value')).to.eventually.not.be.null;
        });
    });

    Then('Warning Message is displayed for that row', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.defaultGridFirstRowErrorMessage.isPresent()).to.be.eventually.true;
    });
});
