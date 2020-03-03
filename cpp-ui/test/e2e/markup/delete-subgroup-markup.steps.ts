import { defineSupportCode } from 'cucumber';
import { browser, element, by } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();

    When('Clicks On Trash Icon of SubGroup Markup', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.subGroupTrashIcon.click();
    });

    Then('Correct Delete confirmation question is displayed for SubGroup Markup', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(markup.deleteConfirmationQuestionSubgroup.getText()).to.eventually.contain(
            'subgroup markup?')
    });
});
