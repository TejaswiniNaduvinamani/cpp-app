import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';

import { apiUrls } from './../app.url';
import { CostModelGridDetails, CostModelDTO } from 'app/cost-model';

@Injectable()
export class CostModelService {

  private fetchCostGridURL = apiUrls.fetchCostGridURL;
  private fetchCostModelListURL = apiUrls.fetchCostModelListURL;
  private saveCostModelGridURL = apiUrls.saveCostModelGridURL;

  constructor(private http: HttpClient) { }

  public fetchCostGridDetails(contractPriceProfileId: string): Observable<CostModelGridDetails[]> {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    return this.http
      .get(this.fetchCostGridURL, { params })
      .map((res: CostModelGridDetails[]) => res);
  }

  public fetchCostModelList(): Observable<CostModelDTO[]> {
    return this.http
      .get(this.fetchCostModelListURL)
      .map((res: CostModelDTO[]) => res);
  }

  public saveCostModel(costModelGridDetails: CostModelGridDetails[]) {
    return this.http
      .post(this.saveCostModelGridURL, costModelGridDetails)
      .map((res: CostModelGridDetails[]) => res);
  }
}
