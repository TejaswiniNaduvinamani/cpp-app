import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';

import { apiUrls } from './../app.url';
import { FurtheranceBaseModel, FurtheranceInformationModel, ValidateFurtheranceModel } from 'app/furtherance';
import { FutureItemModel, ItemSaveStatusModel } from 'app/assignment-header';
import { ItemDetailsForFurtheranceModel } from './../../../contract/';
import { SaveFurtheranceSplitCaseModel, MarkupGridDetails } from 'app/contract';

@Injectable()
export class FurtheranceService {

  private activatePricingForFurtheranceURL = apiUrls.activatePricingForFurtheranceURL;
  private createNewFurtheranceURL = apiUrls.createNewFurtheranceURL;
  private enableActivatePricingForFurtheranceURL = apiUrls.enableActivatePricingForFurtheranceURL;
  private fetchInProgressFurtheranceInfoURL = apiUrls.fetchInProgressFurtheranceInfoURL;
  private fetchMappedItemsForFurtheranceURL = apiUrls.fetchMappedItemsForFurtheranceURL;
  private deleteItemLevelMarkupFurtheranceURL = apiUrls.deleteItemLevelMarkupFurtheranceURL;
  private fetchFurtheranceInfoURL = apiUrls.fetchFurtheranceInfoURL;
  private saveFurtheranceSaveURL = apiUrls.saveFurtheranceInformationURL;
  private saveFurtheranceMarkupGridURL = apiUrls.saveFurtheranceMarkupGridURL;
  private validateHasInProgressFurtheranceURL = apiUrls.validateHasInProgressFurtheranceURL;
  private saveFurtheranceSplitCaseURL = apiUrls.saveFurtheranceSplitCaseURL;
  private saveMappedItemsForFurtheranceURL = apiUrls.saveMappedItemsForFurtheranceURL;
  private deleteMappedItemForFurtheranceURL = apiUrls.deleteMappedItemForFurtheranceURL;
  private canEditFurtheranceURL = apiUrls.canEditFurtherance;

  constructor(private http: HttpClient) { }

  public validateHasInProgressFurtherance(parentAgreementId: string): Observable<ValidateFurtheranceModel> {
    let params = new HttpParams();
    params = params.set('parentAgreementId', parentAgreementId);
    return this.http
      .get(this.validateHasInProgressFurtheranceURL, { params })
      .map((res: ValidateFurtheranceModel) => res);
  }

  public createNewFurtherance(parentAgreementId: string, contractType: string): Observable<FurtheranceBaseModel> {
    let params = new HttpParams();
    params = params.set('parentAgreementId', parentAgreementId);
    params = params.set('contractType', contractType);
    return this.http
      .get(this.createNewFurtheranceURL, { params })
      .map((res: FurtheranceBaseModel) => res);
  }

  public fetchInProgressFurtheranceInfo(parentAgreementId: string): Observable<FurtheranceBaseModel> {
    let params = new HttpParams();
    params = params.set('parentAgreementId', parentAgreementId);
    return this.http
      .get(this.fetchInProgressFurtheranceInfoURL, { params })
      .map((res: FurtheranceBaseModel) => res);
  }

