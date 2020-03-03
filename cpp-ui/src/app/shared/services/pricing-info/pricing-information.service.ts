import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs/Rx';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

import { apiUrls } from './../app.url';
import { ContractInformation} from './../../../contract/pricing-information/pricing-information.model';
import { PricingInformation, ClmContractDetails } from './../../../contract';

@Injectable()
export class PricingInformationService {

  private fetchContractPriceProfileInfoURL = apiUrls.fetchContractPriceProfileInfoURL;
  private fetchPricingInformationURL = apiUrls.fetchPricingInformationURL;
  private savePricingInfoURL = apiUrls.savePricingInformationURL;
  private deletePricingExhibitURL = apiUrls.deletePricingExhibitURL;

  private ClmContractDetails = new Subject<ClmContractDetails>();
  ClmContractDetails$ = this.ClmContractDetails.asObservable();

  private pricingInformationStatus = new Subject<boolean>();
  pricingInformationStatus$ = this.pricingInformationStatus.asObservable();

  constructor(private http: HttpClient) { }

  public savePricingInformation(pricingInformation: PricingInformation): Observable<PricingInformation> {
    return this.http
      .post(this.savePricingInfoURL, pricingInformation)
      .map((res: PricingInformation) => res);
  }

  public fetchPricingInformation(contractPriceProfileSeq: string, agreementId: string,
    contractStatus: string): Observable<PricingInformation> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
    params = params.set('agreementId', agreementId);
    params = params.set('contractStatus', contractStatus);
    return this.http
      .get(this.fetchPricingInformationURL, { params })
      .map((res: PricingInformation) => res);
  }

  public fetchContractPriceProfileInfo(agreementId: string, contractType: string): Observable<ClmContractDetails> {
    let params = new HttpParams();
    params = params.set('agreementId', agreementId);
    params = params.set('contractType', contractType);
    return this.http
      .get(this.fetchContractPriceProfileInfoURL, {params})
      .map((res: ClmContractDetails) => res);
  }

  public setClmContractDetails(data) {
    this.ClmContractDetails.next(data);
  }

  public getClmContractDetails(): Observable<ClmContractDetails> {
    return this.ClmContractDetails;
  }

  public deletePricingExhibit(agreementId: string): Observable<{}> {
    let params = new HttpParams();
    params = params.set('agreementId', agreementId);

    return this.http.delete(this.deletePricingExhibitURL, {params})
    .map((res: {}) => res);

  }

  public setPricingInformationStatus(data) {
    this.pricingInformationStatus.next(data);
  }

  public getPricingInformationStatus(): Observable<boolean> {
    return this.pricingInformationStatus;
  }
}
