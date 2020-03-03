import { HttpRequest } from '@angular/common/http';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { Router, ActivatedRoute } from '@angular/router';
import { TestBed } from '@angular/core/testing';

import { ActivatedRouteStub } from './../../../../../test/unit-testing/mock/activated-route-stub';
import { RouterStub } from './../../../../../test/unit-testing/mock/router-stub';
import { apiUrls } from './../app.url';
import { PricingInformationService, CONTRACT_TYPES } from 'app/shared';
import { PricingInformation, ContractInformation, ClmContractDetails, CPPInformationDetails } from './../../../contract';


describe('Pricing Information Service', () => {

  const savePricingInformationURL = apiUrls.savePricingInformationURL;
  const fetchPricingInformationURL = apiUrls.fetchPricingInformationURL;
  const fetchContractPriceProfileInfoURL = apiUrls.fetchContractPriceProfileInfoURL;
  const deletePricingExhibitURL = apiUrls.deletePricingExhibitURL;

  const PRICING_INFORMATION: PricingInformation = new PricingInformation('New Contract', CONTRACT_TYPES.IFS,
  121, new Date('01/01/2017'), new Date('01/31/2017'), 'fiscal', false, false, false, false, '200', 21,
  '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308', new Date(1540924200000), new Date(1532370600000), null, 1);

  const CPP_INFORMATION_DETAILS: CPPInformationDetails = new CPPInformationDetails(4521, 1, 321);

  const CLM_CONTRACT_DETAILS: ClmContractDetails = new ClmContractDetails('TEST CONTRACT', 'ICMDistributionAgreementRegional',
    new Date('05/07/2019'), new Date('05/07/2020'), 'Draft', 'f7577793-0fb3-45f6-9710-ddb79514c24a', null, false, CPP_INFORMATION_DETAILS);

  let service: PricingInformationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        PricingInformationService,
      ]
    })
    service = TestBed.get(PricingInformationService)
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should save pricng information', () => {

    let actualResponse = null;
    const expected = PRICING_INFORMATION;

    service.savePricingInformation(PRICING_INFORMATION).subscribe((res) => {
      actualResponse = res
    });

    httpMock.expectOne((req: HttpRequest<PricingInformation>) => {
      const matchUrl = req.url === savePricingInformationURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch pricing information', () => {

    let actualResponse = null;
    const cppSeqId = '3750';

    service.fetchPricingInformation('3750', '75490bfc-289d-40d3-b644-ba095c2e1cea', 'DRAFT')
      .subscribe(response => {
        actualResponse = response
      })

    const expected = [PRICING_INFORMATION];

    httpMock.expectOne((req: HttpRequest<PricingInformation[]>) => {
      const matchUrl = req.url === fetchPricingInformationURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId
        && req.params.get('agreementId') === '75490bfc-289d-40d3-b644-ba095c2e1cea'
        && req.params.get('contractStatus') === 'DRAFT';

      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch ClmContractDetails', () => {

    let actualResponse = null;
    const agreementId = '5490bfc-289d-40d3-b644-ba095c2e1cea';
    const contractType = 'ICMDistributionAgreementRegional';

    service.fetchContractPriceProfileInfo(agreementId, contractType)
      .subscribe(response => {
        actualResponse = response
      })

    const expected = CLM_CONTRACT_DETAILS;

    httpMock.expectOne((req: HttpRequest<ClmContractDetails>) => {
      const matchUrl = req.url === fetchContractPriceProfileInfoURL;
      const matchParams = req.params.get('agreementId') === agreementId
        && req.params.get('contractType') === contractType;
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should get Contract Information', () => {

    let actualResponse = CLM_CONTRACT_DETAILS;

    service.getClmContractDetails().subscribe((res: ClmContractDetails) =>
      actualResponse = res
    );
    const expected = CLM_CONTRACT_DETAILS;
    expect(actualResponse).toEqual(expected);
  });

  it('should trigger service call for delete exhibit', () => {

    let actualResponse = null;

    service.deletePricingExhibit('1234').subscribe((res) => {
      actualResponse = res
    });
  const expected = {};

  httpMock.expectOne((req: HttpRequest<{}>) => {
    const matchUrl = req.url === deletePricingExhibitURL;
    const matchParams = req.params.get('agreementId') === '1234'
    return matchUrl && matchParams;
  }).flush(expected);

  expect(actualResponse).toEqual(expected);
  });

  it('should get Pricing Information Status', () => {

    let actualResponse = true;

    service.getPricingInformationStatus().subscribe((res: boolean) =>
      actualResponse = res
    );
    const expected = true;
    expect(actualResponse).toEqual(expected);
  });

})
