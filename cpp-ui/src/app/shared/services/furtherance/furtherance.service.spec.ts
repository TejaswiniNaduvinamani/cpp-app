import { HttpRequest } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { apiUrls } from '../app.url';
import { FurtheranceService } from './furtherance.service';
import { FurtheranceBaseModel, ValidateFurtheranceModel, FurtheranceInformationModel } from '../../../furtherance';
import { FutureItemModel, ItemAssignmentModel, ItemSaveStatusModel } from '../../../assignment-header';
import { ItemLevelMarkupModel, SubgroupMarkupModel, ItemDetailsForFurtheranceModel,
  SaveFurtheranceSplitCaseModel, SplitCaseFees } from './../../../contract';

import { MarkupGridDetails, MarkupGridModel } from './../../../contract/markup/markup-grid/markup-grid.model';

describe('FurtheranceService', () => {

  const validateHasInProgressFurtheranceURL = apiUrls.validateHasInProgressFurtheranceURL;
  const deleteItemLevelMarkupFurtheranceURL = apiUrls.deleteItemLevelMarkupFurtheranceURL;
  const createNewFurtheranceURL = apiUrls.createNewFurtheranceURL;
  const fetchInProgressFurtheranceInfoURL = apiUrls.fetchInProgressFurtheranceInfoURL;
  const fetchFurtheranceInfoURL = apiUrls.fetchFurtheranceInfoURL;
  const fetchMappedItemsForFurtheranceURL = apiUrls.fetchMappedItemsForFurtheranceURL;
  const saveFurtheranceSaveURL = apiUrls.saveFurtheranceInformationURL;
  const saveFurtheranceMarkupGridURL = apiUrls.saveFurtheranceMarkupGridURL;
  const activatePricingForFurtheranceURL = apiUrls.activatePricingForFurtheranceURL;
  const enableActivatePricingForFurtheranceURL = apiUrls.enableActivatePricingForFurtheranceURL;
  const saveMappedItemsForFurtheranceURL = apiUrls.saveMappedItemsForFurtheranceURL;
  const deleteMappedItemForFurtheranceURL = apiUrls.deleteMappedItemForFurtheranceURL;
  const saveFurtheranceSplitCaseURL = apiUrls.saveFurtheranceSplitCaseURL;
  const canEditFurtheranceURL = apiUrls.canEditFurtherance;

  const FURTHERANCE_BASE_MODEL: FurtheranceBaseModel = new FurtheranceBaseModel('c0e3a4f5-7479-4d22-b349-e61c50b3e26c',
   'ICMDistributionAgreementRegional', 101);

  const VALIDATE_FURTHERANCE_MODEL: ValidateFurtheranceModel = new ValidateFurtheranceModel(true);

  const FURTHERENCE_INFORMATION_MODEL: FurtheranceInformationModel = new FurtheranceInformationModel
    (165, 123, 65, 'c0e3a4f5-7479-4d22-b349-e61c50b3e26c', new Date('12/12/2020'), 'Reason for change', 'Reference');

  const ITEM_ASSIGNMENT_LIST: ItemAssignmentModel[] = [new ItemAssignmentModel('166720', 'BLUEBERRY', false, false, false)];

  const FUTURE_ITEM_LIST: FutureItemModel = new FutureItemModel(21, 'Heinz Ketchup', 'Chilli', '150',
       11, ITEM_ASSIGNMENT_LIST, false, false, []);

  const MARKUP_GRID_MODEL: MarkupGridModel[] = [new MarkupGridModel('IFS', 12, '6',
    '%', 2, new Date('2019/02/20'), new Date('2019/02/26'), false, false)];

  const DEFAULT_ITEM_LEVEL_GRID: ItemLevelMarkupModel = new ItemLevelMarkupModel(false, '', '0', '', '', '%', 1, new Date('2019/02/20'),
    new Date('2019/02/26'), false, false, false, false, false, false);

  const DEFAULT_SUBGROUP: SubgroupMarkupModel = new SubgroupMarkupModel('123', 'My Subgroup', '1.00', '$', 1, new Date('2019/02/20'),
   new Date('2019/02/26'), false, false, false, false, false);

  const MARKUP_GRID_DETAILS: MarkupGridDetails = new MarkupGridDetails('12', 30, 123, 'Contract', MARKUP_GRID_MODEL,
    [DEFAULT_SUBGROUP], [DEFAULT_ITEM_LEVEL_GRID], true, 1);

  const ITEM_SAVE_STATUS_MODEL: ItemSaveStatusModel = new ItemSaveStatusModel(140, 'Item was saved');

  const ITEM_DETAILS_FOR_FURTHERANCE: ItemDetailsForFurtheranceModel = new ItemDetailsForFurtheranceModel(21, 123, 'Hienz Ketchup', '151',
  31, ITEM_ASSIGNMENT_LIST, []);

  const SPLIT_CASE_ROW_1: SplitCaseFees = new SplitCaseFees('GROCERY', 35, '%', new Date('2020-02-01'), new Date('2020-02-01'),
   '1', false, false);

  const SAVE_SPLITCASE_MODEL: SaveFurtheranceSplitCaseModel = new SaveFurtheranceSplitCaseModel(1234, [SPLIT_CASE_ROW_1], 456);


  let service: FurtheranceService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FurtheranceService]
    });

    service = TestBed.get(FurtheranceService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should validate for in progress status of furtherance', () => {
    let actualResponse = null;

    service.validateHasInProgressFurtherance('c0e3a4f5-7479-4d22-b349-e61c50b3e26c')
        .subscribe((response) =>
          actualResponse = response);

    const expected = [VALIDATE_FURTHERANCE_MODEL];

    httpMock.expectOne((req: HttpRequest<ValidateFurtheranceModel>) => {
      const matchUrl = req.url === validateHasInProgressFurtheranceURL;
      const matchParams = req.params.get('parentAgreementId') === 'c0e3a4f5-7479-4d22-b349-e61c50b3e26c'
      return matchUrl && matchParams;
    }).flush(expected);
      expect(actualResponse).toEqual(expected);
  });

  it('should create new furtherance', () => {
    let actualResponse = null;

    service.createNewFurtherance('c0e3a4f5-7479-4d22-b349-e61c50b3e26c', 'ICMDistributionAgreementRegional')
        .subscribe((response) =>
          actualResponse = response);

    const expected = [FURTHERANCE_BASE_MODEL];

    httpMock.expectOne((req: HttpRequest<FurtheranceBaseModel>) => {
      const matchUrl = req.url === createNewFurtheranceURL;
      const matchParams = req.params.get('parentAgreementId') === 'c0e3a4f5-7479-4d22-b349-e61c50b3e26c'
        && req.params.get('contractType') === 'ICMDistributionAgreementRegional'
      return matchUrl && matchParams;
    }).flush(expected);
      expect(actualResponse).toEqual(expected);
  });

  it('should fetch in progress furtherance information', () => {
    let actualResponse = null;

    service.fetchInProgressFurtheranceInfo('c0e3a4f5-7479-4d22-b349-e61c50b3e26c')
        .subscribe((response) =>
          actualResponse = response);

    const expected = [FURTHERANCE_BASE_MODEL];

    httpMock.expectOne((req: HttpRequest<FurtheranceBaseModel>) => {
      const matchUrl = req.url === fetchInProgressFurtheranceInfoURL;
      const matchParams = req.params.get('parentAgreementId') === 'c0e3a4f5-7479-4d22-b349-e61c50b3e26c'
      return matchUrl && matchParams;
    }).flush(expected);
      expect(actualResponse).toEqual(expected);
  });

  it('should delete item-level markup for furtherance', () => {
    let actual = null;
    const expected = [];
    service.deleteItemLevelMarkupFurtherance('123', 23, '12', '31', '166720', 'BLUEBERRY IQF 4-5# GFS')
        .subscribe((res) => {
          actual = res;
        })
        httpMock.expectOne((req: HttpRequest<{}>) => {
          const matchUrl = req.url === deleteItemLevelMarkupFurtheranceURL;
          const matchParams = req.params.get('contractPriceProfileSeq') === '123'
            && req.params.get('cppFurtheranceSeq') === '23'
            && req.params.get('gfsCustomerId') === '12'
            && req.params.get('gfsCustomerTypeCode') === '31'
            && req.params.get('itemId') === '166720'
            && req.params.get('itemDesc') === 'BLUEBERRY IQF 4-5# GFS';
          return matchUrl && matchParams;
        }).flush(expected);
  });

  it('should fetch furtherance information', () => {
    let actualResponse = null;

    service.fetchFurtheranceInfo('c0e3a4f5-7479-4d22-b349-e61c50b3e26c', 165)
      .subscribe((response) =>
        actualResponse = response);

    const expected = [FURTHERENCE_INFORMATION_MODEL];

    httpMock.expectOne((req: HttpRequest<FurtheranceBaseModel>) => {
      const matchUrl = req.url === fetchFurtheranceInfoURL;
      const matchParams = req.params.get('parentAgreementId') === 'c0e3a4f5-7479-4d22-b349-e61c50b3e26c'
        && req.params.get('cppFurtheranceSeq') === '165'
      return matchUrl && matchParams;
    }).flush(expected);
      expect(actualResponse).toEqual(expected);
  });

  it('should save furtherence information', () => {
    let actualResponse = null;
    const expected = [FURTHERENCE_INFORMATION_MODEL];

    service.saveFurtheranceInfo(FURTHERENCE_INFORMATION_MODEL).subscribe((res) => {
        actualResponse = res;
      })

      httpMock.expectOne((req: HttpRequest<FurtheranceInformationModel>) => {
        const matchUrl = req.url === saveFurtheranceSaveURL;
        return matchUrl;
      }).flush(expected);

      expect(actualResponse).toEqual(expected);
  });

  it('should save furtherence markup', () => {
    let actualResponse = null;
    const expected = MARKUP_GRID_DETAILS;

    service.saveMarkupGridForFurtherance(MARKUP_GRID_DETAILS).subscribe((res) => {
        actualResponse = res;
      })

      httpMock.expectOne((req: HttpRequest<MarkupGridDetails>) => {
        const matchUrl = req.url === saveFurtheranceMarkupGridURL;
        return matchUrl;
      }).flush(expected);

      expect(actualResponse).toEqual(expected);
  });

  it('should fetch mapped items for furtherance', () => {
    let actualResponse = null;

    service.fetchMappedItemsForFurtherance(123, '301', 31, 'Heinz Ketchup')
      .subscribe((response) =>
        actualResponse = response);

    const expected = FUTURE_ITEM_LIST;

    httpMock.expectOne((req: HttpRequest<FutureItemModel>) => {
      const matchUrl = req.url === fetchMappedItemsForFurtheranceURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === '123'
        && req.params.get('gfsCustomerId') === '301'
        && req.params.get('gfsCustomerTypeCode') === '31'
        && req.params.get('itemDesc') === 'Heinz Ketchup'
        return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should activate pricing for furtherance', () => {
    let actualResponse = null;

    service.activatePricingForFurtherance(321, 'Executed')
      .subscribe((response) =>
        actualResponse = response);

    const expected = {};

    httpMock.expectOne((req: HttpRequest<{}>) => {
      const matchUrl = req.url === activatePricingForFurtheranceURL;
      const matchParams = req.params.get('cppFurtheranceSeq') === '321'
        && req.params.get('clmContractStatus') === 'Executed'
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should enable/disable activate pricing button for furtherance', () => {
    let actualResponse = null;

    service.enableActivatePricingForFurtherance(321, 'Executed')
      .subscribe((response) =>
        actualResponse = response);

    const expected = {enableActivatePricingButton: true};

    httpMock.expectOne((req: HttpRequest<{enableActivatePricingButton}>) => {
      const matchUrl = req.url === enableActivatePricingForFurtheranceURL;
      const matchParams = req.params.get('cppFurtheranceSeq') === '321'
        && req.params.get('clmContractStatus') === 'Executed'
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should save mapped items for furtherence mode', () => {
    let actualResponse = null;
    const expected = ITEM_SAVE_STATUS_MODEL;

    service.saveMappedItemsForFurtherance(ITEM_DETAILS_FOR_FURTHERANCE).subscribe((res) => {
        actualResponse = res;
      })

      httpMock.expectOne((req: HttpRequest<ItemDetailsForFurtheranceModel>) => {
        const matchUrl = req.url === saveMappedItemsForFurtheranceURL;
        return matchUrl;
      }).flush(expected);

      expect(actualResponse).toEqual(expected);
  });

  it('should delete item-assignment for furtherance', () => {
    let actual = null;
    const expected = [];
    service.deleteItemAssignmentForFurtherance('123', 23, '12', '31', '166720', 'BLUEBERRY IQF 4-5# GFS')
        .subscribe((res) => {
          actual = res;
        })
        httpMock.expectOne((req: HttpRequest<{}>) => {
          const matchUrl = req.url === deleteMappedItemForFurtheranceURL;
          const matchParams = req.params.get('contractPriceProfileSeq') === '123'
            && req.params.get('cppFurtheranceSeq') === '23'
            && req.params.get('gfsCustomerId') === '12'
            && req.params.get('gfsCustomerTypeCode') === '31'
            && req.params.get('itemId') === '166720'
            && req.params.get('itemDesc') === 'BLUEBERRY IQF 4-5# GFS';
          return matchUrl && matchParams;
        }).flush(expected);
  });

  it('should save SplitCase Fees for furtherance', () => {
    let actualResponse = null;
    const expected = SAVE_SPLITCASE_MODEL;

    service.saveSplitCaseFee(SAVE_SPLITCASE_MODEL).subscribe((res) => {
      actualResponse = res;
    })

    httpMock.expectOne((req: HttpRequest<SaveFurtheranceSplitCaseModel>) => {
      const matchUrl = req.url === saveFurtheranceSplitCaseURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch canEditFurtherance flag in furtherance mode', () => {
    let actualResponse = null;
    const expected = {canEditFurtherance: true};

    service.canEditFurtherance(123).subscribe((res) => {
      actualResponse = res;
    })

    httpMock.expectOne((req: HttpRequest<{canEditFurtherance: boolean}>) => {
      const matchUrl = req.url === canEditFurtheranceURL;
      const matchParams = req.params.get('furtheranceSeq') === '123'
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
});
})
