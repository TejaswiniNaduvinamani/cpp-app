import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

import { apiUrls } from './../app.url';
import { ContractSearchGridModel } from 'app/contract-search/contract-search.model';

@Injectable()
export class ContractSearchService {

  private searchContractByContractNameURL = apiUrls.searchContractByContractNameURL;
  private searchContractByCustomerURL = apiUrls.searchContractByCustomerURL;
  private searchContractByCppIdURL = apiUrls.searchContractByCppIdURL;

  private isContractSearchPage = new Subject<boolean>();
  isContractSearchPage$ = this.isContractSearchPage.asObservable();

  checkContractSearchPage(data) {
    this.isContractSearchPage.next(data);
  }

  constructor(private http: HttpClient) {}

  public searchContractByContractName(searchContractName: string): Observable<ContractSearchGridModel[]> {
    const params = new HttpParams().set('searchContractName', searchContractName);
    return this.http
      .get(this.searchContractByContractNameURL, {params})
      .map((res: ContractSearchGridModel[]) => res);
  }

  public searchContractByCustomer(gfsCustomerId: string, gfsCustomerTypeCode: number): Observable<ContractSearchGridModel[]> {
    let params = new HttpParams();
    params = params.set('gfsCustomerId', gfsCustomerId);
    params = params.set('gfsCustomerTypeCode', String(gfsCustomerTypeCode));
    return this.http
      .get(this.searchContractByCustomerURL, {params})
      .map((res: ContractSearchGridModel[]) => res);
  }

  public searchContractByCppId(cppId: string): Observable<ContractSearchGridModel[]> {
    const params = new HttpParams().set('cppId', cppId );
    return this.http
      .get(this.searchContractByCppIdURL, {params})
      .map((res: ContractSearchGridModel[]) => res);
  }

}
