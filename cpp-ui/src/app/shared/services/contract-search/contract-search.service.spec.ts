import { HttpRequest } from '@angular/common/http';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { apiUrls } from './../app.url';
import { ContractSearchGridModel } from 'app/contract-search/contract-search.model';
import { ContractSearchService } from './contract-search.service';

describe('ContractSearchService', () => {

  const searchContractByContractNameURL =  apiUrls.searchContractByContractNameURL;
  const searchContractByCustomerURL = apiUrls.searchContractByCustomerURL;
  const searchContractByCppIdURL = apiUrls.searchContractByCppIdURL;


  let service: ContractSearchService;
  let httpMock: HttpTestingController;

  const CONTRACT_NAME_SEARCH_RESPONSE: Array<ContractSearchGridModel> = [
    new ContractSearchGridModel('Brinker International', 123, 1, 'Heinz', 'No', '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308',
    'ICMDistributionAgreementRegional',  new Date('2018-01-20'), 'Approved',
    '/furtheranceinformation?agreementId=c03c2f79-9192-4216-b098-821f88342875&contractType=ICMDistributionAgreementRegional')
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ContractSearchService]
    });

    service = TestBed.get(ContractSearchService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should search contracts by contract name', () => {

    let actualResponse = null;
    const searchContractName = 'Brinker';

    service.searchContractByContractName(searchContractName)
      .subscribe((response: ContractSearchGridModel[]) =>
        actualResponse = response);

    const expected = [CONTRACT_NAME_SEARCH_RESPONSE];

    httpMock.expectOne((req: HttpRequest<ContractSearchGridModel[]>) => {
      const matchUrl = req.url === searchContractByContractNameURL;
      const matchParams = req.params.get('searchContractName') === searchContractName;
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should search contracts by cpp Id', () => {

    let actualResponse = null;
    const cppId = '6734';

    service.searchContractByCppId(cppId)
      .subscribe((response: ContractSearchGridModel[]) =>
        actualResponse = response);

    const expected = [CONTRACT_NAME_SEARCH_RESPONSE];

    httpMock.expectOne((req: HttpRequest<ContractSearchGridModel[]>) => {
      const matchUrl = req.url === searchContractByCppIdURL;
      const matchParams = req.params.get('cppId') === cppId;
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should search contracts by customer details', () => {

    let actualResponse = null;
    const gfsCustomerId = '6734';
    const gfsCustomerTypeCode = 22;

    service.searchContractByCustomer(gfsCustomerId, gfsCustomerTypeCode)
      .subscribe((response: ContractSearchGridModel[]) =>
        actualResponse = response);

    const expected = [CONTRACT_NAME_SEARCH_RESPONSE];

    httpMock.expectOne((req: HttpRequest<ContractSearchGridModel[]>) => {
      const matchUrl = req.url === searchContractByCustomerURL;
      const matchParams = req.params.get('gfsCustomerId') === gfsCustomerId
      && req.params.get('gfsCustomerTypeCode') === String(gfsCustomerTypeCode);
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });
});
