import { defineSupportCode } from 'cucumber';
import { browser, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject, ReviewPageObject, SplitCaseFeePageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const review = new ReviewPageObject();
    const splitCaseFee = new SplitCaseFeePageObject();
    const EC = protractor.ExpectedConditions;

    When('Only selects Sell Unit as Markup Type for all the rows', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(markup.defaultGridFirstRowMarkupField), CommonTasks.LONGWAIT);
        await markup.defaultGridFirstRowMarkupField.clear();
        await markup.defaultGridFirstRowMarkupField.sendKeys('2');
        await browser.wait(EC.presenceOf(markup.defaultGridFirstRowMarkupType), CommonTasks.VERYLONGWAIT);
        await CommonTasks.selectValueFromDropdown(markup.defaultGridFirstRowMarkupType, '1');
        await markup.defaultGridCopyToAllButton.click();
        await browser.wait(EC.presenceOf(markup.defaultGridSaveButton), CommonTasks.VERYLONGWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.VERYLONGWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.VERYLONGWAIT);
        return await splitCaseFee.nextButton.click(); 
    });

    When('User goes back and adds Per Case Markup Type for any of the rows', { timeout: CommonTasks.EXTENSIVEWAIT }, async () => {
        await browser.wait(EC.presenceOf(review.markupStepperEditLink), CommonTasks.LONGWAIT);
        await review.markupStepperEditLink.click();
        await browser.wait(EC.presenceOf(markup.defaultGridFirstRowMarkupType), CommonTasks.LONGWAIT);
        await CommonTasks.selectValueFromDropdown(markup.defaultGridFirstRowMarkupType, '2');
        await browser.wait(EC.elementToBeClickable(markup.defaultGridSaveButton), CommonTasks.LONGWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.VERYLONGWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.VERYLONGWAIT);
        return await splitCaseFee.nextButton.click(); 
    });

    When('User goes back and adds Per Weight Markup Type for any of the rows', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(review.markupStepperEditLink), CommonTasks.LONGWAIT);
        await review.markupStepperEditLink.click();
        await browser.wait(EC.presenceOf(markup.defaultGrid2ndRowMarkupType), CommonTasks.LONGWAIT);
        await CommonTasks.selectValueFromDropdown(markup.defaultGrid2ndRowMarkupType, '3');
        await browser.wait(EC.elementToBeClickable(markup.defaultGridSaveButton), CommonTasks.LONGWAIT);
        await markup.defaultGridSaveButton.click();
        await browser.wait(EC.presenceOf(markup.nextButton), CommonTasks.VERYLONGWAIT);
        await markup.nextButton.click();
        await browser.wait(EC.presenceOf(splitCaseFee.nextButton), CommonTasks.VERYLONGWAIT);
        return await splitCaseFee.nextButton.click(); 
    });

    Then('Review Screen displays Markup Type Definitions for only Sell Unit', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.LONGWAIT);
        return await expect(review.identifyGenericMarkupTypeDefinition('Sell Unit').isPresent()).to.eventually.be.true && 
        expect(review.identifyGenericMarkupTypeDefinition('Per Case').isPresent()).to.eventually.be.false && 
        expect(review.identifyGenericMarkupTypeDefinition('Per Weight').isPresent()).to.eventually.be.false;
    });

    Then('Review screen displays Markup Type Definitions for both Sell Unit and Per Case', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.LONGWAIT);
        return await expect(review.identifyGenericMarkupTypeDefinition('Sell Unit').isPresent()).to.eventually.be.true && 
        expect(review.identifyGenericMarkupTypeDefinition('Per Case').isPresent()).to.eventually.be.true && 
        expect(review.identifyGenericMarkupTypeDefinition('Per Weight').isPresent()).to.eventually.be.false;
    });

    Then('Review screen displays Markup Type Definitions for Sell Unit, Per Case and Per Weight Markup Types', {
            timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.LONGWAIT);
        return await expect(review.identifyGenericMarkupTypeDefinition('Sell Unit').isPresent()).to.eventually.be.true && 
        expect(review.identifyGenericMarkupTypeDefinition('Per Case').isPresent()).to.eventually.be.true && 
        expect(review.identifyGenericMarkupTypeDefinition('Per Weight').isPresent()).to.eventually.be.true;
    });

});
