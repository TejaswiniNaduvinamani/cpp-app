import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpRequest } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { apiUrls } from 'app/shared';
import { CostModelGridDetails, CostModelDTO } from 'app/cost-model';
import { CostModelService } from './cost-model.service';

describe('CostModelService', () => {

  const fetchCostGridURL = apiUrls.fetchCostGridURL;
  const fetchCostModelListURL = apiUrls.fetchCostModelListURL;
  const saveCostModelGridURL = apiUrls.saveCostModelGridURL;

  const COST_MODEL_GRID_DETAILS: CostModelGridDetails = new CostModelGridDetails('Grocery', 3, '123', 45, '31', 34, 123, 45);

  const COST_MODEL: CostModelDTO = new CostModelDTO(23, '70-Last Received Cost Plus');

  let costModelService: CostModelService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CostModelService]
    });
    costModelService = TestBed.get(CostModelService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should fetch costModelGrid Details', () => {

    let actualResponse = null;
    const cppSeqId = '123';

    costModelService.fetchCostGridDetails('123')
        .subscribe(response => {
          actualResponse = response
    });

    const expected = [COST_MODEL_GRID_DETAILS];

    httpMock.expectOne((req: HttpRequest<CostModelGridDetails[]>) => {
      const matchUrl = req.url === fetchCostGridURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId;
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should fetch costModelList', () => {

    let actualResponse = null;
    const expected = [COST_MODEL];

    costModelService.fetchCostModelList()
        .subscribe(response => {
          actualResponse = response
    });

    httpMock.expectOne((req: HttpRequest<CostModelDTO[]>) => {
      const matchUrl = req.url === fetchCostModelListURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should save costModelGrid Details', () => {
    let actualResponse = null;
    const expected = COST_MODEL_GRID_DETAILS;
    costModelService.saveCostModel([COST_MODEL_GRID_DETAILS]).subscribe((res) => {
        actualResponse = res
    });

    httpMock.expectOne((req: HttpRequest<CostModelGridDetails[]>) => {
      const matchUrl = req.url === saveCostModelGridURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

});
