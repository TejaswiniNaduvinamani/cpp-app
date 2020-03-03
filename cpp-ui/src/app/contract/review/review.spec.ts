import { ComponentFixture, TestBed  } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { apiUrls, FurtheranceService } from 'app/shared';
import { ActivatedRouteStub } from './../../../../test/unit-testing/mock/activated-route-stub';
import { AuthorizationService, AuthorizationDetails } from '../../shared';
import { ContractPricingDTO, MarkupGridModel, MarkupGridDTO, MarkupReviewDTO, SplitCaseFeesModel, ReviewData } from './review.model';
import { ReviewComponent } from './review.component';
import { ReturnToCLM } from 'app/contract/review/review.model';
import { ReviewService } from './../../shared/services/review/review.service';
import { RouterStub } from '../../../../test/unit-testing/mock/router-stub';
import { SessionStorageInterface } from 'app/contract';
import { StepperService } from './../../shared/services/stepper/stepper.service';

describe('Review Component ', () => {

  let component: ReviewComponent;
  let fixture: ComponentFixture<ReviewComponent>;
  let reviewService: ReviewService;
  let furtheranceService: FurtheranceService;
  let router: Router;
  let route: ActivatedRoute;

  const RETURN_TO_CLM: ReturnToCLM = new ReturnToCLM('https://gfsdev.icertis.com/Agreement/Details?entityName=');

  // do not remove double quotes in SessionStorageInterface. Setting of session requires double quotes while parsing the string.
  const CONTRACT_DETAILS: SessionStorageInterface = {
    "agreementId": '75490bfc-289d-40d3-b644-ba095c2e1cea',
    "cenddate": new Date('12/12/2020'),
    "cname": 'TEST CONTRACT',
    "contractStatus": 'draft',
    "contractType": 'ICMDistributionAgreementRegional',
    "cppid": 12345,
    "cppseqid": 42915,
    "cstdate": new Date('12/12/2019'),
    "ctype": 'ICMDistributionAgreementRegional',
    "isAmendment": false,
    "isFurtheranceMode": true,
    "isPricingExhibitAttached": true,
    "penddate": new Date('12/12/2020'),
    "pstdate": new Date('12/12/2019'),
    "versionNumber": 1
   }

  beforeEach(() => {


    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReviewComponent],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        StepperService,
        ReviewService,
        FurtheranceService,
        AuthorizationService]
        }).overrideTemplate(ReviewComponent, '')

    fixture = TestBed.createComponent(ReviewComponent);
    component = fixture.componentInstance;
    reviewService = TestBed.get(ReviewService);
    furtheranceService = TestBed.get(FurtheranceService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create the review component', () => {
    // THEN
    expect(component).toBeTruthy();
  })

  it('should increment character set and check smaller functions', () => {
    // WHEN
    component.incrementChar('C')
    component.getContractType()

    // THEN
    expect(component.incrementChar('C')).toBe('D')
  })

  it('should get furtherance mode as editable', () => {
    // GIVEN
    component.cppFurtheranceSeq = 123;
    spyOn(furtheranceService, 'canEditFurtherance').and.returnValue(Observable.of({canEditFurtherance: true}));

    // WHEN
    component.getContractType()

    // THEN
    expect(component.canEditFurtherance).toBeTruthy();
    expect(furtheranceService.canEditFurtherance).toHaveBeenCalledWith(component.cppFurtheranceSeq);
  })

  it('should download exhibit for furtherance', () => {
    // GIVEN
    component.isFurtheranceMode = true;

    // WHEN
    component.downloadExhibitDoc();

    // THEN
    expect(component.downloadExhibitDoc()).toContain(apiUrls.createFurtheranceDocumentURL);

  })

  it('should download exhibit for contract', () => {
    // GIVEN
    component.isFurtheranceMode = false;

    // WHEN
    component.downloadExhibitDoc();

    // THEN
    expect(component.downloadExhibitDoc()).toContain(apiUrls.createExhibitDocURL);

  })

  it('should navigate to different steppers', () => {
    // GIVEN
    fixture.detectChanges();
    let spy = spyOn(router, 'navigate');

    // WHEN
    component.navigateToContractPricing();
    component.navigateToDistributionCenter();
    component.navigateToMarkup();
    component.navigateToSplitCase();

    // THEN
    expect(spy).toHaveBeenCalledWith((['/pricinginformation']), {relativeTo: route});
    expect(spy).toHaveBeenCalledWith((['/distributioncenters']), {relativeTo: route});
    expect(spy).toHaveBeenCalledWith((['/markup']), {relativeTo: route});
    expect(spy).toHaveBeenCalledWith((['/splitcasefee']), {relativeTo: route});
  });

  it('should return to clm', () => {
    // GIVEN
    spyOn(reviewService, 'fetchClmUrl').and.returnValue(Observable.of(RETURN_TO_CLM));
    spyOn(window, 'open');
    spyOn(sessionStorage, 'clear');

    // WHEN
    component.returnToCLM();

    // THEN
    expect(reviewService.fetchClmUrl).toHaveBeenCalled();
    expect(sessionStorage.clear).toHaveBeenCalled();
    expect(component.showSpinner).toBeTruthy();
    expect(window.open).toHaveBeenCalled();
  });

  it('should invoke savePricingExhibit', () => {
    // GIVEN
    component.contractPriceProfileId = 123;
    component.agreementId = '234219f3-9e86-405c-9867-bead4977855b';
    component.contractType = 'ICMDistributionAgreementRegional';
    const pricingExhibitModel = {
      contractPriceProfileSeq: 123,
      contractAgeementId: '234219f3-9e86-405c-9867-bead4977855b',
      contractTypeName: 'ICMDistributionAgreementRegional'
    };
    spyOn(reviewService, 'savePricingExhibit').and.returnValue(Observable.of({}));
    spyOn(reviewService, 'fetchClmUrl').and.returnValue(Observable.of({}));
    spyOn(window, 'open');
    spyOn(sessionStorage, 'clear');

    // WHEN
    component.savePricingExhibit();

    // THEN
    expect(reviewService.savePricingExhibit).toHaveBeenCalledWith(pricingExhibitModel);
    expect(sessionStorage.clear).toHaveBeenCalled();
    expect(component.showSpinner).toBeTruthy();
    expect(window.open).toHaveBeenCalled();
  });

