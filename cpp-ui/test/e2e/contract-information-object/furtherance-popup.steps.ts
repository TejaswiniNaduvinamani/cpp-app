import { defineSupportCode } from 'cucumber';
import { browser, element, by } from 'protractor';

import { CommonTasks, MarkupPageObject , ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ Given, When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const contractPricingInformation = new ContractPricingInformationPageObject();

    Then('Furtherance popup is displayed on screen', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(contractPricingInformation.furtherancePopup.isPresent()).to.eventually.be.true;
    });

    When('User clicks on Continue button on Furtherance popup', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await contractPricingInformation.continueButtonfurtherancePopup.click();    
    });

    Then('Stepper 1 is loaded in Furtherance Mode', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(contractPricingInformation.furtheranceSection.isPresent()).to.eventually.be.true;
    });

});
