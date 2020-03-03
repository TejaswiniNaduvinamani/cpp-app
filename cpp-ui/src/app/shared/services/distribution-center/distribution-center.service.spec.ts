import { TestBed } from '@angular/core/testing';
import { DistributionCenters, SelectedDistributionCenters } from 'app/contract/distribution-centers/distribution-center.model';
import { DistributionCenterService } from './distribution-center.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpRequest } from '@angular/common/http';

import { apiUrls } from './../app.url';

describe('Distribution Center Service', () => {

  const distributionCenterURL = apiUrls.distributionCenterURL;
  const distributionCentersFetchURL = apiUrls.distributionCentersFetchURL;
  const distributionCentersSaveURL = apiUrls.distributionCenterSaveURL;
  const DISTRIBUTION_CENTERS: Array<DistributionCenters> = [
    new DistributionCenters('1', '50th St. Distribution Ctr', '50TH ST - GR'),
    new DistributionCenters('5', 'Brighton Distribution Ctr', 'BRIGHTON')
  ];
  const SELECTED_DISTRIBUTION_CENTERS: Array<SelectedDistributionCenters> = [
    new SelectedDistributionCenters('12', '123', new Date('2018-01-20'), new Date('2018-01-16'))
  ];

  let service: DistributionCenterService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [DistributionCenterService]
    });

    service = TestBed.get(DistributionCenterService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should get the distribution center list', () => {

    let actualResponse = null;

    service.fetchDistributionCenters()
      .subscribe((response) =>
        actualResponse = response);

    const expected = [DISTRIBUTION_CENTERS];
    httpMock.expectOne((req: HttpRequest<any>) => {
      const matchUrl = req.url === distributionCenterURL;
      return matchUrl;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should get already selected distribution center', () => {

    let actualResponse = null;
    const cppSeqId = '123';

    service.fetchSelectedDistributionCenters(cppSeqId)
      .subscribe((response: SelectedDistributionCenters[]) =>
        actualResponse = response);

    const expected = [SELECTED_DISTRIBUTION_CENTERS];

    httpMock.expectOne((req: HttpRequest<any>) => {
      const matchUrl = req.url === distributionCentersFetchURL;
      const matchParams = req.params.get('contractPriceProfileSeq') === cppSeqId;
      return matchUrl && matchParams;
    }).flush(expected);

    expect(actualResponse).toEqual(expected);
  });

  it('should call service for saving selected distribution center', () => {
    service.saveDistributionCenters(DISTRIBUTION_CENTERS).subscribe(() => { });

    httpMock.expectOne({ url: distributionCentersSaveURL, method: 'POST' });
  });


});
