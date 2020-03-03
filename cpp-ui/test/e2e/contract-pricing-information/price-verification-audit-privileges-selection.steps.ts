import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const EC = protractor.ExpectedConditions;

    When('Answers Yes to the question to display Price Verification & Audit Privileges Toggles',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.showTogglesQuestionYes), CommonTasks.LONGWAIT);
        return await contractPricingInformation.showTogglesQuestionYes.click();
    });

    When('Selects No to Price verification Audit Privileges Question', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
       await browser.wait(EC.presenceOf(contractPricingInformation.showTogglesQuestionNo), CommonTasks.LONGWAIT);
       return await contractPricingInformation.showTogglesQuestionNo.click();
    });

    Then('Question whether to show price verification and formal price audit privileges toggle or not is displayed',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.showTogglesQuestion.isPresent()).to.eventually.be.true;
    });
    
    Then('No toggle should be displayed', async () => {
        return expect (contractPricingInformation.priceVerificationQuestionToggle.isPresent()).to.eventually.be.false && 
        expect (contractPricingInformation.formalPriceAuditPrivilegesToggle.isPresent()).to.eventually.be.false
    });

    Then('Both Price Verification and Audit Privileges Toggles is displayed', async () => {
        return expect (contractPricingInformation.priceVerificationQuestionToggle.isPresent()).to.eventually.be.true && 
        expect (contractPricingInformation.formalPriceAuditPrivilegesToggle.isPresent()).to.eventually.be.true
    });

});
