import { TestBed } from '@angular/core/testing';
import { HttpRequest } from '@angular/common/http';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';

import { apiUrls } from './../app.url';


import { AuthorizationService } from './authorization.service';
import { AuthorizationDetails } from './authorization-details.model';

describe('AuthorizationService', () => {

  const fetchAuthorizationDetailsURL = apiUrls.fetchAuthorizationDetailsURL;
  const checkForViewOnlyUserURL = apiUrls.checkForViewOnlyUserURL;

  const AUTH_DETAILS: AuthorizationDetails = new AuthorizationDetails(true, true, true, true, 'draft')

  let service: AuthorizationService;
  let httpMock: HttpTestingController;


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthorizationService]
    })

    service = TestBed.get(AuthorizationService)
    httpMock = TestBed.get(HttpTestingController);
  });

 it('should fetch authorization details', () => {

    let actualResponse = null;
    const cppSeq = '123';

  service.fetchAuthorizationDetails('123', false, 'draft')
   .subscribe(response => {
        actualResponse = response
    })

   const expected = [AUTH_DETAILS];

   httpMock.expectOne((req: HttpRequest<AuthorizationDetails>) => {
    const matchUrl = req.url === fetchAuthorizationDetailsURL;
    const matchParams = req.params.get('contractPriceProfileSeq') === cppSeq
      && req.params.get('isAmendment') === 'false'
      && req.params.get('clmStatus') === 'draft';

    return matchUrl && matchParams;
  }).flush(expected);

  expect(actualResponse).toEqual(expected);
});

it('should check for view only user', () => {
  let actualResponse = null;

  service.checkForViewOnlyUser()
    .subscribe((response) =>
      actualResponse = response);

  const expected = {isUserAuthorizedToViewOnly: false};

  httpMock.expectOne((req: HttpRequest<{isUserAuthorizedToViewOnly: boolean}>) => {
    const matchUrl = req.url === checkForViewOnlyUserURL;
    return matchUrl
  }).flush(expected);

  expect(actualResponse).toEqual(expected);
});

})
