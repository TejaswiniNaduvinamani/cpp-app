import { defineSupportCode } from 'cucumber';

import { CommonTasks, ContractPricingInformationPageObject, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const markup = new MarkupPageObject();

    When('User navigates to markup stepper', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.continueButtonfurtherancePopup.click();
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);          
        return await contractPricingInformation.stepper3.click();
    });

    Then('Edit icon is displayed for future item row', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await expect (markup.editButtonFutureItemFurtherance.isPresent()).to.eventually.be.true;    
    });

    Then('Item mapping Pop-up is displayed on click of Edit Icon', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await markup.editButtonFutureItemFurtherance.click();
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect (markup.editPopupFutureItemFurtherance.isPresent()).to.eventually.be.true; 
    });

});