it('should invoke savePricingDocumentForFurtherance', () => {
  // GIVEN
  component.cppFurtheranceSeq = 123;
  component.parentAgreementId = '234219f3-9e86-405c-9867-bead4977855b';
  component.parentContractType = 'ICMDistributionAgreementRegional';
  const furtheranceBaseObj = {
    cppFurtheranceSeq: 123,
    agreementId: '234219f3-9e86-405c-9867-bead4977855b',
    contractType: 'ICMDistributionAgreementRegional'
  };
  spyOn(reviewService, 'savePricingDocumentForFurtherance').and.returnValue(Observable.of({}));
  spyOn(reviewService, 'fetchClmUrl').and.returnValue(Observable.of({}));
  spyOn(window, 'open');
  spyOn(sessionStorage, 'clear');

  // WHEN
  component.saveFurtherancePricingDocument();

  // THEN
  expect(reviewService.savePricingDocumentForFurtherance).toHaveBeenCalledWith(furtheranceBaseObj);
  expect(sessionStorage.clear).toHaveBeenCalled();
  expect(component.showSpinner).toBeTruthy();
  expect(window.open).toHaveBeenCalled();
});

})

describe('should test ngonInit() method of review component', () => {

  let component: ReviewComponent;
  let fixture: ComponentFixture<ReviewComponent>;
  let reviewService;
  let authService: AuthorizationService;
  let furtheranceService: FurtheranceService;

  const AUTH_DETAILS: AuthorizationDetails = new AuthorizationDetails(true, true, true, true, 'draft');

      beforeEach(() => {
        // stub UserService for test purposes
        let http: HttpClient;
        let reviewServiceStub: ReviewServiceStub = new ReviewServiceStub(this.http);

      TestBed.configureTestingModule({
        declarations: [ReviewComponent],
        providers: [
          { provide: ActivatedRoute, useClass: ActivatedRouteStub },
          { provide: Router, useClass: RouterStub },
          { provide: ReviewService, useValue: reviewServiceStub},
          StepperService,
          AuthorizationService,
          FurtheranceService
          ],
        imports: [HttpClientTestingModule, TranslateModule.forRoot({
          loader: {
            provide: TranslateHttpLoader,
            useFactory: () => new TranslateHttpLoader(http, 'public/assets/i18n', '.json'),
            deps: [Http]
         }})],
          }).overrideTemplate(ReviewComponent, '');

          fixture = TestBed.createComponent(ReviewComponent);
          component = fixture.componentInstance;
          reviewService = TestBed.get(ReviewService);
          authService = TestBed.get(AuthorizationService);
          furtheranceService = TestBed.get(FurtheranceService);
      });

      it('should call ngOnit method', () => {

        // GIVEN
        spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
        spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of([]));
        spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
        component.contractPriceProfileId = 44;

        // WHEN
        component.ngOnInit();

        // THEN
        expect(component.reviewData).not.toBe(null);
      });

});


export class ReviewServiceStub {

  constructor(private http: HttpClient) { }

  public fetchReviewData(contractPriceProfileId: string): Observable<{}>  {
    let params = new HttpParams();
    params.set('contractPriceProfileSeq', contractPriceProfileId);
    return Observable.of({
      'contractPricingReviewDTO': {
        'formalPriceAuditContractLanguage': 'Language',
        'priceVerificationContractLanguage': 'Language 2',
      },
      'distributionCenter': [
        'Abc',
        'DEF'
      ],
      'markupReviewDTO': {
        'markupGridDTOs': [
          {
            'markupName': 'Contract',
            'markup': [
              {
                'productType': 'FROZEN',
                'itemPriceId': 1,
                'markup': '1',
                'unit': '$',
                'markupType': 1,
                'effectiveDate': '2017-12-30 00:00:00.0',
                'expirationDate': '2019-12-07 00:00:00.0'
              },
            ]
          },
        ]
      },

    });
  }
}



