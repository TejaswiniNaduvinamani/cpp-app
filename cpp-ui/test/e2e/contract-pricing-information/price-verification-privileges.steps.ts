import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const EC = protractor.ExpectedConditions;

    When('Answers yes to the question to have both price verification and formal price audit privileges option',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.priceVerificationQuestionYesButton), CommonTasks.LONGWAIT);
        return await contractPricingInformation.priceVerificationQuestionYesButton.click();
    });

    When('User hovers on the info Icon', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(
            contractPricingInformation.identifyGenericInfoIcon('Price Verification Privileges')), CommonTasks.LONGWAIT);
        return await browser.actions().mouseMove(
            contractPricingInformation.identifyGenericInfoIcon('Price Verification Privileges')).perform();
    });

    Then('Formal Price Verification Privileges toggle is visible on CPP Customer Information Screen',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(contractPricingInformation.priceVerificationQuestionToggle.isPresent()).to.eventually.be.true;
    });

    Then('User able to make a selection on the Pricing Verification Privileges toggle', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await contractPricingInformation.priceVerificationQuestionToggle.click();
    });

    Then('User will able to see a detailed explanation', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(contractPricingInformation.genericHoverOver.getText()).to.eventually.equal
            ('This selection will allow for the customer to receive a report to validate pricing without manufacturer invoices.')
    });

});
