import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, HeaderPageObject, AssignmentsPageObject } from '../shared';

defineSupportCode(({ When, Then }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const assignments = new AssignmentsPageObject();
    const header = new HeaderPageObject();
    const EC = protractor.ExpectedConditions;

    When('When User clicks Assignments tab', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(header.assignmentTab), CommonTasks.LONGWAIT);
        return await header.assignmentTab.click();
      });

    Then('Customer Type, Customer Name, Customer Id are displayed on the Page', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await browser.wait(EC.presenceOf(assignments.assignments1stCMGName), CommonTasks.LONGWAIT);
        await expect(assignments.assignments1stCMGName.isPresent()).to.eventually.be.true;
        return await expect(assignments.assignments1stCMGTypeAndId.isPresent()).to.eventually.be.true;
    });

    Then('Correct markup data is displayed', async () => {
        return await element.all(by.xpath(assignments.assignments1stCMGAllMarkupValues)).each(function (item) {
            expect(item.getText()).to.eventually.equal('44%');
        });
    });

});
