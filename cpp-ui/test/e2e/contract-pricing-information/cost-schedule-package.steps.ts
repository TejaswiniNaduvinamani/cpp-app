import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, ContractPricingInformationPageObject } from '../shared';

defineSupportCode(({ Given, When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const contractPricingInformation = new ContractPricingInformationPageObject();

    Then('Cost Schedule Package is displayed', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await expect(contractPricingInformation.gfsFiscalCalendar.isPresent()).to.eventually.be.true &&
            expect(contractPricingInformation.gregorianCalendar.isPresent()).to.eventually.be.true;
    });

    Then('User able to make a selection on GFS Fiscal Calendar', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        return await contractPricingInformation.gfsFiscalCalendar.click();
    });

    Then('Gregorian Calendar', async () => {
        await contractPricingInformation.gregorianCalendar.click();
    });
});
