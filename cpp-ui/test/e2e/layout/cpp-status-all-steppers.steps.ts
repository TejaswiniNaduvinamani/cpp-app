import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, HeaderPageObject, MarkupPageObject, SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const header = new HeaderPageObject();
    const common = new CommonTasks();
    const markup = new MarkupPageObject();
    const splitCaseFee = new SplitCaseFeePageObject();
    let cppStatus;
    const EC = protractor.ExpectedConditions;

    Then('CPP Status is displayed on Contract Pricing stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppStatusHeader), CommonTasks.LONGWAIT);
        await header.cppStatusHeader.getText().then(function (text) {
            cppStatus = text.split(': ')[1];
        });
        await expect(header.cppStatusHeader.isPresent()).to.eventually.be.true;
        await browser.wait(EC.presenceOf(contractPricingInformation.nextButton), CommonTasks.LONGWAIT);
        return await contractPricingInformation.nextButton.click();
    });

    Then('CPP Status is displayed on Distribution center stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppStatusHeader), CommonTasks.LONGWAIT);
        await header.cppStatusHeader.getText().then(function (text) {
            expect(cppStatus).to.equal(text.split(': ')[1]);
        });
        await expect(header.cppStatusHeader.isPresent()).to.eventually.be.true;
        await browser.wait(EC.presenceOf(common.genericNextButton), CommonTasks.MEDIUMWAIT);
        await common.genericNextButton.click();
    });

    Then('CPP Status is displayed on Markup stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppStatusHeader), CommonTasks.LONGWAIT);
        await header.cppStatusHeader.getText().then(function (text) {
            expect(cppStatus).to.equal(text.split(': ')[1]);
        });
        await expect(header.cppStatusHeader.isPresent()).to.eventually.be.true;
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.defaultGridSaveButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.expireLowerQuestionSelectionYes.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await markup.nextButton.click();
    });

    Then('CPP Status is displayed on Split Case Fee stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppStatusHeader), CommonTasks.LONGWAIT);
        await header.cppStatusHeader.getText().then(function (text) {
            expect(cppStatus).to.equal(text.split(': ')[1]);
        });

        await expect(header.cppStatusHeader.isPresent()).to.eventually.be.true;
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    Then('CPP Status is displayed on Review stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppIdHeader), CommonTasks.MEDIUMWAIT);
        await header.cppStatusHeader.getText().then(function (text) {
            expect(cppStatus).to.equal(text.split(': ')[1]);
        });
        await expect(header.cppStatusHeader.isPresent()).to.eventually.be.true;
        await browser.wait(EC.presenceOf(header.assignmentTab), CommonTasks.MEDIUMWAIT);
        await header.assignmentTab.click();
    });

    Then('CPP Status is displayed on Assignments tab', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppIdHeader), CommonTasks.MEDIUMWAIT);
        await header.cppStatusHeader.getText().then(function (text) {
            expect(cppStatus).to.equal(text.split(': ')[1]);
        });

        await expect(header.cppStatusHeader.isPresent()).to.eventually.be.true;
        return await expect(header.versionNumberHeader.isPresent()).to.eventually.be.true;
    });

});
