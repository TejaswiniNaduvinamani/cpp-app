import { defineSupportCode } from 'cucumber';
import { browser, element, by } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();

    When('Edits the exception Markup structure name', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.markupEditLink.click();
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.popUpExceptionEditMarkupNameField.sendKeys('Edit');
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await markup.saveButtonEditException.click();
    });

    When('User enters a new markup with same name', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.addExceptionMarkupStructuresButton.click();
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.popUpExceptionMarkupNameField.sendKeys('Edit');
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.clickingOutsideOfInputField.click();
        return await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
    });

    Then('Exception markup name is renamed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.firstExceptionMarupName.getText()).to.eventually.equal('Edit');
    });

    Then('Correct error messsage is visible on the Page', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await expect(markup.duplicateNameErrorMessage.getText()).to.eventually.equal(
            'The markup structure name you are trying to enter is already in use. Please enter a new name');
        return await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
    });
});
