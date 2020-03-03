import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();

    Then('All Steppers are displayed on CPP Customer Information Screen', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.stepper1.getText()).to.eventually.equal('Contract Pricing Information') &&
        expect(contractPricingInformation.stepper2.getText()).to.eventually.equal('Distribution Centers') &&
        expect(contractPricingInformation.stepper3.getText()).to.eventually.equal('Markup') &&
        expect(contractPricingInformation.stepper4.getText()).to.eventually.equal('Split Case Fee') &&
        expect(contractPricingInformation.stepper5.getText()).to.eventually.equal('Review');
    });

    Then('Formal Price Audit Privileges toggle is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.formalPriceAuditPrivilegesToggle).to.not.be.null;
    });

    Then('Schedule for Cost Changes section is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.scheduleForCostChanges).to.not.be.null;
    });

});
