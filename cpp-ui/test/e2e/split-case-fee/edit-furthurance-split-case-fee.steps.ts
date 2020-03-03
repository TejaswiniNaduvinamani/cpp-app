import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject , SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const splitCaseFee = new SplitCaseFeePageObject();
    const EC = protractor.ExpectedConditions;

    When('User clicks on split case fee stepper', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await contractPricingInformation.continueButtonfurtherancePopup.click();
        await browser.wait(EC.presenceOf(contractPricingInformation.stepper4), CommonTasks.LONGWAIT);       
        return await contractPricingInformation.stepper4.click();
    });

    Then('User is able to edit split case fee values', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await splitCaseFee.SplitCaseFeeFirstRow.clear();
        return await splitCaseFee.SplitCaseFeeFirstRow.sendKeys('45');
    });

    Then('User is not able to switch between % or $', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await expect (splitCaseFee.viewModefirstRowDollarIcon.isPresent()).to.eventually.be.true;
        return await expect (splitCaseFee.viewModefirstRowPercentIcon.isPresent()).to.eventually.be.true;
    });

});
