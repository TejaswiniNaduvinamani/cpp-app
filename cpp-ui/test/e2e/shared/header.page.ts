import { element, by } from 'protractor';
import { IdentificationType, CommonTasks } from './common-tasks';

// Component Locators
export const LocatorsHp = {
    priceProfileTab: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/a[text()="Price Profile"]'
    },

    overViewTab: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/a[text()="Overview"]'
    },

    assignmentTab: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/a[text()="Assignments"]'
    },

    contractInformationObject: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-contract-information/div[contains(@class, "info-bar")]'
    },

    cppIdHeader: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//DIV[contains(text(), "CPP ID")]'
    },

    versionNumberHeader: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//DIV[contains(text(),"CPP Version")]'
    },

    returntoContractButton: {
        type: IdentificationType[IdentificationType.ButtonText],
        value: 'Return to Contract'
    },

    cppStatusHeader: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//DIV[contains(text(), "Status")]'
    },

}

export class HeaderPageObject extends CommonTasks {

    // Header Tab Objects
    priceProfileTab = this.elementLocator(LocatorsHp.priceProfileTab);
    overViewTab = this.elementLocator(LocatorsHp.overViewTab);
    assignmentTab = this.elementLocator(LocatorsHp.assignmentTab);
    contractInformationObject = this.elementLocator(LocatorsHp.contractInformationObject);

    // CPP Id in header
    cppIdHeader = this.elementLocator(LocatorsHp.cppIdHeader);

    // Version Number Header 
    versionNumberHeader = this.elementLocator(LocatorsHp.versionNumberHeader);

    // Return to Contract Button
    returntoContractButton = this.elementLocator(LocatorsHp.returntoContractButton);

    // Contract Status Header
    cppStatusHeader = this.elementLocator(LocatorsHp.cppStatusHeader);
}
