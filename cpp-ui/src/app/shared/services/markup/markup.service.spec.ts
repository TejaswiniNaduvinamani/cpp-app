import { HttpRequest } from '@angular/common/http';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { apiUrls } from './../app.url';
import { ItemDescriptionModel, ItemLevelMarkupModel, SubgroupMarkupModel, SubgroupSearchModel } from './../../../contract';
import { MarkupService } from 'app/shared';
import { MarkupGridDetails, RenameMarkupModel, MarkupIndicatorsModel, MarkupGridModel } from './../../../contract';
import * as moment from 'moment';

describe('Markup Service', () => {

  const fetchMarkupGridURL = apiUrls.fetchMarkupGridURL;
  const saveMarkupGridURL = apiUrls.saveMarkupGridURL;
  const addExceptionURL = apiUrls.addExceptionURL;
  const saveMarkupIndicatorsURL = apiUrls.saveMarkupIndicatorsURL;
  const renameMarkupExceptionURL = apiUrls.renameMarkupExceptionURL;
  const deleteMarkupExceptionURL = apiUrls.deleteMarkupExceptionURL;
  const createDefaultItemLevelMarkupURL = apiUrls.createDefaultItemLevelMarkupURL;
  const findItemInformationURL = apiUrls.findItemInformationURL;
  const deleteItemLevelMarkupURL = apiUrls.deleteItemLevelMarkupURL;
  const fetchMarkupIndicatorsURL = apiUrls.fetchMarkupIndicatorsURL;
  const createDefaultSubgroupMarkupURL = apiUrls.createDefaultSubgroupMarkupURL;
  const findSubgroupInformationURL = apiUrls.findSubgroupInformationURL;
  const deleteSubgroupMarkupURL = apiUrls.deleteSubgroupMarkupURL;

  const MARKUP_GRID_MODEL: MarkupGridModel[] = [new MarkupGridModel('IFS', 12, '6',
        '%', 2, new Date('2019/02/20'), new Date('2019/02/26'), false, false)];

  const DEFAULT_ITEM_LEVEL_GRID: ItemLevelMarkupModel = new ItemLevelMarkupModel(false, '', '0', '', '', '%', 1, new Date('2019/02/20'),
        new Date('2019/02/26'), false, false, false, false, false, false);

  const DEFAULT_SUBGROUP: SubgroupMarkupModel = new SubgroupMarkupModel('123', 'My Subgroup', '1.00', '$', 1, new Date('2019/02/20'),
       new Date('2019/02/26'), false, false, false, false, false);

  const MARKUP_GRID_DETAILS: MarkupGridDetails = new MarkupGridDetails('12', 30, 123, 'Contract', MARKUP_GRID_MODEL,
  [DEFAULT_SUBGROUP], [DEFAULT_ITEM_LEVEL_GRID], true, 1);

  const SAVE_MARKUP_INDICATORS: MarkupIndicatorsModel = new MarkupIndicatorsModel(123,
    new Date('2019/02/20'), new Date('2019/02/26'), true, true);

  const FETCH_MARKUP_INDICATORS: MarkupIndicatorsModel = new MarkupIndicatorsModel(123, new Date('2019/02/20'),
  new Date('2019/02/26'), true, true);

  const RENAME_MARKUP_MODEL: RenameMarkupModel = new RenameMarkupModel(123, '11', 'grocery', 'Grocery');

  const ITEM_DESCRIPTION_DETAILS: ItemDescriptionModel = new ItemDescriptionModel(101, '1-6/4 PK 9.5 OZ FRAPPUCCINO VA',
   'AC', '3', true, true, false);

  const SUBGROUP_DESCRIPTION_DETAILS: SubgroupSearchModel = new SubgroupSearchModel('123', 'My Subgroup', 'Valid', 200);

  let service: MarkupService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MarkupService]
    })

    service = TestBed.get(MarkupService);
  httpMock = TestBed.get(HttpTestingController);
  });

  it('should get MarkupGrid Details', () => {

    let actualResponse = null;
    const cppSeqId = '123';
    const pricingEffectiveDate = new Date('2019/02/20');
    const pricingExpirationDate = new Date('2019/12/30');

    service.fetchMarkupGridDetails('123', pricingEffectiveDate, pricingExpirationDate, 'Contract')
        .subscribe(response => {
          actualResponse = response
        })


    const expected = [MARKUP_GRID_DETAILS];

    httpMock.expectOne((req: HttpRequest<MarkupGridDetails[]>) => {
      const matchUrl = req.url === fetchMarkupGridURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId
        && req.params.get('pricingEffectiveDate') === moment.utc(pricingEffectiveDate).format('MM/DD/YYYY')
        && req.params.get('pricingExpirationDate') ===  moment.utc(pricingExpirationDate).format('MM/DD/YYYY')
        && req.params.get('contractName') === 'Contract';

      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should save MarkupGrid Details', () => {
    let actualResponse = null;
    const expected = MARKUP_GRID_DETAILS;
    service.saveMarkupGridDetails(MARKUP_GRID_DETAILS).subscribe((res) => {
        actualResponse = res
    });

    httpMock.expectOne((req: HttpRequest<MarkupGridDetails>) => {
      const matchUrl = req.url === saveMarkupGridURL;
      return matchUrl;
    }).flush(expected);
    expect(actualResponse).toEqual(expected);
  });


  it('should add a new exception and fetch exception defaults', () => {

    let actualResponse = null;
    const cppSeqId = '123';
    const pricingEffectiveDate = new Date('2019/02/20');
    const pricingExpirationDate = new Date('2019/12/30');

    service.addExceptionDetails('123', 'New Contract', pricingEffectiveDate, pricingExpirationDate)
        .subscribe(response => {
          actualResponse = response
      });

    const expected = MARKUP_GRID_DETAILS;

    httpMock.expectOne((req: HttpRequest<MarkupGridDetails>) => {
      const matchUrl = req.url === addExceptionURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId
        && req.params.get('exceptionName') === 'New Contract'
        && req.params.get('pricingEffectiveDate') === moment.utc(pricingEffectiveDate).format('MM/DD/YYYY')
        && req.params.get('pricingExpirationDate') ===  moment.utc(pricingExpirationDate).format('MM/DD/YYYY')
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should save markup indicators', () => {
    let actualResponse = null;
    const expected = SAVE_MARKUP_INDICATORS;

    service.saveMarkupIndicators(SAVE_MARKUP_INDICATORS)
        .subscribe((res) => {
          actualResponse = res
        })

        httpMock.expectOne((req: HttpRequest<MarkupIndicatorsModel>) => {
          const matchUrl = req.url === saveMarkupIndicatorsURL;
          return matchUrl;
        }).flush(expected);

        expect(actualResponse).toEqual(expected);

  })

  it('should rename markup exception', () => {
    let actualResponse = null;
    const expected = RENAME_MARKUP_MODEL;
    service.renameMarkupException(RENAME_MARKUP_MODEL)
        .subscribe((res) => {
          actualResponse = res
        })

        httpMock.expectOne((req: HttpRequest<RenameMarkupModel>) => {
          const matchUrl = req.url === renameMarkupExceptionURL;
          return matchUrl;
        }).flush(expected);

        expect(actualResponse).toEqual(expected);

  })

  it('should delete markup Exception', () => {
    let actual = null;
    const expected = [];
    service.deleteMarkupException('123', '12', 'grocery')
        .subscribe((res) => {
          actual = res;
        })
        httpMock.expectOne((req: HttpRequest<{}>) => {
          const matchUrl = req.url === deleteMarkupExceptionURL;
          const matchParams = req.params.get('contractPriceProfileSeq') === '123'
            && req.params.get('gfsCustomerId') === '12'
            && req.params.get('markupName') === 'grocery';

          return matchUrl && matchParams;
        }).flush(expected);
  });

  it('should fetch item level defaults', () => {

    let actualResponse = null;
    const cppSeqId = '123';
    const pricingEffectiveDate = new Date('2019/02/20');
    const pricingExpirationDate = new Date('2019/12/30');

    service.fetchItemLevelDefaults('123', pricingEffectiveDate, pricingExpirationDate)
        .subscribe(response => {
          actualResponse = response
        })

    const expected = [DEFAULT_ITEM_LEVEL_GRID];

    httpMock.expectOne((req: HttpRequest<ItemLevelMarkupModel>) => {
      const matchUrl = req.url === createDefaultItemLevelMarkupURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId
        && req.params.get('pricingEffectiveDate') === moment.utc(pricingEffectiveDate).format('MM/DD/YYYY')
        && req.params.get('pricingExpirationDate') ===  moment.utc(pricingExpirationDate).format('MM/DD/YYYY')
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch subgroup defaults', () => {

  const pricingEffectiveDate = new Date('2019/02/20');
  const pricingExpirationDate = new Date('2019/12/30');
  let actualResponse = null;

    service.fetchDefaultSubgroupMarkup(pricingEffectiveDate, pricingExpirationDate)
        .subscribe(response => {
          actualResponse = response
        })

    const expected = [DEFAULT_SUBGROUP];

    httpMock.expectOne((req: HttpRequest<SubgroupMarkupModel>) => {
      const matchUrl = req.url === createDefaultSubgroupMarkupURL;
      const matchParams = req.params.get('pricingExpirationDate') === moment.utc(pricingEffectiveDate).format('MM/DD/YYYY')
        && req.params.get('pricingEffectiveDate') ===  moment.utc(pricingExpirationDate).format('MM/DD/YYYY')

      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch subgroup description details', () => {

    let actualResponse = null;
    const subgroupId = '123';
    const cmgCustomerId = '30';
    const cmgCustomerTypeCode = '101';
    const contractPriceProfileSeq = '123';

    service.fetchSubgroupDescription(subgroupId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq)
        .subscribe(response => {
          actualResponse = response
        })

    const expected = [SUBGROUP_DESCRIPTION_DETAILS];

    httpMock.expectOne((req: HttpRequest<SubgroupSearchModel>) => {
      const matchUrl = req.url === findSubgroupInformationURL;
      const matchParams = req.params.get('subgroupId') === subgroupId
        && req.params.get('gfsCustomerId') === '30'
        && req.params.get('gfsCustomerType') === '101'
        && req.params.get('contractPriceProfileSeq') === '123'

      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch item description details', () => {

    let actualResponse = null;
    const itemId = '166720';
    const cmgCustomerId = '30';
    const cmgCustomerTypeCode = '101';
    const contractPriceProfileSeq = '123';

    service.fetchItemDescription(itemId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq)
        .subscribe(response => {
          actualResponse = response
        })

    const expected = [ITEM_DESCRIPTION_DETAILS];

    httpMock.expectOne((req: HttpRequest<ItemDescriptionModel>) => {
      const matchUrl = req.url === findItemInformationURL;
      const matchParams = req.params.get('itemId') === itemId
        && req.params.get('cmgCustomerId') === '30'
        && req.params.get('cmgCustomerTypeCode') === '101'
        && req.params.get('contractPriceProfileSeq') === '123'

      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should delete item-level markup', () => {
    let actual = null;
    const expected = [];
    service.deleteItemLevelMarkup('123', '12', '31', '166720', 'BLUEBERRY IQF 4-5# GFS')
        .subscribe((res) => {
          actual = res;
        })
        httpMock.expectOne((req: HttpRequest<{}>) => {
          const matchUrl = req.url === deleteItemLevelMarkupURL;
          const matchParams = req.params.get('contractPriceProfileSeq') === '123'
            && req.params.get('gfsCustomerId') === '12'
            && req.params.get('gfsCustomerType') === '31'
            && req.params.get('itemId') === '166720'
            && req.params.get('itemDesc') === 'BLUEBERRY IQF 4-5# GFS';

          return matchUrl && matchParams;
        }).flush(expected);
  });

  it('should fetch markup indicators', () => {

    let actualResponse = null;
    const cppSeqId = '123';

    service.fetchMarkupIndicators('123')
        .subscribe(response => {
          actualResponse = response
        })

    const expected = [FETCH_MARKUP_INDICATORS];

    httpMock.expectOne((req: HttpRequest<MarkupIndicatorsModel>) => {
      const matchUrl = req.url === fetchMarkupIndicatorsURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId

      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should delete subgroup markup', () => {
    let actual = null;
    const expected = [];
    service.deleteSubgroupMarkup('123', '12', '31', '20213')
        .subscribe((res) => {
          actual = res;
        })
        httpMock.expectOne((req: HttpRequest<{}>) => {
          const matchUrl = req.url === deleteSubgroupMarkupURL;
          const matchParams = req.params.get('contractPriceProfileSeq') === '123'
            && req.params.get('gfsCustomerId') === '12'
            && req.params.get('gfsCustomerType') === '31'
            && req.params.get('subgroupId') === '20213';

          return matchUrl && matchParams;
        }).flush(expected);
  });
})
