import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('Clicks on Info icon against Markup Type Column in the Default Grid', {timeout: CommonTasks.LONGWAIT}, async () => {
        await browser.wait(EC.presenceOf(markup.defaultGridMarkupTypeHelperIcon), CommonTasks.MEDIUMWAIT);
        return await markup.defaultGridMarkupTypeHelperIcon.click();
    });

    When('User closes the pop-up and clicks on Info icon against Markup Type Column in the Item Level Grid', {
        timeout: CommonTasks.EXTENSIVEWAIT}, async () => {
        await browser.wait(EC.presenceOf(markup.markupTypeModalPopupCloseButton), CommonTasks.MEDIUMWAIT);
        await markup.markupTypeModalPopupCloseButton.click();
        await markup.defaultAddItemLevelMarkupButton.click();
        await browser.wait(EC.presenceOf(markup.defaultItemLevelMarkupTypeHelperIcon), CommonTasks.MEDIUMWAIT);
        return await markup.defaultItemLevelMarkupTypeHelperIcon.click();
    });

    Then('Pop-up is correctly displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(markup.markupTypeModalPopup.isPresent()).to.eventually.be.true;
    });

});
