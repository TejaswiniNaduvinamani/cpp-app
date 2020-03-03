import { defineSupportCode } from 'cucumber';
import { browser } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();

    Then('Header title is Customer Price Profile', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.headerTitle.getText()).to.eventually.equal('Customer Price Profile');
    });

    Then('Logo is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.applogo).to.not.be.null;
    });

    Then('Screen title is Create Price Profile for New Contract', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.screenTitle.getText()).to.eventually.equal('Create Price Profile for New Contract');
    });

    Then('Stepper 1 is Customer Information', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.stepper1.getText()).to.eventually.equal('Contract Pricing Information');
    });

    Then('Stepper 2 is Distrubution Centers', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.stepper2.getText()).to.eventually.equal('Distribution Centers');
    });

    Then('Stepper 3 is Markup', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.stepper3.getText()).to.eventually.equal('Markup');
    });

    Then('Stepper 4 is Split Case Fee', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.stepper4.getText()).to.eventually.equal('Split Case Fee');
    });

    Then('Stepper 5 is Review', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.stepper5.getText()).to.eventually.equal('Review');
    });

    Then('User is unable to navigate directly to Subsequent steppers', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        return await expect(contractPricingInformation.stepper2Link.getAttribute('class')).to.eventually.equal('disabled') &&
        expect(contractPricingInformation.stepper3Link.getAttribute('class')).to.eventually.equal('disabled') &&
        expect(contractPricingInformation.stepper4Link.getAttribute('class')).to.eventually.equal('disabled') &&
        expect(contractPricingInformation.stepper5Link.getAttribute('class')).to.eventually.equal('disabled');
    });

    Then('User is able to navigate directly to previous stepper by clicking on it', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.SMALLWAIT);
        await contractPricingInformation.stepper1.click();
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(browser.getCurrentUrl()).to.eventually.contain('pricinginformation');
    });
});
