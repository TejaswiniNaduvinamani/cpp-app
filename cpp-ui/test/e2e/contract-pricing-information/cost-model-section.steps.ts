import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const EC = protractor.ExpectedConditions;

    When('{string} is selected as Yes on the Customer Information Screen',
    { timeout: CommonTasks.VERYLONGWAIT }, async (toggle: string) => {
        await browser.wait(EC.presenceOf(contractPricingInformation.selectGenericToggle(toggle)), CommonTasks.LONGWAIT);
        return await contractPricingInformation.selectGenericToggle(toggle).click();
    });

    When('{string} is selected as No on the Customer Information Screen', { timeout: CommonTasks.VERYLONGWAIT }, async (toggle: string) => {
        await browser.wait(EC.presenceOf(contractPricingInformation.selectGenericToggle(toggle)), CommonTasks.LONGWAIT);
        // Toggle ON
        await contractPricingInformation.selectGenericToggle(toggle).click();
        // Toggle OFF
        return await contractPricingInformation.selectGenericToggle(toggle).click();
    });

    When('User selects Include Transfer Fee Toggle as No', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.selectGenericToggle('Include Transfer Fees')), CommonTasks.LONGWAIT);
        return await contractPricingInformation.selectGenericToggle('Include Transfer Fees').click();
    });

    When('User hovers over info icon for Transfer Fees Toggle', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(
            contractPricingInformation.identifyGenericInfoIcon('Include Transfer Fees')), CommonTasks.LONGWAIT);
        return await browser.actions().mouseMove(contractPricingInformation.identifyGenericInfoIcon('Include Transfer Fees')).perform();
    });

    When('User hovers over info icon for GFS Label Assessment Fee Toggle', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(
            contractPricingInformation.identifyGenericInfoIcon('Include GFS Label Assessment Fee')), CommonTasks.LONGWAIT);
        return await browser.actions().mouseMove(
            contractPricingInformation.identifyGenericInfoIcon('Include GFS Label Assessment Fee')).perform();
    });

    Then('Cost Model Section is displayed to the User', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.costModelSection.isPresent()).to.eventually.be.true;
    });

    Then('Help text for Transfer Fees displayed to User', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(contractPricingInformation.genericHoverOver.getText()).to.eventually.equal
        ('This fee covers the costs of purchasing a product into one Distribution Center and transferring to another Distribution Center.')
    });

    Then('Help text for GFS Label Assessment Fee displayed to User', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(contractPricingInformation.genericHoverOver.getText()).to.eventually.equal(
            'This fee covers the Marketing costs of the Gordon Food Sevice Label and/or Gordon Food Service Branded items.')
    });

    Then('Cost Model Section is not displayed to the User', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.costModelSection.isPresent()).to.eventually.be.false;
    });

    Then('Warning text is displayed to the User that an extra level of approval is required',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.genericWarning.getText()).to.eventually.equal(
            'This contract will require an extra level of approval as Transfer Fees is not included.')
    });
});
