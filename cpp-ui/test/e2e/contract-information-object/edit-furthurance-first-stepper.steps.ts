import { defineSupportCode } from 'cucumber';
import { protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();

    When('User enters Furtherance Effective Date less than Current Date', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.continueButtonfurtherancePopup.click();
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.FurtheranceDateField.clear();
        await contractPricingInformation.FurtheranceDateField.sendKeys('06/25/1990');
        await contractPricingInformation.FurtheranceDateField.sendKeys(protractor.Key.TAB);
    });

    Then('User sees error message: Furtherance cannot be made effective for dates earlier than today.',
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(contractPricingInformation.FurthuranceEffectivetDateErrorMessage.getText())
        .to.eventually.contain('Furtherance effective date cannot be less than the current date');  
    });

    When('User enters Furtherance Effective Date greater than Contract End Date', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.FurtheranceDateField.click();
        await contractPricingInformation.FurtheranceDateField.clear();
        await contractPricingInformation.FurtheranceDateField.sendKeys('10/31/2021');
        return await contractPricingInformation.FurtheranceDateField.sendKeys(protractor.Key.TAB);
    });

    Then('User sees error message: Furtherance effective date cannot be greater than the contract end date.', 
        { timeout: CommonTasks.VERYLONGWAIT }, async () => {
            await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
            return await expect(contractPricingInformation.FurthuranceEffectivetDateErrorMessage.getText())
            .to.eventually.contain('Furtherance effective date cannot be greater than the contract end date');
    });

    When('User enters an invalid date format in furthurance date field', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.FurtheranceDateField.click();
        await contractPricingInformation.FurtheranceDateField.clear();
        await contractPricingInformation.FurtheranceDateField.sendKeys('abcd');
        await contractPricingInformation.FurtheranceDateField.sendKeys(protractor.Key.TAB);
    });

    Then('User sees error message to enter correct date for in furthurance date field', {timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(contractPricingInformation.FurthuranceEffectivetDateErrorMessage.getText())
        .to.eventually.contain('Please enter a valid date in the format \'mm/dd/yyyy\'');
    });


    Then('User is able to enter Reason for change', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.FurtheranceReasonForChangeField.sendKeys('abcd123#');
    });

    Then('User is able to enter Contract Reference', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.FurtheranceContractReferenceField.sendKeys('abcd123#');
    });

});
