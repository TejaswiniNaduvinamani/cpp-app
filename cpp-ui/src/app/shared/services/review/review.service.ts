import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';


import { apiUrls } from './../app.url';
import { CPPExhibitDownload } from './../../../contract/review/review.model';
import { FurtheranceBaseModel } from 'app/furtherance';
import { ReviewData, ReturnToCLM, PricingExhibitModel } from 'app/contract/review/review.model';



@Injectable()
export class ReviewService {
  private reviewURL = apiUrls.fetchReviewDataURL;
  private downloadURL = apiUrls.createExhibitDocURL;
  private savePricingExhibitURL = apiUrls.savePricingExhibitURL;
  private saveFurtherancePricingDocumentURL = apiUrls.savePricingDocumentForFurtheranceURL;
  private clmURL = apiUrls.fetchClmUrl;

  constructor(private http: HttpClient) { }

  public fetchReviewData(contractPriceProfileId: string): Observable<ReviewData>  {
    let params = new HttpParams();
    params = params.set('contractPriceProfileSeq', contractPriceProfileId);
    return this.http
      .get(this.reviewURL, {params})
      .map((res: ReviewData) => res);
  }

  public savePricingExhibit(pricingExhibitModel: PricingExhibitModel): Observable<PricingExhibitModel>  {
    return this.http
      .post(this.savePricingExhibitURL, pricingExhibitModel)
      .map((res: PricingExhibitModel) => res);
  }

  public savePricingDocumentForFurtherance(furtheranceBaseModel: FurtheranceBaseModel): Observable<FurtheranceBaseModel>  {
    return this.http
      .post(this.saveFurtherancePricingDocumentURL, furtheranceBaseModel)
      .map((res: FurtheranceBaseModel) => res);
  }

  public fetchClmUrl(): Observable<ReturnToCLM> {
    return this.http
      .get(this.clmURL)
      .map((res: ReturnToCLM) => res);
  }

}
