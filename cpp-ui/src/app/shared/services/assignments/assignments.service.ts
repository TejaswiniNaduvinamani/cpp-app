import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { apiUrls } from './../app.url';
import { RealCustomerSearchModel, AssignmentMarkupModel,
  SaveAssignmentModel, DeleteRealCustomerModel, SaveAssignmentResponseModel } from 'app/assignment-header';

@Injectable()
export class AssignmentsService {

  private assignmentFetchURL = apiUrls.fetchMarkupsAssignmentURL;
  private customerTypeURL = apiUrls.fetchCustomerTypes;
  private findCustomerIdURL = apiUrls.findCustomer;
  private saveAssignmentsURL = apiUrls.saveAssignmentsURL;
  private deleteRealCustomerURL = apiUrls.deleteRealCustomer;
  private activatePricingURL = apiUrls.activatePricingURL;
  private enableActivatePricingURL = apiUrls.enableActivatePricingURL;

  constructor(private http: HttpClient) { }

  public fetchAssignmentMarkups(cppSeqId: string): Observable<AssignmentMarkupModel[]> {
    const params = new HttpParams().set('contractPriceProfileSeq', cppSeqId);
    return this.http
      .get(this.assignmentFetchURL, {params})
      .map((res: AssignmentMarkupModel[]) => res);
  }

  public fetchCustomerType(): Observable<string[]> {
    return this.http
      .get(this.customerTypeURL)
      .map((res: string[]) => res);
  }

  public findRealCustomer(customerId: string, customerType: string, contractPriceProfileSeq: string,
    cmgCustomerId: string, cmgCustomerType: string ): Observable<RealCustomerSearchModel> {
    let params = new HttpParams();
    params = params.set('gfsCustomerId', customerId);
    params = params.set('gfsCustomerType', customerType);
    params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
    params = params.set('cmgCustomerId', cmgCustomerId);
    params = params.set('cmgCustomerType', cmgCustomerType);
    return this.http
      .get(this.findCustomerIdURL, {params})
      .map((res: RealCustomerSearchModel) => res)
  }

  public saveAssignment(saveAssignment ): Observable<SaveAssignmentResponseModel> {
    return this.http
      .post(this.saveAssignmentsURL, saveAssignment)
      .map((res: SaveAssignmentResponseModel) => res);
  }

  public deleteRealCustomer(customerId: string, customerType: string, contractPriceProfileSeq: string,
     gfsCustomerId: string, gfsCustomerTypeId: string): Observable<DeleteRealCustomerModel> {
    let params = new HttpParams();
    params = params.set('realCustomerId', customerId);
    params = params.set('realCustomerType', customerType);
    params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
    params = params.set('cmgCustomerId', gfsCustomerId);
    params = params.set('cmgCustomerType', gfsCustomerTypeId);
    return this.http
      .delete(this.deleteRealCustomerURL, {params})
      .map((res: DeleteRealCustomerModel) => res);
  }

    public activatePricing(contractPriceProfileSeq: string, isAmendment: boolean, clmStatus: string): Observable<{}>  {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
    params = params.set('isAmendment', String(isAmendment));
    params = params.set('clmContractStatus', clmStatus);
    return this.http
      .get(this.activatePricingURL, {params})
      .map((res: {}) => res);
  }

  public fetchActivatePricingButtonState(contractPriceProfileSeq: string):
  Observable<{enableActivatePricingButton: boolean}> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileSeq);
    return this.http
      .get(this.enableActivatePricingURL, {params})
      .map((res: {enableActivatePricingButton: boolean}) => res);
  }

}
