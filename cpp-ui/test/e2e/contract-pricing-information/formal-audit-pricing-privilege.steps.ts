import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ Given, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();

    Given('User is on the Contract Information Pricing Stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await contractPricingInformation.applogo.click();
        return await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
    });

    Given('User launches CPP Application', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.get('#/pricinginformation?agreementId=c0e3a4f5-7479-4d22-b349-e61c50b3e26c' +
            '&contractType=ICMDistributionAgreementRegional', CommonTasks.VERYLONGWAIT * 2);
        return await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
    });

    Given('User launches CPP Application for a Furtherance', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.get('#/furtheranceinformation?agreementId=c0e3a4f5-7479-4d22-b349-e61c50b3e26c' +
            '&contractType=ICMDistributionAgreementRegional', CommonTasks.VERYLONGWAIT * 2);
        return await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
    });

    Then('Formal Price Audit Privileges toggle is displayed correctly on CPP Customer Information Screen',
            { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await contractPricingInformation.showTogglesQuestionYes.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(contractPricingInformation.formalPriceAuditPrivilegesToggle.isPresent()).to.eventually.be.true;
    });

    Then('User able to make a selection on the toggle', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await contractPricingInformation.formalPriceAuditPrivilegesToggle.click();
    });

});
