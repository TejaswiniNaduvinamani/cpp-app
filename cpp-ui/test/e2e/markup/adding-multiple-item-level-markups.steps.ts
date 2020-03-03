import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('User clicks Add item level Markup button Twenty Five times', {timeout: CommonTasks.EXTENSIVEWAIT}, async () => {
        let i = 1;
        await browser.wait(EC.presenceOf(markup.defaultAddItemLevelMarkupButton), CommonTasks.LONGWAIT);
        while (i <= 25) {
            await CommonTasks.wait(CommonTasks.SMALLWAIT);
            await markup.defaultAddItemLevelMarkupButton.click();
            i++;
        };
    });

    When('Enters mandatory information and navigates to Split Case Fee Stepper', {timeout: CommonTasks.VERYLONGWAIT}, async () => {
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await markup.expireLowerQuestionYes.click();
        browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.LONGWAIT);
        return await markup.nextButton.click();
    });

    Then('Twenty Five rows are added to item level markup grid one at a time', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(element.all(by.xpath(markup.allDefaultItemLevelRows)).count()).to.eventually.equal(25);
    });

    Then('Add Item Level Markup button is disabled', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.defaultAddItemLevelMarkupButton.getAttribute('disabled')).to.eventually.equal('true');
    });

    Then('Message is displayed informing user that only 25 item level markups can be added',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.defaultMarkupMaximumItemLevelMessage.isPresent()).to.eventually.be.true;
    });

    Then('Default value in Markup column is blank', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        let length = 0;
        await element.all(by.xpath(markup.allDefaultItemLevelInputFields)).count().then(function (size) {
            length = size;
        });
        for (let i = 0; i < length; i++) {
            await element.all(by.xpath(markup.allDefaultItemLevelInputFields)).then(function (item) {
                expect(item[i].getAttribute('value')).to.eventually.equal('');
            });
        }
    });
});
