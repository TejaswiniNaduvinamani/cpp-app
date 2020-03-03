import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Params } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import * as moment from 'moment';

import { apiUrls } from './../app.url';
import { SplitCaseFees, SplitCaseList } from './../../../contract';

@Injectable()
export class SplitCaseFeeService {

  private fetchSplitcaseFeeURL = apiUrls.fetchSplitcaseFeeURL;
  private splitcaseFeeSaveURL = apiUrls.splitCaseSaveURL;


  constructor(private http: HttpClient) { }

  public fetchProductTypes(cppId: string, effectiveDate: Date, expirationDate: Date): Observable<any> {
    let params = new HttpParams().set('contractPriceProfileId', cppId);
    params = params.set('pricingEffectiveDate', moment.utc(effectiveDate).format('MM/DD/YYYY'));
    params = params.set('pricingExpirationDate', moment.utc(expirationDate).format('MM/DD/YYYY')) ;
    return this.http
    .get(this.fetchSplitcaseFeeURL, {params})
    .map(res => res);
}

 public saveSplitCaseFee(splitCaseList): Observable<SplitCaseList> {
   return this.http.post(this.splitcaseFeeSaveURL, splitCaseList)
   .map((res: SplitCaseList) => res);
 }
}
