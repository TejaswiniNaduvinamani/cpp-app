import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, DistributionCentersPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const distributionCenters = new DistributionCentersPageObject();
    const common = new CommonTasks();
    const EC = protractor.ExpectedConditions;

    When('Clicks Next Again', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.VERYLONGWAIT);
        return await common.genericNextButton.click();
    });

    Then('Distribution Centers Screen is Displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(browser.getCurrentUrl()).to.eventually.contain('distributioncenters');
    });

    Then('User can make any number of selections on the Distribution Centers', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await distributionCenters.clickAllDistributionCenters();
    });

    Then('Users can deselect all distribution centers using Clear All button', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await distributionCenters.clear.click();
    });

    Then('Users can select all distribution centers using Select All button', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await distributionCenters.selectAll.click();
    });
});
