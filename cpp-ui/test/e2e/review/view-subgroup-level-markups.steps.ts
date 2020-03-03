import { defineSupportCode } from 'cucumber';

import { CommonTasks, ReviewPageObject } from '../shared';

defineSupportCode(({ Then, setDefaultTimeout }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const review = new ReviewPageObject();

    Then('Subgroup-level markups are displayed correctly on Review screen', {timeout: CommonTasks.LONGWAIT}, async () => {
        await CommonTasks.wait(CommonTasks.TINYWAIT);
        return await expect(review.subGroupItemRow.isPresent()).to.eventually.be.true;
    });

});