  public deleteItemLevelMarkupFurtherance(contractPriceProfileId: string, cppFurtheranceSeq: number,
    gfsCustomerId: string, gfsCustomerTypeCode: string, itemId: string, itemDesc: string): Observable<{}> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    params = params.set('cppFurtheranceSeq', String(cppFurtheranceSeq));
    params = params.set('gfsCustomerId', gfsCustomerId);
    params = params.set('gfsCustomerTypeCode', gfsCustomerTypeCode);
    params = params.set('itemId', itemId);
    params = params.set('itemDesc', itemDesc);
    return this.http
      .delete(this.deleteItemLevelMarkupFurtheranceURL, { params })
      .map((res: {}) => res);
  };

  public fetchFurtheranceInfo(parentAgreementId: string, cppFurtheranceSeq: number): Observable<FurtheranceInformationModel> {
    let params = new HttpParams();
    params = params.set('parentAgreementId', parentAgreementId);
    params = params.set('cppFurtheranceSeq', String(cppFurtheranceSeq));
    return this.http
      .get(this.fetchFurtheranceInfoURL, {params})
      .map((res: FurtheranceInformationModel) => res);
  }

  public saveFurtheranceInfo(furtheranceInformation: FurtheranceInformationModel): Observable<FurtheranceInformationModel> {
    return this.http.post(this.saveFurtheranceSaveURL, furtheranceInformation)
      .map((res: FurtheranceInformationModel) => res);
  }

  public saveMarkupGridForFurtherance(saveMarkupDetails: MarkupGridDetails): Observable<MarkupGridDetails> {
    return this.http
      .post(this.saveFurtheranceMarkupGridURL, saveMarkupDetails)
      .map((res: MarkupGridDetails) => res);
  }

  public saveSplitCaseFee(saveSplitcaseList: SaveFurtheranceSplitCaseModel): Observable<SaveFurtheranceSplitCaseModel> {
    return this.http.post(this.saveFurtheranceSplitCaseURL, saveSplitcaseList)
      .map((res: SaveFurtheranceSplitCaseModel) => res);
  }

  public fetchMappedItemsForFurtherance(contractPriceProfileSeq: number, gfsCustomerId: string,
    gfsCustomerTypeCode: number, itemDesc: string): Observable<FutureItemModel> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', String(contractPriceProfileSeq));
    params = params.set('gfsCustomerId', gfsCustomerId);
    params = params.set('gfsCustomerTypeCode', String(gfsCustomerTypeCode));
    params = params.set('itemDesc', itemDesc);
    return this.http
      .get(this.fetchMappedItemsForFurtheranceURL, {params})
      .map((res: FutureItemModel) => res);
  }

  public enableActivatePricingForFurtherance(cppFurtheranceSeq: number,
      clmContractStatus: string): Observable<{enableActivatePricingButton}> {
    let params = new HttpParams();
    params = params.set('cppFurtheranceSeq', String(cppFurtheranceSeq));
    params = params.set('clmContractStatus', clmContractStatus);
    return this.http
      .get(this.enableActivatePricingForFurtheranceURL, {params})
      .map((res: {enableActivatePricingButton}) => res);
  }

  public activatePricingForFurtherance(cppFurtheranceSeq: number, clmContractStatus: string): Observable<{}> {
    let params = new HttpParams();
    params = params.set('cppFurtheranceSeq', String(cppFurtheranceSeq));
    params = params.set('clmContractStatus', clmContractStatus);
    return this.http
      .get(this.activatePricingForFurtheranceURL, {params})
      .map((res: {}) => res);
  }

  public saveMappedItemsForFurtherance(itemDetails: ItemDetailsForFurtheranceModel): Observable<ItemSaveStatusModel> {
    return this.http
    .post(this.saveMappedItemsForFurtheranceURL, itemDetails)
    .map((res: ItemSaveStatusModel) => res);
  }

  public deleteItemAssignmentForFurtherance(contractPriceProfileSeq: string, cppFurtheranceSeq: number, gfsCustomerId: string,
    gfsCustomerTypeCode: string, itemId: string, futureItemDesc: string): Observable<{}> {
   let params = new HttpParams();
   params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
   params = params.set('cppFurtheranceSeq', String(cppFurtheranceSeq));
   params = params.set('gfsCustomerId', gfsCustomerId);
   params = params.set('gfsCustomerTypeCode', gfsCustomerTypeCode);
   params = params.set('itemId', itemId);
   params = params.set('itemDesc', futureItemDesc);
   return this.http
     .delete(this.deleteMappedItemForFurtheranceURL, {params})
     .map((res: {}) => res);
 }

 public canEditFurtherance(furtheranceSeq: number): Observable<{canEditFurtherance: boolean}> {
  let params = new HttpParams();
  params = params.set('furtheranceSeq', String(furtheranceSeq));
  return this.http.get(this.canEditFurtheranceURL, {params})
  .map((res: {canEditFurtherance: boolean}) => res);
 }
}
