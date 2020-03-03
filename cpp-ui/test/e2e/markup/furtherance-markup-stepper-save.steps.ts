import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject , SplitCaseFeePageObject, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const markup = new MarkupPageObject();
    const EC = protractor.ExpectedConditions;

    When('User enters mandatory information and navigates to Markup Stepper', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.FurtheranceDateField), CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.FurtheranceDateField.sendKeys(CommonTasks.getCurrentSystemDate('/'));
        return await contractPricingInformation.nextButton.click();
    });

    Then('Next button label is correctly displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect (markup.furtheranceNextButton.getText()).to.eventually.contain('Continue to: Split Case Fee');
    });

    Then('User is navigated to Split Case Fee stepper on click of this button', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.furtheranceNextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(browser.getCurrentUrl()).to.eventually.contain('splitcasefee');
    });

});
