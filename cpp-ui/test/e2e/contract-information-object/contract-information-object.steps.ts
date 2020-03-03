import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, DistributionCentersPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const distributionCenters = new DistributionCentersPageObject();
    const EC = protractor.ExpectedConditions;

    When('User navigates to Distribution Centers Stepper', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.nextButton), CommonTasks.LONGWAIT);
        await contractPricingInformation.nextButton.click();
        await browser.wait(EC.presenceOf(contractPricingInformation.exhibitPopUpSaveChangesButton), CommonTasks.LONGWAIT);
        return await contractPricingInformation.exhibitPopUpSaveChangesButton.click();
    });

    Then('Contract Name is correctly displayed on CPP Customer Information screen', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.contractName.getText()).to.eventually.equal('TEST CONTRACT');
    });

    Then('Contract Type is correctly displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.contractType.getText()).to.eventually.equal('Distribution Agreement Regional');
    });

    Then('Contract Start Date is correctly displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
       return await expect(contractPricingInformation.contractStartDate.getText()).to.eventually.equal('07/24/2018');
    });

    Then('Contract End Date is correctly displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
       return await expect(contractPricingInformation.contractEndDate.getText()).to.eventually.equal('10/31/2018');
    });

    Then('Pricing Start Date is not displayed', async () => {
        return await expect(contractPricingInformation.pricingStartDateAbsence).to.not.be.null;
    });

    Then('Pricing End Date is not displayed', async () => {
        return await expect(contractPricingInformation.pricingEndDateAbsence).to.not.be.null;
    });

    Then('Pricing Start Date is displayed correctly', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
       await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
       return await expect(distributionCenters.pricingEffectiveDate.getText()).to.eventually.equal('07/24/2018');
    });

    Then('Pricing End Date is displayed correctly', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
      return await expect(distributionCenters.pricingExpirationDate.getText()).to.eventually.equal('01/01/9999');
    });

});
