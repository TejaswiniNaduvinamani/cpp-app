import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const common = new CommonTasks();
    const EC = protractor.ExpectedConditions;

    When('User enters Pricing Effective Date greater than Contract Start Date', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.pricingEffectivetDate), CommonTasks.LONGWAIT);
        await contractPricingInformation.pricingEffectivetDate.click();
        await contractPricingInformation.pricingEffectivetDate.clear();
        return await contractPricingInformation.pricingEffectivetDate.sendKeys('07/25/2018');
    });

    When('User enters Pricing Effective Date less than Current Date', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.pricingEffectivetDate), CommonTasks.LONGWAIT);
        await contractPricingInformation.pricingEffectivetDate.click();
        await contractPricingInformation.pricingEffectivetDate.clear();
        return await contractPricingInformation.pricingEffectivetDate.sendKeys('06/06/2017');
    });

    When('User enters an invalid date format', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.pricingEffectivetDate), CommonTasks.LONGWAIT);
        await contractPricingInformation.pricingEffectivetDate.click();
        await contractPricingInformation.pricingEffectivetDate.clear();
        return await contractPricingInformation.pricingEffectivetDate.sendKeys('abcdkd');
    });

    When('User clicks Next', { timeout: CommonTasks.EXTENSIVEWAIT * 2 }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.nextButton), CommonTasks.EXTENSIVEWAIT);
        await contractPricingInformation.nextButton.click();
        return await common.genericNextButton.click();
    });

    When('Clicks Next', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.nextButton), CommonTasks.MEDIUMWAIT);
        return await contractPricingInformation.nextButton.click();
    });

    Then('Pricing Effective & Exipration Dates are displayed correctly on CPP Information Screen',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.pricingEffectivetDate.isPresent()).to.eventually.be.true &&
        expect(contractPricingInformation.pricingExpirationDate.isPresent()).to.eventually.be.true;
    });

    Then('They are equal to Contract Start Date and Far Out Date respectively', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.pricingEffectivetDate.getAttribute('value')).to.eventually.contain('07/24/2018') &&
        expect(contractPricingInformation.pricingExpirationDate.getText()).to.eventually.contain('01/01/9999');
    });

    Then('User sees error message: Pricing must be effective before or on the same day as Contract Start Date',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.pricingEffectivetDateErrorMessage.getText())
            .to.eventually.equal('Pricing must be effective before or on the same day as Contract Start Date');
    });

    Then('User sees error message: Pricing cannot be made effective for dates earlier than today.',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.pricingEffectivetDateErrorMessage.getText())
            .to.eventually.equal('Pricing cannot be made effective for dates earlier than today');
    });

    Then('User sees error message to enter correct date format', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.pricingEffectivetDateErrorMessage.getText())
            .to.eventually.equal('Please enter a valid date in the format \'mm/dd/yyyy\'');
    });
});
