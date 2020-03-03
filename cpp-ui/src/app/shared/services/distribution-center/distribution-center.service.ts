import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { HttpClient, HttpParams } from '@angular/common/http';

import { DistributionCenters, SelectedDistributionCenters } from 'app/contract/distribution-centers/distribution-center.model';
import { apiUrls } from './../app.url';

@Injectable()
export class DistributionCenterService {

  private distributionCenterURL = apiUrls.distributionCenterURL;
  private distributionCentersSaveURL = apiUrls.distributionCenterSaveURL;
  private distributionCentersFetchURL = apiUrls.distributionCentersFetchURL;

  constructor(private http: HttpClient) {}

  public fetchDistributionCenters(): Observable<DistributionCenters[]> {
    return this.http
      .get(this.distributionCenterURL)
      .map((res: DistributionCenters[]) => res);
  }

  saveDistributionCenters(dcList) {
    return this.http.post(this.distributionCentersSaveURL, dcList);
  }

  public fetchSelectedDistributionCenters(cppSeqId): Observable<SelectedDistributionCenters[]> {
    const params = new HttpParams().set('contractPriceProfileSeq', cppSeqId);
    return this.http
      .get(this.distributionCentersFetchURL, {params})
      .map((res: SelectedDistributionCenters[]) => res);
  }




}
