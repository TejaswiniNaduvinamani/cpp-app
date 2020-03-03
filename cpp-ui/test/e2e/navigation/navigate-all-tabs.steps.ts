import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, HeaderPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const header = new HeaderPageObject();
    const EC = protractor.ExpectedConditions;

    When('User clicks Price Profile tab', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.contractName), CommonTasks.LONGWAIT);
        return await header.priceProfileTab.click();
    });

    When('User clicks Overview tab', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.overViewTab), CommonTasks.LONGWAIT);
        return await header.overViewTab.click();
    });

    When('User clicks Assignments tab', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.assignmentTab), CommonTasks.VERYLONGWAIT);
        return await header.assignmentTab.click();
    });

    Then('Price Profile, Overview, Assignments tabs are displayed on the top', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await expect(header.priceProfileTab.isPresent()).to.eventually.be.true;
        await expect(header.overViewTab.isPresent()).to.eventually.be.true;
        return await expect(header.assignmentTab.isPresent()).to.eventually.be.true;
    });

    Then('User sucessfully navigates to Price Profile tab', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(browser.getCurrentUrl()).to.eventually.contain('pricinginformation');
    });

    Then('User sucessfully navigates to Overview tab', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(browser.getCurrentUrl()).to.eventually.contain('overview');
    });

    Then('User sucessfully navigates to Assignments tab', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(browser.getCurrentUrl()).to.eventually.contain('assignments');
    });

    Then('Contract Information Object is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(header.contractInformationObject.isPresent()).to.eventually.be.true;
    });
});
