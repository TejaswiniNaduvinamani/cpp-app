import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('Enters and Saves valid data for default Markup structure', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultGridFirstRowMarkupField), CommonTasks.LONGWAIT);
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('2');
        await markup.defaultGridFirstRowMarkupDollarIcon.click();
        await CommonTasks.selectValueFromDropdown(markup.defaultGridFirstRowMarkupType, '1');
        await markup.defaultGridCopyToAllButton.click();
        return await markup.defaultGridSaveButton.click();
    });

    When('Adds an exception Markup structure', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.addExceptionMarkupStructuresButton), CommonTasks.LONGWAIT);
        await markup.addExceptionMarkupStructuresButton.click();
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        await markup.popUpExceptionMarkupNameField.sendKeys('Exception Markup 1');
        await markup.popUpExceptionAddButton.click();
        return await CommonTasks.wait(CommonTasks.TINYWAIT);
    });

    When('Selects Default Grid from Copy Markup option', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.copyMarkupArrow.click();
        await markup.copyMarkupFirstDropdown.all(by.tagName('option')).get(1).click();
        return await CommonTasks.wait(CommonTasks.LONGWAIT);
    });

    Then('Markup data gets copied from Default Grid to current Markup structure', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        for (let i = 0; i <= 12; i++) {
            await element.all(by.xpath(markup.firstExceptionaAllInputMarkupFields)).then(function (item) {
                expect(item[i].getAttribute('value')).to.eventually.equal('2.00');
            });
        };
    });

    Then('Correct help text is displayed when user hovers over info icon', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.copyMarkupInfoIcon), CommonTasks.LONGWAIT);
        await browser.actions().mouseMove(markup.copyMarkupInfoIcon).perform();
        return await expect(markup.genericHoverOver.getText()).to.eventually.equal(
            'This allows you to copy markups entered on one of the saved markup structures within this contract');
    });

    Then('Copy Markup option is visible for the exception Markup structure', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(markup.copyMarkupFirstDropdown).to.exist;
    });
});
