import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpRequest } from '@angular/common/http';
import { TestBed, inject } from '@angular/core/testing';

import { apiUrls } from './../app.url';
import { FutureItemModel, ItemAssignmentModel, AssignmentItemDescriptionModel } from './../../../assignment-header';
import { ItemAssignmentService } from './item-assignment.service';

describe('ItemAssignmentService', () => {

  const fetchAllFutureItemsURL = apiUrls.fetchAllFutureItemsURL;
  const saveItemAssignmentsURL = apiUrls.saveItemAssignmentsURL;
  const deleteItemAssignmentURL = apiUrls.deleteItemAssignmentURL;
  const findItemInformationURL = apiUrls.findItemInformationURL;

  const ITEM_ASSIGNMENT_LIST: ItemAssignmentModel[] = [new ItemAssignmentModel('166720', 'BLUEBERRY', false, false, false)];

  const FUTURE_ITEM_LIST: FutureItemModel = new FutureItemModel(21, 'Heinz Ketchup', 'Chilli', '150', 11,
   ITEM_ASSIGNMENT_LIST, false, false, []);

  let service: ItemAssignmentService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ItemAssignmentService]
    })

    service = TestBed.get(ItemAssignmentService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should fetch all future item details', () => {
    let actualResponse = null;
    const cppSeqId = '123';

    service.fetchAllFutureItems('123')
      .subscribe(response => {
        actualResponse = response
      })

    const expected = [FUTURE_ITEM_LIST];

    httpMock.expectOne((req: HttpRequest<FutureItemModel[]>) => {
      const matchUrl = req.url === fetchAllFutureItemsURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId;
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should save item assignment', () => {
    let actualResponse = null;
    const expected = FUTURE_ITEM_LIST;
    service.saveItemAssignment(FUTURE_ITEM_LIST)
        .subscribe((res) => {
          actualResponse = res
        })

        httpMock.expectOne((req: HttpRequest<FutureItemModel>) => {
          const matchUrl = req.url === saveItemAssignmentsURL;
          return matchUrl;
        }).flush(expected);

        expect(actualResponse).toEqual(expected);
  });

  it('should delete item assignment', () => {
    let actual = null;
    const expected = [];
    service.deleteItemAssignment('123', '150', '31', '166720', 'BLUEBERRY IQF 4-5# GFS')
        .subscribe((res) => {
          actual = res;
        });

        httpMock.expectOne((req: HttpRequest<{}>) => {
          const matchUrl = req.url === deleteItemAssignmentURL;
          const matchParams = req.params.get('contractPriceProfileSeq') === '123'
            && req.params.get('gfsCustomerId') === '150'
            && req.params.get('gfsCustomerTypeCode') === '31'
            && req.params.get('itemId') === '166720'
            && req.params.get('futureItemDesc') === 'BLUEBERRY IQF 4-5# GFS'

            return matchUrl && matchParams;
        }).flush(expected);
  });

  it('should fetch item description for assignments', () => {
    let actual = null;
    const ITEM_DESCRIPTION_MODEL: AssignmentItemDescriptionModel =
    new AssignmentItemDescriptionModel(12, 'Biskrit', '03', '22', true, true, false);
    const expected = [ITEM_DESCRIPTION_MODEL];

    service.fetchItemDescriptionForAssignment('12', '12', '12', '12' )
      .subscribe( response => {
        actual = response;
      });

      httpMock.expectOne((req: HttpRequest<{}>) => {
        const matchUrl = req.url === findItemInformationURL;
        const matchParams = req.params.get('itemId') === '12'
          && req.params.get('cmgCustomerId') === '12'
          && req.params.get('cmgCustomerTypeCode') === '12'
          && req.params.get('contractPriceProfileSeq') === '12'

          return matchUrl && matchParams
      }).flush(expected);
  })

});
