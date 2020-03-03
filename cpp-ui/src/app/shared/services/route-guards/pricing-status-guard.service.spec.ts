import { async } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { PricingStatusGuard } from 'app/shared';


describe('PricingStatusGuard', () => {

  let service: PricingStatusGuard;

  // do not remove double quotes in SessionStorageInterface. Setting of session requires double quotes while parsing the string.
  const CONTRACT_DETAILS = {
    "agreementId": '75490bfc-289d-40d3-b644-ba095c2e1cea',
    "cenddate": new Date('12/12/2020'),
    "cname": 'TEST CONTRACT',
    "contractStatus": 'draft',
    "contractType": 'ICMDistributionAgreementRegional',
    "cppid": 12345,
    "cppseqid": 42915,
    "cstdate": new Date('12/12/2019'),
    "ctype": 'ICMDistributionAgreementRegional',
    "isAmendment": false,
    "isFurtheranceMode": true,
    "isPricingExhibitAttached": false,
    "penddate": new Date('12/12/2020'),
    "pstdate": new Date('12/12/2019'),
    "versionNumber": 1,
    "furtherancedate": '2019-07-23T00:00:00.000Z',
    "isPricingSaved": true
   }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PricingStatusGuard]
    });
    service = TestBed.get(PricingStatusGuard);
  }));

  beforeEach(() => {
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should return true if pricing information is saved', () => {
    // GIVEN
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));

    // THEN
    expect(service.canActivate()).toBe(true);
  });

});
