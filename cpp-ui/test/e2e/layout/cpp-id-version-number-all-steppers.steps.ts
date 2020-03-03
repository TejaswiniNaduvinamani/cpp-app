import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject, DistributionCentersPageObject,
    HeaderPageObject, MarkupPageObject, SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();
    const distributionCenters = new DistributionCentersPageObject();
    const header = new HeaderPageObject();
    const markup = new MarkupPageObject();
    const splitCaseFee = new SplitCaseFeePageObject();
    let cppId, versionNumber;
    const EC = protractor.ExpectedConditions;

    Then('CPP Id and version number are displayed on Contract Pricing stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppIdHeader), CommonTasks.LONGWAIT);
        await header.cppIdHeader.getText().then(function (text) {
            cppId = text.split(': ')[1];
        });
        await header.versionNumberHeader.getText().then(function (text) {
            versionNumber = text.split(': ')[1];
        });
        await expect(header.cppIdHeader.isPresent()).to.eventually.be.true;
        await expect(header.versionNumberHeader.isPresent()).to.eventually.be.true;
        await browser.wait(EC.presenceOf(contractPricingInformation.nextButton), CommonTasks.LONGWAIT);
        await contractPricingInformation.nextButton.click();
        await browser.wait(EC.presenceOf(contractPricingInformation.exhibitPopUpSaveChangesButton), CommonTasks.MEDIUMWAIT);
        return await contractPricingInformation.exhibitPopUpSaveChangesButton.click();
    });

    Then('CPP Id and version number are displayed on Distribution center stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppIdHeader), CommonTasks.LONGWAIT);
        await header.cppIdHeader.getText().then(function (text) {
            expect(cppId).to.equal(text.split(': ')[1]);
        });
        await header.versionNumberHeader.getText().then(function (text) {
            expect(versionNumber).to.equal(text.split(': ')[1]);
        });
        await expect(header.cppIdHeader.isPresent()).to.eventually.be.true;
        await expect(header.versionNumberHeader.isPresent()).to.eventually.be.true;
        await browser.wait(EC.presenceOf(distributionCenters.nextButton), CommonTasks.LONGWAIT)
        await distributionCenters.nextButton.click();
    });

    Then('CPP Id and version number are displayed on Markup stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppIdHeader), CommonTasks.LONGWAIT);
        await header.cppIdHeader.getText().then(function (text) {
            expect(cppId).to.equal(text.split(': ')[1]);
        });
        await header.versionNumberHeader.getText().then(function (text) {
            expect(versionNumber).to.equal(text.split(': ')[1]);
        });
        await expect(header.cppIdHeader.isPresent()).to.eventually.be.true;
        await expect(header.versionNumberHeader.isPresent()).to.eventually.be.true;
        await markup.defaultGridSaveButton.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        await markup.expireLowerQuestionSelectionYes.click();
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await markup.nextButton.click();
    });

    Then('CPP Id and version number are displayed on Split Case Fee stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppIdHeader), CommonTasks.LONGWAIT);
        await header.cppIdHeader.getText().then(function (text) {
            expect(cppId).to.equal(text.split(': ')[1]);
        });
        await header.versionNumberHeader.getText().then(function (text) {
            expect(versionNumber).to.equal(text.split(': ')[1]);
        });
        await expect(header.cppIdHeader.isPresent()).to.eventually.be.true;
        await expect(header.versionNumberHeader.isPresent()).to.eventually.be.true;
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.MEDIUMWAIT);
        return await splitCaseFee.nextButton.click();
    });

    Then('CPP Id and version number are displayed on Review stepper', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppIdHeader), CommonTasks.MEDIUMWAIT);
        await header.cppIdHeader.getText().then(function (text) {
            expect(cppId).to.equal(text.split(': ')[1]);
        });
        await header.versionNumberHeader.getText().then(function (text) {
            expect(versionNumber).to.equal(text.split(': ')[1]);
        });
        await expect(header.cppIdHeader.isPresent()).to.eventually.be.true;
        await expect(header.versionNumberHeader.isPresent()).to.eventually.be.true;
        await browser.wait(EC.presenceOf(header.assignmentTab), CommonTasks.MEDIUMWAIT);
        return await header.assignmentTab.click();
    });

    Then('CPP Id and version number are displayed on Assignments tab', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.cppIdHeader), CommonTasks.MEDIUMWAIT);
        await header.cppIdHeader.getText().then(function (text) {
            expect(cppId).to.equal(text.split(': ')[1]);
        });
        await header.versionNumberHeader.getText().then(function (text) {
            expect(versionNumber).to.equal(text.split(': ')[1]);
        });
        await expect(header.cppIdHeader.isPresent()).to.eventually.be.true;
        return await expect(header.versionNumberHeader.isPresent()).to.eventually.be.true;
    });

});
