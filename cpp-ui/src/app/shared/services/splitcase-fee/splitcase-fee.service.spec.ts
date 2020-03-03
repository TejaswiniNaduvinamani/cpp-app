import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { HttpRequest } from '@angular/common/http';

import { SplitCaseFeeService } from './splitcase-fee.service';
import { SplitCaseList, SplitCaseFees } from './../../../contract/split-case/split-case-fee.model';
import { apiUrls } from './../app.url';


describe('SplitCase Fee Service', () => {

  const fetchSplitcaseFeeURL = apiUrls.fetchSplitcaseFeeURL;
  const splitcaseFeeSaveURL = apiUrls.splitCaseSaveURL;

  const SPLITCASE_VALUES: Array<SplitCaseFees> =  [
    new SplitCaseFees('Grocery', 10, '$', new Date('2019/02/20'), new Date('2019/02/28'), '12', false, false)
  ];
  const SPLIT_CASE_LIST = {
    'contractPriceProfileSeq': 123,
    'splitCaseFeeValues': SPLITCASE_VALUES
  }


  let service: SplitCaseFeeService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SplitCaseFeeService]

    });

    service = TestBed.get(SplitCaseFeeService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should get the product types', () => {

    let actualResponse = null;

    service.fetchProductTypes('123', new Date('2019/02/20'), new Date('2019/02/26'))
      .subscribe(res => {
        actualResponse = res});

    const expected = SPLIT_CASE_LIST;

    httpMock.expectOne((req: HttpRequest<SplitCaseFees>) => {
      const matchUrl = req.url === fetchSplitcaseFeeURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);

  });

  it('should save splitcase fees', () => {

    service.saveSplitCaseFee(SPLIT_CASE_LIST)
      .subscribe( () => { });

    httpMock.expectOne({ url: splitcaseFeeSaveURL, method: 'POST' });
  })

});
