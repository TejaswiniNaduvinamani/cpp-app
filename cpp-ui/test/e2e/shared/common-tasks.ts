import { browser, element, by } from 'protractor';

// Choose Identification Type
export enum IdentificationType {
    Id,
    Name,
    Css,
    Xpath,
    LinkText,
    PartialLinkText,
    ClassName,
    ButtonText,
    Model
}

// Generic Elements
export const LocatorGeneric = {
    genericNextButton: {
        type: IdentificationType[IdentificationType.Xpath],
        value: '//button[@type="submit"]'
    }
}

// Common Functions
export class CommonTasks {

    // Constant Wait Time Values
    static TINYWAIT = 2000;
    static SMALLWAIT = 3000;
    static MEDIUMWAIT = 5000;
    static LONGWAIT = 10000;
    static VERYLONGWAIT = 30000;
    static EXTENSIVEWAIT = 180000;

    // Generic Next Button
    genericNextButton = this.elementLocator(LocatorGeneric.genericNextButton);

    // Comapre Two Arrays
    static compareArray(array1, array2) {
        if (array1.length !== array2.length) {
            return false;
        }
        return array1.sort().join() === array2.sort().join();
    }

    // Add Wait
    static wait(number: any) {
        return browser.sleep(number);
    }

    // Select Value from Dropdown List
    static async selectValueFromDropdown(elem: any, valueAtt: string) {
        return await elem.$('[value="' + valueAtt + '"]').click();
    }

    // Getting Current System Date in Required Format
    static getCurrentSystemDate(seperator: any) {
        const currentTime = new Date();
        const requiredTime = new Date(currentTime.getTime());
        const mm = (requiredTime.getMonth() + 1) < 10 ? '0'
        + (requiredTime.getMonth() + 1).toString() : (requiredTime.getMonth() + 1).toString()
        const dd = requiredTime.getDate() < 10 ? '0' + requiredTime.getDate().toString() : requiredTime.getDate().toString();
        return mm + seperator + dd + seperator + requiredTime.getFullYear().toString();
    }

    // Identify Web Elements
    elementLocator(obj: any) {

        switch (obj.type) {
            case IdentificationType[IdentificationType.Xpath]:
                return element(by.xpath(obj.value));

            case IdentificationType[IdentificationType.Css]:
                return element(by.css(obj.value));

            case IdentificationType[IdentificationType.Id]:
                return element(by.id(obj.value));

            case IdentificationType[IdentificationType.Name]:
                return element(by.name(obj.value));

            case IdentificationType[IdentificationType.LinkText]:
                return element(by.linkText(obj.value));

            case IdentificationType[IdentificationType.PartialLinkText]:
                return element(by.partialLinkText(obj.value));

            case IdentificationType[IdentificationType.ClassName]:
                return element(by.className(obj.value));

            case IdentificationType[IdentificationType.ButtonText]:
                return element(by.buttonText(obj.value));

            case IdentificationType[IdentificationType.Model]:
                return element(by.model(obj.value));

            default:
                return element(by.xpath(obj.value));
        }
    }

}
