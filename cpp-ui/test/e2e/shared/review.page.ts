import { element, by } from 'protractor';

import { IdentificationType, CommonTasks } from './common-tasks';

// Component Locators
export const LocatorsR = {

    durationOfGFSCost: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div/*[contains(text(), "Duration of GFS Cost")]/../following-sibling::div)[1]/div[1]'
    },

    sellPriceReview: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[contains(text(), "Sell Price")]/../following-sibling::div)[1]/div[1]'
    },

    priceAuditReview: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[contains(text(), "Price Audit")]/../following-sibling::div)[1]/div[1]'
    },

    markupsSellPriceReview: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[contains(text(), "Sell Price")]/../following-sibling::div)[1]/div[1]'
    },

    markupsStructuresReview: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[contains(text(), "Markup Structures")]/../following-sibling::div)[1]/div[1]'
    },

    splitCaseFeeReview: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[contains(text(), "Split Case Fee")]/..'
    },

    distributionCentersReview: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[contains(text(), "Distribution Centers")]'
    },

    firstDistributionCenterReview: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[contains(text(), "Distribution Centers")]/../../div[3]'
    },

    costOfProductsReview: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[contains(text(), "Cost of Products")]/../following-sibling::div)[1]/div[1]'
    },

    genericMarkupTypeDefinitions: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[contains(text(),"3. Markups")]/../following-sibling::div/descendant::div[contains(text(), "Replace_Text_Value")]'
    },

    markupStepperEditLink: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[contains(text(), "3. Markups")]/../descendant::*[text()="Edit"]'
    },

    commitPriceProfileButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/BUTTON[contains(text(), " Commit Price Profile and Return to Contract ")]'
    },

    subGroupItemRow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//td[contains(@class, "item-description")][contains(text(), "SAFETY EQUIPMENT & SUPPLIES")]'
    },

    itemLevelRow: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//td[contains(@class, "item-description")][contains(text(), "future Item")]'
    }

}

export class ReviewPageObject extends CommonTasks {

    // Pricing Language For Duration Of GFS Cost Section
    durationOfGFSCost = this.elementLocator(LocatorsR.durationOfGFSCost);

    // Pricing Language For Markup Based On Sell Section 
    sellPriceReview = this.elementLocator(LocatorsR.sellPriceReview);

    // Pricing Audit & Formal Audit Toggles Review Section
    priceAuditReview = this.elementLocator(LocatorsR.priceAuditReview);

    // Markup Sell Price Review Section
    markupsSellPriceReview = this.elementLocator(LocatorsR.markupsSellPriceReview);

    // Markup Structures Review Section
    markupsStructuresReview = this.elementLocator(LocatorsR.markupsStructuresReview);

    // Split Case Fee Review Section
    splitCaseFeeReview = this.elementLocator(LocatorsR.splitCaseFeeReview);

    // Distribution Centers Review Section
    distributionCentersReview = this.elementLocator(LocatorsR.distributionCentersReview);

    // First Distribution Center Review
    firstDistributionCenterReview = this.elementLocator(LocatorsR.firstDistributionCenterReview);

    // Cost Of Products Review Section
    costOfProductsReview = this.elementLocator(LocatorsR.costOfProductsReview);

    // Sell Unit Markup Type Definitions
    sellUnitMarkupTypeDefinitions = this.elementLocator(LocatorsR.genericMarkupTypeDefinitions);

    // Markup Stepper Edit Link
    markupStepperEditLink = this.elementLocator(LocatorsR.markupStepperEditLink);

    // Commit Price profile button
    commitPriceProfileButton = this.elementLocator(LocatorsR.commitPriceProfileButton);

    // Sub Group Item Row
    subGroupItemRow = this.elementLocator(LocatorsR.subGroupItemRow);

    // Item Level Row
    itemLevelRow = this.elementLocator(LocatorsR.itemLevelRow);

    // Identify Generic Markup Type Definition
    identifyGenericMarkupTypeDefinition(textReplaceValue) {
        return element(by.xpath(LocatorsR.genericMarkupTypeDefinitions.value.replace('Replace_Text_Value', textReplaceValue)));
    }
}
