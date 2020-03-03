import { HttpRequest } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { apiUrls } from '../app.url';
import { AssignmentMarkupModel } from 'app/assignment-header/assignments/assignments.model';
import { AssignmentsService } from './assignments.service';
import {
  RealCustomerModel, SaveAssignmentModel, RealCustomerSearchModel, MarkupDisplayModel,
  DeleteRealCustomerModel, ItemLevelDisplayModel, SubgroupDisplayModel
} from 'app/assignment-header';

describe('Assignments Service', () => {

  const assignmentFetchURL = apiUrls.fetchMarkupsAssignmentURL;
  const customerFetchURL = apiUrls.fetchCustomerTypes;
  const findRealCustomerURL = apiUrls.findCustomer;
  const saveAssignmentsURL = apiUrls.saveAssignmentsURL;
  const deleteRealCustomerURL = apiUrls.deleteRealCustomer;
  const activatePricingURL = apiUrls.activatePricingURL;
  const enableActivatePricingURL = apiUrls.enableActivatePricingURL;

  const MARKUP_VALUE: Array<MarkupDisplayModel> = [
    new MarkupDisplayModel('Grocery', 12, '2', '%', 1, new Date('2019/02/20'), new Date('2019/02/26'))
  ];

  const ITEM_VALUE: Array<ItemLevelDisplayModel> = [
    new ItemLevelDisplayModel(null, '100003', 'CKN WINGS', null, '35', '$', 1, new Date('2019/02/20'), new Date('2019/02/26'))
  ];

  const REAL_CUST_VALUE: Array<RealCustomerModel> = [
    new RealCustomerModel('Heinz', '110', '31', 'SMG', true, false, false)
  ];

  const SUBGROUP_VALUE: Array<SubgroupDisplayModel> = [
    new SubgroupDisplayModel('Heinz', '10', '%', 1)
  ];

  const ASSIGNMENT_MARKUP: Array<AssignmentMarkupModel> = [
    new AssignmentMarkupModel('123', 'Contract', 'CMG', 2, MARKUP_VALUE, SUBGROUP_VALUE, ITEM_VALUE, true, false, REAL_CUST_VALUE, 1)
  ];

  const SAVE_ASSIGNMENT: SaveAssignmentModel =
    new SaveAssignmentModel(21, '1951', 4, REAL_CUST_VALUE)
    ;
  const CUSTOMER_TYPES: Array<string> = ['Contract Management Group', 'Super Street Managed', 'Super Bid Managed', 'Bid Managed'];
  const CUSTOMER_NAME: RealCustomerSearchModel = new RealCustomerSearchModel('Customer Name', 200, 'abc');

  let service: AssignmentsService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AssignmentsService]
    });

    service = TestBed.get(AssignmentsService);
    httpMock = TestBed.get(HttpTestingController);
  });


  it('should get the saved markup list', () => {

    let actualResponse = null;
    const cppSeqId = '123';

    service.fetchAssignmentMarkups(cppSeqId)
      .subscribe((response) =>
        actualResponse = response);

    const expected = [ASSIGNMENT_MARKUP];

    httpMock.expectOne((req: HttpRequest<AssignmentMarkupModel[]>) => {
      const matchUrl = req.url === assignmentFetchURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId;
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch Customer Types', () => {
    let actualResponse = null;

    service.fetchCustomerType()
      .subscribe((response) =>
        actualResponse = response);

    const expected = [CUSTOMER_TYPES];

    httpMock.expectOne((req: HttpRequest<string>) => {
      const matchUrl = req.url === customerFetchURL;
      return matchUrl;
    }).flush(expected);
  });

  it('should find Real Customer', () => {
    let actualResponse = null;


  service.findRealCustomer('123', 'Family', '123', '12345', '31')
    .subscribe((response) =>
        actualResponse = response);

    const expected = [CUSTOMER_NAME];

  httpMock.expectOne((req: HttpRequest<RealCustomerSearchModel>) => {
    const matchUrl = req.url === findRealCustomerURL;
    const matchParams = req.params.get('gfsCustomerId') === '123'
    && req.params.get('gfsCustomerType') === 'Family'
    && req.params.get('contractPriceProfileSeq') === '123'
    && req.params.get('cmgCustomerId') === '12345'
    && req.params.get('cmgCustomerType') === '31'
    return matchUrl && matchParams;
  }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should save assignment', () => {
    let actualResponse = null;
    const expected = SAVE_ASSIGNMENT;
    service.saveAssignment(SAVE_ASSIGNMENT)
      .subscribe((res) => {
        actualResponse = res
      })

    httpMock.expectOne((req: HttpRequest<SaveAssignmentModel>) => {
      const matchUrl = req.url === saveAssignmentsURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);

  });

it('should delete real customer', () => {
  let actual = null;
  service.deleteRealCustomer('123', '12', '21', '456', '45')
      .subscribe((res) => {
        actual = res;
      });
    const expected = {};

      httpMock.expectOne((req: HttpRequest<{}>) => {
        const matchUrl = req.url === deleteRealCustomerURL;
        const matchParams = req.params.get('realCustomerId') === '123'
          && req.params.get('realCustomerType') === '12'
          && req.params.get('contractPriceProfileSeq') === '21'
          && req.params.get('cmgCustomerId') === '456'
          && req.params.get('cmgCustomerType') === '45';

      return matchUrl && matchParams;
    }).flush(expected);

  expect(actual).toEqual(expected);
  });

  it('should activate pricing', () => {
    let actualResponse = null;

    service.activatePricing('123', true, 'Draft')
      .subscribe((response) =>
        actualResponse = response);

    const expected = {};

    httpMock.expectOne((req: HttpRequest<{}>) => {
      const matchUrl = req.url === activatePricingURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === '123'
        && req.params.get('isAmendment') === 'true'
        && req.params.get('clmContractStatus') === 'Draft'
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });


  it('should fetch Activate Pricing Button State', () => {
    let actualResponse = null;

    service.fetchActivatePricingButtonState('123')
      .subscribe((response) =>
        actualResponse = response);

    const expected = {enableActivatePricingButton: false};

    httpMock.expectOne((req: HttpRequest<{enableActivatePricingButton: boolean}>) => {
      const matchUrl = req.url === enableActivatePricingURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === '123'
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

});
