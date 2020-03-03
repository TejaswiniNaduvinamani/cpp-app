import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs/Rx';
import { HttpClient, HttpParams } from '@angular/common/http';

import { apiUrls } from './../app.url';
import { AuthorizationDetails } from './authorization-details.model';

@Injectable()
export class AuthorizationService {

  private fetchAuthorizationDetailsURL = apiUrls.fetchAuthorizationDetailsURL;
  private checkForViewOnlyUserURL = apiUrls.checkForViewOnlyUserURL;

  private authorizationDetails = new Subject<AuthorizationDetails>();
  authorizationDetails$ = this.authorizationDetails.asObservable();

  constructor(private http: HttpClient) { }

  public fetchAuthorizationDetails(cppSeq: string, isAmendment: boolean, contractStatus: string): Observable<AuthorizationDetails> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', cppSeq);
    params = params.set('isAmendment', String(isAmendment));
    params = params.set('clmStatus', contractStatus);
    return this.http
      .get(this.fetchAuthorizationDetailsURL, { params })
      .map((res: AuthorizationDetails) => res);
  }

  public setAuthorizationDetails(data) {
    this.authorizationDetails.next(data);
  }

  public getAuthorizationDetails(): Observable<AuthorizationDetails> {
    return this.authorizationDetails;
  }

  public checkForViewOnlyUser(): Observable<{isUserAuthorizedToViewOnly: boolean}> {
    return this.http
    .get(this.checkForViewOnlyUserURL)
    .map((res: {isUserAuthorizedToViewOnly: boolean}) => res);
  }

}
