import { defineSupportCode } from 'cucumber';
import { browser, element, by } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();

    When('Delete the exception Markup structure name', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.markupEditLink.click();
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.deleteButtonEditScreen.click();
        return await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
    });

    Then('Correct confirmation question is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.deleteConfirmationQuestion.getText()).to.eventually.contain(
            'Are you sure you would like to delete this') && expect(
            markup.deleteConfirmationQuestion.getText()).to.eventually.contain('Exception Markup Structure?');
    });

    Then('Deleted exception markup stucture is no longer displayed on screen', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.deleteButtonConfirmationQuestionYes.click();
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.firstExceptionMarkupLabel.isPresent()).to.eventually.be.false;
    });

});
