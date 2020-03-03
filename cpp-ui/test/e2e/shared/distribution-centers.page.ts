import { element, by } from 'protractor';

import { IdentificationType, CommonTasks } from './common-tasks';

// Component Locators
export const LocatorsDc = {
    pricingEffectiveDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-contract-information/descendant::div[text()="Pricing Effective Date"]' +
            '/following-sibling::div[@class="table-cell-data"]'
    },

    pricingExpirationDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-contract-information/descendant::div[text()="Pricing Expiration Date"]' +
            '/following-sibling::div[@class="table-cell-data"]'
    },

    distributionCenter: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//div[@class="alignDC checkbox"]/label)[replace_value]'
    },

    selectAll: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'select-all'
    },

    clear: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'clear'
    },

    errorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//div[contains(@class, "warning-text")]/descendant::em[not(contains(@class, "exclamation"))]'
    },

    nextButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//button[contains(text(), "Continue to:")]'
    }
}

export class DistributionCentersPageObject extends CommonTasks {

    // Pricing Effective Date
    pricingEffectiveDate = this.elementLocator(LocatorsDc.pricingEffectiveDate);

    // Pricing Expiration Date
    pricingExpirationDate = this.elementLocator(LocatorsDc.pricingExpirationDate);

    // Select All Button
    selectAll = this.elementLocator(LocatorsDc.selectAll);

    // Clear Button
    clear = this.elementLocator(LocatorsDc.clear);

    // Next Button
    nextButton = this.elementLocator(LocatorsDc.nextButton);

    // Next Button
    errorMessage = this.elementLocator(LocatorsDc.errorMessage);

    // Get Distribution Center
    getDistributionCenter(id: any) {
        return element(by.xpath(LocatorsDc.distributionCenter.value.replace('replace_value', id)));
    }

    // Click All Distribution Centers
    async clickAllDistributionCenters() {
        await element.all(by.xpath('.//div[@class="alignDC checkbox"]/label')).each(function (item) {
            item.click();
        });
    }

    // Click First Distribution Center
    clickFirstDistributionCenter() {
        element(by.xpath('(.//div[@class="alignDC checkbox"]/label)[1]')).click();
    }

}
