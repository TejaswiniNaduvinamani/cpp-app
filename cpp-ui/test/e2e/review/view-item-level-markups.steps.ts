import { defineSupportCode } from 'cucumber';
import { browser, element, by, protractor } from 'protractor';

import { CommonTasks, ReviewPageObject } from '../shared';

defineSupportCode(({ Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const review = new ReviewPageObject();

    Then('Item-level markups are displayed correctly on Review screen', {timeout: CommonTasks.LONGWAIT}, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(review.itemLevelRow.isPresent()).to.eventually.be.true;
    });

});
