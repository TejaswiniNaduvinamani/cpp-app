import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, DistributionCentersPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const distributionCenters = new DistributionCentersPageObject();
    const EC = protractor.ExpectedConditions;

    Then('Markup screen is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(browser.getCurrentUrl()).to.eventually.contain('markup');
    });

    Then('Message saying at least one distribution center must be selected is displayed', {
        timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(distributionCenters.errorMessage.getText()).to.eventually.contain(
            'At least one distribution center must be selected for a customer on the contract.');
    });
});
