import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, MarkupPageObject, AssignmentsPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const markup = new MarkupPageObject();
    const assignments = new AssignmentsPageObject();
    const EC = protractor.ExpectedConditions;

    Then('Error message is displayed', {timeout: CommonTasks.VERYLONGWAIT}, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(assignments.firstExceptionGrid1stRowErrorMessage.isPresent()).to.eventually.be.true;
    });

    Then('Only a single real Customer can be assigned to default grid', {timeout: CommonTasks.VERYLONGWAIT}, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(assignments.defaultGridCustomerName.getText()).to.eventually.equal('ABC') && expect(
            assignments.defaultGridAddAnotherButton.isPresent()).to.eventually.be.false;
    });

    Then('User can add a single real Customer to an exception CMG grid', {timeout: CommonTasks.VERYLONGWAIT}, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await assignments.firstexceptionGridExpandCollapseArrow.click();
        return await expect(assignments.firstExceptionGrid1stRowCustomerName.getText()).to.eventually.contain('ABC');
    });

    Then('User can add another customer to the same grid', {timeout: CommonTasks.VERYLONGWAIT}, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await assignments.firstExceptionGridAddAnotherButton.click();
        return await expect(assignments.firstExceptionGrid4thRow.isPresent()).to.eventually.be.true;
    });

    Then('The user can delete a Customer from the exception grid', {timeout: CommonTasks.VERYLONGWAIT}, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        await assignments.firstExceptionGrid4thRowDeleteButton.click();
        return await expect(element.all(by.xpath(assignments.firstExceptionGridAllRows)).count()).to.eventually.equal(3);
    });

});
