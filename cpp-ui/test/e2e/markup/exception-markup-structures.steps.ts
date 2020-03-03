import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('Adds an additional Exception Markup structure', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await markup.addExceptionMarkupStructuresButton.click();
    });

    When('Provides Exception Makrup Structure Name in the pop-up and clicks Add', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.popUpExceptionMarkupNameField.sendKeys('Exception Markup 1');
        return await markup.popUpExceptionAddButton.click();
    });

    When('User expands default markup grid and provides valid markup data', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.defaultGridExpandCollapseArrow.click();
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('2');
        await markup.defaultGridFirstRowMarkupDollarIcon.click();
        await CommonTasks.selectValueFromDropdown(markup.defaultGridFirstRowMarkupType, '1');
        await markup.defaultGridCopyToAllButton.click();
        return await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
    });

    When('Collapses the grid', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await markup.defaultGridExpandCollapseArrow.click();
    });

    When('User completely fills an exception grid and clicks Save', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.LONGWAIT);
        await browser.wait(EC.elementToBeClickable(markup.firstExceptionGridFirstRowMarkupField), CommonTasks.LONGWAIT);
        await markup.firstExceptionGridFirstRowMarkupField.clear();
        await markup.firstExceptionGridFirstRowMarkupField.sendKeys('2');
        await browser.wait(EC.elementToBeClickable(markup.firstExceptionGridFirstRowMarkupType), CommonTasks.MEDIUMWAIT);
        await CommonTasks.selectValueFromDropdown(markup.firstExceptionGridFirstRowMarkupType, '1');
        await markup.firstExceptionGridCopyToAllButton.click();
        return await markup.firstExceptionGridSaveButton.click();
    });

    Then('A new exception markup grid is added', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.firstExceptionMarkupLabel.isPresent()).to.eventually.be.true;
    });

    Then('User can expand or collapse both the default as well as the exception markup grid', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        // Click once to collapse Default Grid
        await markup.defaultGridExpandCollapseArrow.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        // Click to collapse exception markup grid
        return await markup.firstExceptionGridExpandCollapseArrow.click();
    });

    Then('Data entered by User remains intact when user expands the grid again', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        let length;
        await markup.defaultGridExpandCollapseArrow.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await element.all(by.xpath(markup.allDefaultInputMarkupFields)).count().then(function (size) {
            length = size;
        });
        for (let i = 0; i < length; i++) {
            await element.all(by.xpath(markup.allDefaultInputMarkupFields)).then(function (item) {
                expect(item[i].getAttribute('value')).to.eventually.equal('2.00');
            });
        }
    });

    Then('Markup button label changes to Markup Saved', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(markup.firstExceptionGridSaveButtonAfterLabelChange.isPresent()).to.eventually.be.true;
    });

});
