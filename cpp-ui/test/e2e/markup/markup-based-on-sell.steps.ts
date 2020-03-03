import { defineSupportCode } from 'cucumber';
import { browser, element, by } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, DistributionCentersPageObject, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();

    When('User selects markup based on sell Toggle as No', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        return await markup.markupBasedOnSellToggle.click();
    });

    Then('Default value for Markup based on Sell toggle is Yes', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        await markup.defaultGridFirstRowMarkupPercentIcon.click();
        return await expect(browser.getCurrentUrl()).to.eventually.contain('markup');
    });

    Then('Warning message is displayed to the User', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(markup.markupToggleWarningMessage.isPresent()).to.eventually.be.true;
    });

});
