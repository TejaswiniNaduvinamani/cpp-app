import { element, by } from 'protractor';

import { IdentificationType, CommonTasks } from './common-tasks';

// Component Locators
export const LocatorsSc = {

    pricingEffectiveDate: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'pricing-start-date-data'
    },

    pricingExpirationDate: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'pricing-end-date-data'
    },

    firstRowEffectiveDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//datatable-body-cell[4]/descendant::div[last()])[1]'
    },

    firstRowExpirationDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//datatable-body-cell[5]/descendant::div[last()])[1]'
    },

    splitCaseFeeFirstRow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[@class="datatable-body-cell-label"]/input)[1]'
    },

    firstRowDollarIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="datatable-body-cell-label"]/descendant::span[contains(text(),"$")])[1]'
    },

    firstRowPercentIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="datatable-body-cell-label"]/descendant::span[contains(text(),"%")])[1]'
    },

    copyToAllButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//button[text()="Copy To All"]'
    },

    approvalWarning: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/span[contains(text(),"This contract will require an extra level of approval as no split case fees is included")]'
    },

    recommendedWarning: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/SPAN[contains(text(),"Recommended values for split case fees are typically below 49% or $5.")]'
    },

    allSplitCaseFeeInputFields: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@class="datatable-body-cell-label"]/input'
    },

    feeTypeList: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-split-case/descendant::select'
    },

    nextButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/button[contains(text(), "Save and Continue to: Review")]'
    },

    viewModefirstRowDollarIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="datatable-body-cell-label"]/div[@class="view-mode-radio"]/descendant::span[contains(text(),"$")])[1]'
    },

    viewModefirstRowPercentIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="datatable-body-cell-label"]/div[@class="view-mode-radio"]/descendant::span[contains(text(),"%")])[1]'
    },

    genericInfoIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div/div[descendant::span[contains(text(), "Replace_Text_Value")]]/descendant::img'
    },

    genericHoverOver: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'tooltip-inner'
    },

}

export class SplitCaseFeePageObject extends CommonTasks {

    // Pricing Effective Date
    pricingEffectiveDate = this.elementLocator(LocatorsSc.pricingEffectiveDate);

    // Pricing Expiration Date
    pricingExpirationDate = this.elementLocator(LocatorsSc.pricingExpirationDate);

    // Effective Date
    firstRowEffectiveDate = this.elementLocator(LocatorsSc.firstRowEffectiveDate);

    // Expiration Date
    firstRowExpirationDate = this.elementLocator(LocatorsSc.firstRowExpirationDate);

    // Split Case Fee First Row
    SplitCaseFeeFirstRow = this.elementLocator(LocatorsSc.splitCaseFeeFirstRow);

    // First Row Dollar Icon
    firstRowDollarIcon = this.elementLocator(LocatorsSc.firstRowDollarIcon);

    // First Row Percentage Icon
    firstRowPercentIcon = this.elementLocator(LocatorsSc.firstRowPercentIcon);

    // Copy To All
    copyToAllButton = this.elementLocator(LocatorsSc.copyToAllButton);

    // Approval Warning
    approvalWarning = this.elementLocator(LocatorsSc.approvalWarning);

    // Recommended Warning
    recommendedWarning = this.elementLocator(LocatorsSc.recommendedWarning);

    // Next button to go Review Stepper
    nextButton = this.elementLocator(LocatorsSc.nextButton);

    // All Split Case Fee Input sFields
    allSplitCaseFeeInputFields = LocatorsSc.allSplitCaseFeeInputFields.value;

    // Fee Type List
    feeTypeList = this.elementLocator(LocatorsSc.feeTypeList);

    // First Row Dollar Icon in view Mode
    viewModefirstRowDollarIcon = this.elementLocator(LocatorsSc.viewModefirstRowDollarIcon);

    // First Row Percentage Icon in view mode
    viewModefirstRowPercentIcon = this.elementLocator(LocatorsSc.viewModefirstRowPercentIcon);

    // Generic Hover Over
    genericHoverOver = this.elementLocator(LocatorsSc.genericHoverOver);

    // Helper Text
    identifyGenericInfoIcon(textReplaceValue) {
        return element(by.xpath(LocatorsSc.genericInfoIcon.value.replace('Replace_Text_Value', textReplaceValue)));
    }

}
