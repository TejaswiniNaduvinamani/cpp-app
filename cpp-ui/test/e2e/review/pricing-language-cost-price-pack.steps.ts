import { defineSupportCode } from 'cucumber';
import { browser, element, by } from 'protractor';

import { CommonTasks, ReviewPageObject } from '../shared';

defineSupportCode(({ When, Then, setDefaultTimeout  }) => {

    const chai = require('chai').use(require('chai-as-promised'));
    const expect = chai.expect;
    const review = new ReviewPageObject();

    Then('Correct pricing language is displayed for Price per pack', { timeout: CommonTasks.VERYLONGWAIT }, async () => {
        await CommonTasks.wait(CommonTasks.MEDIUMWAIT);
        return await expect(review.sellPriceReview.getText()).to.eventually.contain('full Case sell price');
    });

});
