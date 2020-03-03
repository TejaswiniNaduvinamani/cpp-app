import { element, by } from 'protractor';

import { IdentificationType, CommonTasks } from './common-tasks';

// Component Locators
export const LocatorsCi = {
    appLabel: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[contains(@class, "app-label")]'
    },

    applogo: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[contains(@class, "gfsLogo")]'
    },

    stepperTitle: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/*[contains(@class, "setup-header")]/div/div'
    },

    footerText: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/footer/div[1]/div[1]'
    },

    stepper1: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[1]/a/span[last()]'
    },

    stepper2: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[2]/a/span[last()]'
    },

    stepper2Link: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[2]/a'
    },

    stepper3: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[3]/a/span[last()]'
    },

    stepper3Link: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[3]/a'
    },

    stepper4: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[4]/a/span[last()]'
    },

    stepper4Link: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[4]/a'
    },

    stepper5: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[5]/a/span[last()]'
    },

    stepper5Link: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '(.//*[@class="step"])[5]/a'
    },

    contractName: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-contract-information/descendant::div[text()="Contract Name"]/following-sibling::div[@class="table-cell-data"]'
    },

    contractType: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-contract-information/descendant::div[text()="Contract Type"]/following-sibling::div[@class="table-cell-data"]'
    },

    contractStartDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-contract-information/descendant::div[text()="Contract Start Date"]/following-sibling::div[@class="table-cell-data"]'
    },

    contractEndDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-contract-information/descendant::div[text()="Contract End Date"]/following-sibling::div[@class="table-cell-data"]'
    },

    pricingStartDateHidden: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@hidden]/td[@class="pricing-start-date"]'
    },

    pricingEndDateHidden: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@hidden]/td[@class="pricing-end-date"]'
    },

    pricingEffectivetDate: {
        type: IdentificationType[IdentificationType.Id],
        value: 'effectiveDate'
    },

    pricingEffectivetDateErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[descendant::label[contains(text(), "Pricing Effective Date")]]/span/span'
    },

    pricingExpirationDate: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//label[contains(text(), "Pricing Expiration Date")]/following-sibling::div'
    },

    scheduleForCostChanges: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/label[contains(text(), "Schedule for Cost Changes")]'
    },

    gfsFiscalCalendar: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[descendant::input[@id="fiscalCalendar"]]/label'
    },

    gregorianCalendar: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[descendant::input[@id="gregorianCalendar"]]/label'
    },

    priceVerificationPrivilegesToggle: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/app-toggle/div[descendant::span[contains(text(),"Price Verification Privileges")]]/label/span'
    },

    formalPriceAuditPrivilegesToggle: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/app-toggle/div[descendant::span[contains(text(),"Formal Price Audit Privileges")]]/label/span'
    },

    genericToggle: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/app-toggle/div[descendant::span[contains(text(),"Replace_Text_Value")]]/descendant::span[1]'
    },

    genericHoverOverText: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'tooltip-inner'
    },

    genericInfoIcon: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//span[@class="text"][contains(text(), "Replace_Text_Value")]/ancestor::div[@class="p-1"]/following-sibling::img'
        
    },

    genericWarning: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'warning-text'
    },

    costModelSection: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/label[contains(text(), "Cost Model")]'
    },

    nextButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/button[contains(text(), "Save & Continue")]'
    },

    priceverificationPrivilegRadioButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/label[@class="radio-label"][text()="Yes"]'
    },

    priceverificationPrivilegeToggle: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/app-toggle/div[descendant::span[contains(text(),"Price Verification Privileges")]]/label/span'
    },

    showTogglesQuestion: {
        type: IdentificationType[IdentificationType.ClassName],
        value: 'question-toggle'
    },

    showTogglesQuestionYes: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[descendant::input[@id="toggleQuestionYes"]]/label'
    },

    showTogglesQuestionNo: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="toggleQuestionNo"]'
    },

    clearExhibitPopUp: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//*[@id="deletePricingExhibit"][@class="modal fade show"]'
    },

    exhibitPopUpSaveChangesButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@class="modal-content"]/descendant::button[text()="Save Changes"]'
    },
    
    FurtheranceReasonForChangeField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/label[contains(text(), "Reason for Change")]/following-sibling::textarea[1]'
    },

    FurtheranceContractReferenceField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/label[contains(text(), "Contract Reference")]/following-sibling::textarea[1]'
    },

    FurtheranceDateField: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/input[@id="furtheranceEffectiveDate"]'
    },

    FurthuranceEffectivetDateErrorMessage: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[descendant::label[contains(text(), "Furtherance Effective Date")]]//descendant::span[contains(@class, "danger")]'
    },

    furtherancePopup: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[contains(text(),"furtherance ?")]'
    },

    continueButtonfurtherancePopup: {
        type: IdentificationType[IdentificationType.Xpath],
        value: './/div[@class="modal-footer"]/BUTTON[text()="Continue"]'
    },

    furtheranceSection: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//app-pricing-information/descendant::strong[text()="Furtherance"]'
    },


}

export class ContractPricingInformationPageObject extends CommonTasks {

    // Logo
    applogo = this.elementLocator(LocatorsCi.applogo);

    // Header
    headerTitle = this.elementLocator(LocatorsCi.appLabel);

    // Screen Title
    screenTitle = this.elementLocator(LocatorsCi.stepperTitle);

    // Contract Name
    contractName = this.elementLocator(LocatorsCi.contractName);

    // Contract Type
    contractType = this.elementLocator(LocatorsCi.contractType);

    // Contract Start Date
    contractStartDate = this.elementLocator(LocatorsCi.contractStartDate);

    // Contract End Date
    contractEndDate = this.elementLocator(LocatorsCi.contractEndDate);

    // Pricing Start Date
    pricingStartDateAbsence = this.elementLocator(LocatorsCi.pricingStartDateHidden);

    // Pricing End Date
    pricingEndDateAbsence = this.elementLocator(LocatorsCi.pricingEndDateHidden);

    // Price Verification Radio Button
    priceVerificationQuestionYesButton = this.elementLocator(LocatorsCi.priceverificationPrivilegRadioButton)

    // Price Verification Toggle
    priceVerificationQuestionToggle = this.elementLocator(LocatorsCi.formalPriceAuditPrivilegesToggle)

    // Price Verification Question Yes
    showTogglesQuestionYes = this.elementLocator(LocatorsCi.showTogglesQuestionYes)

    // Price Verification Question No
    showTogglesQuestionNo = this.elementLocator(LocatorsCi.showTogglesQuestionNo)

    // Steppers
    stepper1 = this.elementLocator(LocatorsCi.stepper1);

    stepper2 = this.elementLocator(LocatorsCi.stepper2);
    stepper2Link = this.elementLocator(LocatorsCi.stepper2Link);

    stepper3 = this.elementLocator(LocatorsCi.stepper3);
    stepper3Link = this.elementLocator(LocatorsCi.stepper3Link);

    stepper4 = this.elementLocator(LocatorsCi.stepper4);
    stepper4Link = this.elementLocator(LocatorsCi.stepper4Link);

    stepper5 = this.elementLocator(LocatorsCi.stepper5);
    stepper5Link = this.elementLocator(LocatorsCi.stepper5Link);

    // GFS Calendar
    gfsFiscalCalendar = this.elementLocator(LocatorsCi.gfsFiscalCalendar);

    // Gregorian Calendar
    gregorianCalendar = this.elementLocator(LocatorsCi.gregorianCalendar);

    // Formal Price Audit Privileges Toggle
    formalPriceAuditPrivilegesToggle = this.elementLocator(LocatorsCi.formalPriceAuditPrivilegesToggle);

    // Price Verification Privileges Toggle
    priceverificationPrivilegesToggle = this.elementLocator(LocatorsCi.priceVerificationPrivilegesToggle);

    // Pricing Effective Date
    pricingEffectivetDate = this.elementLocator(LocatorsCi.pricingEffectivetDate);

    // Pricing Expiration Date
    pricingExpirationDate = this.elementLocator(LocatorsCi.pricingExpirationDate);

    // Pricing Effective Date Error Message
    pricingEffectivetDateErrorMessage = this.elementLocator(LocatorsCi.pricingEffectivetDateErrorMessage);

    // Schedule For Cost Changes Section
    scheduleForCostChanges = this.elementLocator(LocatorsCi.scheduleForCostChanges);

    // Cost Model Section
    costModelSection = this.elementLocator(LocatorsCi.costModelSection);

    // Price verification and/or Formal Price Audit Privileges Question
    showTogglesQuestion = this.elementLocator(LocatorsCi.showTogglesQuestion);

    // Generic Hover Over
    genericHoverOver = this.elementLocator(LocatorsCi.genericHoverOverText);

    // Generic Warning Text
    genericWarning = this.elementLocator(LocatorsCi.genericWarning);

    // Next Button
    nextButton = this.elementLocator(LocatorsCi.nextButton);

    // Clear exibit popup
    clearExhibitPopUp = this.elementLocator(LocatorsCi.clearExhibitPopUp);

    // Exhibit Pop Up Save Changes Button
    exhibitPopUpSaveChangesButton = this.elementLocator(LocatorsCi.exhibitPopUpSaveChangesButton);

    // Furturance Reason For change text field
    FurtheranceReasonForChangeField = this.elementLocator(LocatorsCi.FurtheranceReasonForChangeField);

    // Furturance Contract Reference Field
    FurtheranceContractReferenceField = this.elementLocator(LocatorsCi.FurtheranceContractReferenceField);

    // Furturance Date Field
    FurtheranceDateField = this.elementLocator(LocatorsCi.FurtheranceDateField);

    // Furthurance Effective Date Error Message
    FurthuranceEffectivetDateErrorMessage = this.elementLocator(LocatorsCi.FurthuranceEffectivetDateErrorMessage);
    // Furturance exibit popup
    furtherancePopup = this.elementLocator(LocatorsCi.furtherancePopup);

    // Continue Button furturance exibit popup
    continueButtonfurtherancePopup = this.elementLocator(LocatorsCi.continueButtonfurtherancePopup);

    // Furturance Section in first Stepper
    furtheranceSection = this.elementLocator(LocatorsCi.furtheranceSection);

    // Select Generic Toggle
    selectGenericToggle(textReplaceValue: any) {
        return element(by.xpath(LocatorsCi.genericToggle.value.replace('Replace_Text_Value', textReplaceValue)))
    }

    // Hover Over Generic Icon
    identifyGenericInfoIcon(textReplaceValue) {
        return element(by.xpath(LocatorsCi.genericInfoIcon.value.replace('Replace_Text_Value', textReplaceValue)))
    }



}
