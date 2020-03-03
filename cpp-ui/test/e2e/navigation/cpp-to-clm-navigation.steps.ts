import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, HeaderPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const header = new HeaderPageObject();

    When('User clicks on return to Contract Button', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await header.returntoContractButton.click();
    });

    Then('User succesfully navigates to the CLM application', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        browser.getAllWindowHandles().then(async function (handles) {
            await browser.switchTo().window(handles[1]);
            await browser.sleep(2000);
            return await expect(browser.getCurrentUrl()).to.eventually.contain('c83f60ed-1aa6-4a48-bcb0-171e614841f7');
        });

    });

});
