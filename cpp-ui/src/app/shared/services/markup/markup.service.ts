import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import * as moment from 'moment';

import { MarkupGridDetails,  MarkupIndicatorsModel, RenameMarkupModel, ItemLevelMarkupModel,
   ItemDescriptionModel, SubgroupMarkupModel, SubgroupSearchModel } from './../../../contract';
import { apiUrls } from './../app.url';

@Injectable()
export class MarkupService {

  private fetchMarkupGridURL = apiUrls.fetchMarkupGridURL;
  private saveMarkupGridURL = apiUrls.saveMarkupGridURL;
  private addExceptionURL = apiUrls.addExceptionURL;
  private createDefaultItemLevelMarkupURL = apiUrls.createDefaultItemLevelMarkupURL;
  private saveMarkupIndicatorsURL = apiUrls.saveMarkupIndicatorsURL;
  private renameMarkupExceptionURL = apiUrls.renameMarkupExceptionURL;
  private deleteMarkupExceptionURL = apiUrls.deleteMarkupExceptionURL;
  private findItemInformationURL = apiUrls.findItemInformationURL;
  private deleteItemLevelMarkupURL = apiUrls.deleteItemLevelMarkupURL;
  private fetchMarkupIndicatorsURL = apiUrls.fetchMarkupIndicatorsURL;
  private createDefaultSubgroupMarkupURL = apiUrls.createDefaultSubgroupMarkupURL;
  private findSubgroupInformationURL = apiUrls.findSubgroupInformationURL;
  private deleteSubgroupMarkupURL = apiUrls.deleteSubgroupMarkupURL;

  constructor(private http: HttpClient) { }

  public fetchMarkupGridDetails(contractPriceProfileId: string, effectiveDate: Date, expirationDate: Date,
    contractName: string): Observable<MarkupGridDetails[]> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    params = params.set('pricingEffectiveDate', moment.utc(effectiveDate).format('MM/DD/YYYY'));
    params = params.set('pricingExpirationDate', moment.utc(expirationDate).format('MM/DD/YYYY'));
    params = params.set('contractName', contractName);
    return this.http
      .get(this.fetchMarkupGridURL, { params })
      .map((res: MarkupGridDetails[]) => res);
  }

  public saveMarkupGridDetails(saveMarkupDetails: MarkupGridDetails): Observable<MarkupGridDetails> {
    return this.http
      .post(this.saveMarkupGridURL, saveMarkupDetails)
      .map((res: MarkupGridDetails) => res);
  }

  public addExceptionDetails(contractPriceProfileId: string, exceptionName: string, effectiveDate: Date,
    expirationDate: Date): Observable<MarkupGridDetails> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    params = params.set('exceptionName', exceptionName);
    params = params.set('pricingEffectiveDate', moment.utc(effectiveDate).format('MM/DD/YYYY'));
    params = params.set('pricingExpirationDate', moment.utc(expirationDate).format('MM/DD/YYYY'));
    return this.http
      .get(this.addExceptionURL, { params })
      .map((res: MarkupGridDetails) => res);
  }

  public saveMarkupIndicators(markupIndicatorsDetails: MarkupIndicatorsModel): Observable<MarkupIndicatorsModel> {
    return this.http
      .post(this.saveMarkupIndicatorsURL, markupIndicatorsDetails)
      .map((res: MarkupIndicatorsModel) => res);
  }

  public renameMarkupException(renameMarkupModel: RenameMarkupModel): Observable<RenameMarkupModel> {
    return this.http
      .put(this.renameMarkupExceptionURL, renameMarkupModel)
      .map((res: RenameMarkupModel) => res);
  }

  public deleteMarkupException(contractPriceProfileId: string, gfsCustomerId: string, markupName: string): Observable<any> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    params = params.set('gfsCustomerId', gfsCustomerId);
    params = params.set('markupName', markupName);
    return this.http
      .delete(this.deleteMarkupExceptionURL, { params })
      .map((res: any) => res);
  }

  public fetchItemLevelDefaults(contractPriceProfileId: string, effectiveDate: Date,
    expirationDate: Date): Observable<ItemLevelMarkupModel> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    params = params.set('pricingEffectiveDate', moment.utc(effectiveDate).format('MM/DD/YYYY'));
    params = params.set('pricingExpirationDate', moment.utc(expirationDate).format('MM/DD/YYYY'));
    return this.http
      .get(this.createDefaultItemLevelMarkupURL, { params })
      .map((res: ItemLevelMarkupModel) => res);
  }

  public fetchDefaultSubgroupMarkup(expirationDate: Date, effectiveDate: Date): Observable<SubgroupMarkupModel> {
    let params = new HttpParams();
    params = params.set('pricingExpirationDate', moment.utc(expirationDate).format('MM/DD/YYYY'));
    params = params.set('pricingEffectiveDate', moment.utc(effectiveDate).format('MM/DD/YYYY'));
    return this.http
    .get(this.createDefaultSubgroupMarkupURL, { params })
    .map((res: SubgroupMarkupModel) => res);
  }

  public fetchItemDescription(itemId: string, cmgCustomerId: string,
    cmgCustomerTypeCode: string, contractPriceProfileSeq: string ): Observable<ItemDescriptionModel> {
    let params = new HttpParams();
    params = params.set('itemId', itemId);
    params = params.set('cmgCustomerId', cmgCustomerId);
    params = params.set('cmgCustomerTypeCode', cmgCustomerTypeCode);
    params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
    return this.http
      .get(this.findItemInformationURL, { params })
      .map((res: ItemDescriptionModel) => res);
  }

  public fetchSubgroupDescription(subgroupId: string, gfsCustomerId: string,
    gfsCustomerType: string, contractPriceProfileSeq: string): Observable<SubgroupSearchModel> {
      let params = new HttpParams();
      params = params.set('subgroupId', subgroupId);
      params = params.set('gfsCustomerId', gfsCustomerId);
      params = params.set('gfsCustomerType', gfsCustomerType);
      params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
      return this.http
        .get(this.findSubgroupInformationURL, { params })
        .map((res: SubgroupSearchModel) => res);
    }

  public deleteItemLevelMarkup(contractPriceProfileId: string, gfsCustomerId: string,
    gfsCustomerType: string, itemId: string, itemDesc: string): Observable<{}> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    params = params.set('gfsCustomerId', gfsCustomerId);
    params = params.set('gfsCustomerType', gfsCustomerType);
    params = params.set('itemId', itemId);
    params = params.set('itemDesc', itemDesc);
    return this.http
      .delete(this.deleteItemLevelMarkupURL, { params })
      .map((res: {}) => res);
  }

  public deleteSubgroupMarkup(contractPriceProfileId: string, gfsCustomerId: string,
    gfsCustomerType: string, subgroupId: string): Observable<{}> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    params = params.set('gfsCustomerId', gfsCustomerId);
    params = params.set('gfsCustomerType', gfsCustomerType);
    params = params.set('subgroupId', subgroupId);
    return this.http
      .delete(this.deleteSubgroupMarkupURL, { params })
      .map((res: {}) => res);
  }

  public fetchMarkupIndicators(contractPriceProfileId: string): Observable<MarkupIndicatorsModel> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    return this.http
      .get(this.fetchMarkupIndicatorsURL, { params })
      .map((res: MarkupIndicatorsModel) => res);
  }
}
