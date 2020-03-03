import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject } from '../shared';

defineSupportCode(({ Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const markup = new MarkupPageObject();

    Then('User is able to select No Item Id checkbox', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await markup.defaultItemLevelFirstRowNoItemIdCheckbox.click();
    });

    Then('User is allowed to type free text as item description', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await markup.defaultItemLevelFirstRowItemDescription.sendKeys('String 1234');
    });

});
