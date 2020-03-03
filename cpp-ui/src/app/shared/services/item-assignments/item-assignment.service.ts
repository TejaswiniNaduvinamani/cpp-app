import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';

import { apiUrls } from './../app.url';
import { FutureItemModel, AssignmentItemDescriptionModel, ItemSaveStatusModel } from './../../../assignment-header';

@Injectable()
export class ItemAssignmentService {

  private fetchAllFutureItemsURL = apiUrls.fetchAllFutureItemsURL;
  private saveItemAssignmentsURL = apiUrls.saveItemAssignmentsURL;
  private deleteItemAssignmentURL = apiUrls.deleteItemAssignmentURL;
  private findItemInformationURL = apiUrls.findItemInformationURL;

    constructor(private http: HttpClient) {}

    public fetchAllFutureItems(contractPriceProfileSeq: string): Observable<FutureItemModel[]> {
      let params = new HttpParams();
      params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
      return this.http
      .get(this.fetchAllFutureItemsURL, {params})
      .map((res: FutureItemModel[]) => res);
    }

    public saveItemAssignment(saveItemAssignment: FutureItemModel): Observable<ItemSaveStatusModel> {
      return this.http
        .post(this.saveItemAssignmentsURL, saveItemAssignment)
        .map((res: ItemSaveStatusModel) => res);
    }

    public fetchItemDescriptionForAssignment(itemId: string, cmgCustomerId: string,
       cmgCustomerTypeCode: string, contractPriceProfileSeq: string ): Observable<AssignmentItemDescriptionModel> {
      let params = new HttpParams();
      params = params.set('itemId', itemId);
      params = params.set('cmgCustomerId', cmgCustomerId);
      params = params.set('cmgCustomerTypeCode', cmgCustomerTypeCode);
      params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
      return this.http
        .get(this.findItemInformationURL, { params })
        .map((res: AssignmentItemDescriptionModel) => res);
    }

    public deleteItemAssignment(contractPriceProfileSeq: string, gfsCustomerId: string,
       gfsCustomerTypeCode: string, itemId: string, futureItemDesc: string): Observable<{}> {
      let params = new HttpParams();
      params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
      params = params.set('gfsCustomerId', gfsCustomerId);
      params = params.set('gfsCustomerTypeCode', gfsCustomerTypeCode);
      params = params.set('itemId', itemId);
      params = params.set('futureItemDesc', futureItemDesc);
      return this.http
        .delete(this.deleteItemAssignmentURL, {params})
        .map((res: {}) => res);
    }
}
