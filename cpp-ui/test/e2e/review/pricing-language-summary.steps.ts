import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, DistributionCentersPageObject,
    MarkupPageObject, ReviewPageObject, SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const distributionCenters = new DistributionCentersPageObject();
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const markup = new MarkupPageObject();
    const review = new ReviewPageObject();
    const splitCaseFee = new SplitCaseFeePageObject();
    const common = new CommonTasks();
    const EC = protractor.ExpectedConditions;

    When('User enters all CPP pricing information with cost schedule package as GFS Fiscal calender', {
            timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.gfsFiscalCalendar), CommonTasks.LONGWAIT);
        await contractPricingInformation.gfsFiscalCalendar.click();
        await contractPricingInformation.nextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.wait(EC.presenceOf(distributionCenters.nextButton), CommonTasks.LONGWAIT);
        await distributionCenters.nextButton.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await markup.expireLowerQuestionYes.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.MEDIUMWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    When('User enters all CPP pricing information with cost schedule package as Gregorian Calendar', {
            timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.gregorianCalendar), CommonTasks.LONGWAIT);
        await contractPricingInformation.gregorianCalendar.click();
        await contractPricingInformation.nextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.MEDIUMWAIT);
        await common.genericNextButton.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.MEDIUMWAIT);
        await markup.nextButton.click();
        await markup.expireLowerQuestionYes.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    When('User enters all CPP pricing information with selecting markup based on sell to Yes', {
            timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.gfsFiscalCalendar), CommonTasks.LONGWAIT);
        await contractPricingInformation.gfsFiscalCalendar.click();
        await contractPricingInformation.nextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.MEDIUMWAIT);
        await common.genericNextButton.click();
        await markup.expireLowerQuestionYes.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.MEDIUMWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    When('User enters all CPP pricing information with selecting markup based on sell to No', {
            timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.gfsFiscalCalendar), CommonTasks.LONGWAIT);
        await contractPricingInformation.gfsFiscalCalendar.click();
        await contractPricingInformation.nextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.MEDIUMWAIT);
        await common.genericNextButton.click();
        await markup.expireLowerQuestionYes.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.markupBasedOnSellToggle), CommonTasks.MEDIUMWAIT);
        await markup.markupBasedOnSellToggle.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.MEDIUMWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    When('User enters all CPP pricing information with Formal Price Audit Privileges selected as No', {
            timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.showTogglesQuestionYes), CommonTasks.LONGWAIT);
        await contractPricingInformation.showTogglesQuestionYes.click();
        await contractPricingInformation.nextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.MEDIUMWAIT);
        await common.genericNextButton.click();
        await markup.expireLowerQuestionYes.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.MEDIUMWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    When('User enters all CPP pricing information with Formal Price Audit Privileges selected as Yes', {
            timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.showTogglesQuestionYes), CommonTasks.LONGWAIT);
        await contractPricingInformation.showTogglesQuestionYes.click();
        await contractPricingInformation.formalPriceAuditPrivilegesToggle.click();
        await contractPricingInformation.priceverificationPrivilegesToggle.click();
        await browser.wait(EC.presenceOf(contractPricingInformation.nextButton), CommonTasks.MEDIUMWAIT);
        await contractPricingInformation.nextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.MEDIUMWAIT);
        await common.genericNextButton.click();
        await markup.expireLowerQuestionYes.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.MEDIUMWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    When('User enters all CPP pricing information with only Transfer fee selected', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.showTogglesQuestionYes), CommonTasks.LONGWAIT);
        await contractPricingInformation.showTogglesQuestionYes.click();
        await contractPricingInformation.priceverificationPrivilegesToggle.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await contractPricingInformation.selectGenericToggle('GFS Label Assessment Fee');
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await contractPricingInformation.nextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.MEDIUMWAIT);
        await common.genericNextButton.click();
        await markup.expireLowerQuestionYes.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.MEDIUMWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    When('User enters all CPP pricing information with only GFS Label Assessment Fee selected', {
            timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(contractPricingInformation.showTogglesQuestionYes), CommonTasks.LONGWAIT);
        await contractPricingInformation.showTogglesQuestionYes.click();
        await contractPricingInformation.priceverificationPrivilegesToggle.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await contractPricingInformation.selectGenericToggle('Include Transfer Fees');
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await contractPricingInformation.nextButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.MEDIUMWAIT);
        await common.genericNextButton.click();
        await markup.expireLowerQuestionYes.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.MEDIUMWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.MEDIUMWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    Then('Correct pricing language is displayed for Gregorian Calendar', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.durationOfGFSCost.getText()).to.eventually.contain('first Monday of the calendar month');
    });

    Then('Correct pricing language is displayed for GFS Cost', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.durationOfGFSCost.getText()).to.eventually.contain('first Monday of GFS') &&
            expect(review.durationOfGFSCost.getText()).to.eventually.contain('fiscal month');
    });

    Then('Correct pricing language is displayed for markup based on sell selected as Yes', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.sellPriceReview.getText()).to.eventually.contain(
            'the price is Cost divided by the difference between one');
    });

    Then('Correct pricing language is displayed for markup based on sell selected as No', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.sellPriceReview.getText()).to.eventually.contain('set forth in below');
    });

    Then('No Pricing language is displayed for Formal Price Audit Privileges selected as No', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.priceAuditReview.isPresent()).to.eventually.be.false;
    });

    Then('No Pricing language is displayed for Price Verification privileges selected as No', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.priceAuditReview.isPresent()).to.eventually.be.false;
    });

    Then('Correct pricing language is displayed for Formal Price Audit Privileges selected as Yes', {
        timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.priceAuditReview.isPresent()).to.eventually.be.true &&
        expect(review.priceAuditReview.getText()).to.eventually.contain(
            'GFS will provide a copy of the Vendor Contract used to determine the Contract Cost');
    });

    Then('Correct pricing language is displayed for Price Verification privileges selected as Yes', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.priceAuditReview.isPresent()).to.eventually.be.true &&
        expect(review.priceAuditReview.getText()).to.eventually.contain(
            'GFS will provide the Pricing Report at reasonable intervals that Customer requests');
    });

    Then('Correct pricing language is displayed for all Markup values entered', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.markupsSellPriceReview.isPresent()).to.eventually.be.true &&
        expect(review.markupsStructuresReview.isPresent()).to.eventually.be.true;
    });

    Then('Correct pricing language is displayed for split case values entered', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.splitCaseFeeReview.isPresent()).to.eventually.be.true;
    });

    Then('Correct pricing language is displayed for distribution centers selected', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return expect(review.distributionCentersReview.isPresent()).to.eventually.be.true &&
        expect(review.firstDistributionCenterReview.isPresent()).to.eventually.be.true
    });

    Then('Correct pricing language is displayed when GFS Label Assessment Fee & Transfer Fee are selected', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.costOfProductsReview.getText()).to.eventually.contain(
            'Products that are branded to GFS or a trade name used exclusively by GFS');
    });

    Then('Correct pricing language is displayed when only Transfer fee is selected', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.costOfProductsReview.getText()).to.eventually.contain(
            'Cross-Dock Fees and Transfers Fees between GFS distribution centers may be included');
    });

    Then('Correct pricing language is displayed when only GFS Label Assessment Fee is selected', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.costOfProductsReview.getText()).to.eventually.contain(
            'Cost may also include fees assessed by GFS for brand labeling');
    });

});
