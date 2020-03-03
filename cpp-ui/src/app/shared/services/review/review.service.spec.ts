import { TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpRequest } from '@angular/common/http';

import { SplitCaseFeesModel, SplitCaseReviewDTO, ContractPricingDTO, MarkupGridDTO,
  MarkupReviewDTO, ReviewData, MarkupGridModel, ItemGridModel, PricingExhibitModel, 
  SubgroupGridModel } from './../../../contract/review/review.model';
import { CPPExhibitDownload } from 'app/contract/review/review.model';
import { FurtheranceBaseModel } from 'app/furtherance';
import { ReviewService } from './review.service';
import { apiUrls } from 'app/shared';

describe('Review Service', () => {

  const reviewURL = apiUrls.fetchReviewDataURL;
  const clmURL = apiUrls.fetchClmUrl;
  const savePricingExhibitURL = apiUrls.savePricingExhibitURL;
  const savePricingDocumentForFurtheranceURL = apiUrls.savePricingDocumentForFurtheranceURL;

  const SPLITCASE_FEE_MODEL: SplitCaseFeesModel = new SplitCaseFeesModel('Grocery',
            12, '$', new Date('2019/02/20'), new Date('2019/02/26'), '10');

  const CONTRACT_PRICING_REVIEW_DTO: ContractPricingDTO = new ContractPricingDTO('Yes',
            'Text', 'Yes', 'Text', 'Yes', 'Text', 'Yes', 'Yes', 'Text');

  const MARKUP_GRID_MODEL: MarkupGridModel = new MarkupGridModel('Grocery',
            12, '12', '$', 10, new Date('2019/02/20'), new Date('2019/02/26'));

  const ITEM_GRID_MODEL: ItemGridModel = new ItemGridModel(false, '123', 'Heinz', 'KYC', '2',
             '$', 2, new Date('2020/02/26'), new Date('2020/03/27'));
  
  const SUBGROUP_GRID_MODEL: SubgroupGridModel = new SubgroupGridModel("SAFETY EQUIPMENT & SUPPLIES", "4.00","%",1);           

  const SPLITCASE_GRID_DTO: SplitCaseReviewDTO = new SplitCaseReviewDTO('feeTypeContractLanguage',2 ,[SPLITCASE_FEE_MODEL]);

  const MARKUP_GRID_DTO: MarkupGridDTO = new MarkupGridDTO('CONTRACT', [MARKUP_GRID_MODEL], [ITEM_GRID_MODEL], [SUBGROUP_GRID_MODEL]);

  const PRICING_EXHIBIT_MODEL: PricingExhibitModel = new PricingExhibitModel(123,
     '234219f3-9e86-405c-9867-bead4977855b', 'ICMDistributionAgreementRegional');

  const FURTHERANCE_BASE_MODEL: FurtheranceBaseModel = new FurtheranceBaseModel('234219f3-9e86-405c-9867-bead4977855b',
    'ICMDistributionAgreementRegional', 123);

  const MARKUP_REVIEW_DTO: MarkupReviewDTO = new MarkupReviewDTO('Yes', 'MarkupBasedOnSellContractLanguage1',
  'MarkupTypeDefinitionSellUnitLanguage', 'MarkupTypeDefinitionPerCaseLanguage', 'MarkupTypeDefinitionPerWeightLanguage',
  'MarkupBasedOnSellContractLanguage2', [MARKUP_GRID_DTO])

  const REVIEW_DATA = new ReviewData(CONTRACT_PRICING_REVIEW_DTO,
           ['1', '2'], MARKUP_REVIEW_DTO, SPLITCASE_GRID_DTO)

  let service: ReviewService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReviewService]
    });

    service = TestBed.get(ReviewService);
    httpMock = TestBed.get(HttpTestingController);

  })

  afterEach(() => {
    httpMock.verify();
  });

  it('should get review data', () => {

    let actualResponse = null;
    const cppSeqId = '123';

    service.fetchReviewData('123')
      .subscribe((res) => {
        actualResponse = res});

    const expected = [REVIEW_DATA];

    httpMock.expectOne((req: HttpRequest<ReviewData>) => {
      const matchUrl = req.url === reviewURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId;
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should save Pricing Exhibit', () => {
    let actualResponse = null;
    const expected = PRICING_EXHIBIT_MODEL;
    service.savePricingExhibit(PRICING_EXHIBIT_MODEL).subscribe((res) => {
        actualResponse = res
    });

    httpMock.expectOne((req: HttpRequest<PricingExhibitModel>) => {
      const matchUrl = req.url === savePricingExhibitURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should save Pricing Document for Furtherance', () => {
    let actualResponse = null;
    const expected = FURTHERANCE_BASE_MODEL;
    service.savePricingDocumentForFurtherance(FURTHERANCE_BASE_MODEL).subscribe((res) => {
        actualResponse = res
    });

    httpMock.expectOne((req: HttpRequest<FurtheranceBaseModel>) => {
      const matchUrl = req.url === savePricingDocumentForFurtheranceURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch icertis url', () => {

    let actualResponse = null;

    service.fetchClmUrl().subscribe((res) => {
      actualResponse = res
    });

    const expected = [{ 'clmUrl' : 'https://gfsdev.icertis.com/Agreement/Details?entityName='}]

    httpMock.expectOne((req: HttpRequest<ReviewData>) => {
      const matchUrl = req.url === clmURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  })

})
