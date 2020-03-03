import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, ReviewPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const review = new ReviewPageObject();
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const EC = protractor.ExpectedConditions;
    
    When('User clicks on Commit Price Profile button', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(review.commitPriceProfileButton), CommonTasks.MEDIUMWAIT);
        await review.commitPriceProfileButton.click();
    });

    When('User clicks on save and continue to distribution centers button', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.nextButton), CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.nextButton.click();
    });

    Then('Clear pricing exhibit pop up is displayed to the User', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(contractPricingInformation.clearExhibitPopUp.isPresent()).to.eventually.be.true;
    });

});
