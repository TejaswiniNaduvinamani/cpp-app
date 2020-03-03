import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, MarkupPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const EC = protractor.ExpectedConditions;
    
    When('Enters mandatory Furtherance information on the first stepper', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.elementToBeClickable(contractPricingInformation.FurtheranceDateField), CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.FurtheranceDateField.clear();
        await contractPricingInformation.FurtheranceDateField.sendKeys('07/24/2019');
        await contractPricingInformation.FurtheranceReasonForChangeField.clear();
        await contractPricingInformation.FurtheranceReasonForChangeField.sendKeys('abcd123#Comment');
        await contractPricingInformation.FurtheranceContractReferenceField.clear();
        return await contractPricingInformation.FurtheranceContractReferenceField.sendKeys('abcd123#Reference');
    });

    Then('Clicks Continue to enter Furtherance mode', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await expect(contractPricingInformation.furtherancePopup.isPresent()).to.eventually.be.true;
        return await contractPricingInformation.continueButtonfurtherancePopup.click();
    });
    
    Then('Existing markup values are editable', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.defaultGridFirstRowMarkupField.clear();
        return await markup.defaultGridFirstRowMarkupField.sendKeys('12.34');
    });

    Then('Subgroup Markups are editable', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.default1stSubgroupRowMarkupValue.clear();
        return await markup.default1stSubgroupRowMarkupValue.sendKeys('12.34');
    });

    Then('Item Level Markups are editable', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.defaultItemLevelFirstRowMarkupField.clear();
        return await markup.defaultItemLevelFirstRowMarkupField.sendKeys('12.34');
    });

    Then('Real Item Level markup rows can be added', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await markup.defaultAddItemLevelMarkupButton.click();
    });

    Then('Real Items Level markup rows can be deleted', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.defaultItemLevel3rdRowTrashIcon.click();
        await markup.deleteButtonConfirmationQuestionYes.click();
    });

    Then('Future Item Level Markup rows can be deleted', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await markup.defaultItemLevel2ndRowTrashIcon.click();
        await markup.deleteButtonConfirmationQuestionYes.click();
    });

});
