import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';
import { Router, ActivatedRoute } from '@angular/router';

import { ActivatedRouteStub } from './../../../../test/unit-testing/mock/activated-route-stub';
import { SessionStorageInterface } from './../contract-information/contract-information.model';
import { DistributionCentersComponent } from './distribution-centers.component';
import { DeleteExhibitComponent } from 'app/contract';
import { DistributionCenterService, StepperService, AuthorizationService, AuthorizationDetails,
   PricingInformationService, TranslatorService, ToasterService } from './../../shared';
import { DistributionCenters, SelectedDistributionCenters, DistributionCenterList } from './distribution-center.model';
import { RouterStub } from './../../../../test/unit-testing/mock/router-stub';

describe('Distribution-Centers-Component', () => {

  const DISTRIBUTION_CENTERS: Array<DistributionCenters> = [
    new DistributionCenters('1', '50th St. Distribution Ctr', '50TH ST - GR'),
    new DistributionCenters('5', 'Brighton Distribution Ctr', 'BRIGHTON')
  ];

  const SELECTED_DC: Array<SelectedDistributionCenters> = [
    new SelectedDistributionCenters('560', '5', new Date('2018-02-01'), new Date('2018-01-20'))
  ];

  // do not remove double quotes in SessionStorageInterface. Setting of session requires double quotes while parsing the string.
  const CONTRACT_DETAILS: SessionStorageInterface = {
    "agreementId": '75490bfc-289d-40d3-b644-ba095c2e1cea',
    "cenddate": new Date('12/12/2020'),
    "cname": 'TEST CONTRACT',
    "contractStatus": 'draft',
    "contractType": 'ICMDistributionAgreementRegional',
    "cppid": 12345,
    "cppseqid": 42915,
    "cstdate": new Date('12/12/2019'),
    "ctype": 'ICMDistributionAgreementRegional',
    "isAmendment": false,
    "isFurtheranceMode": true,
    "isPricingExhibitAttached": true,
    "penddate": new Date('12/12/2020'),
    "pstdate": new Date('12/12/2019'),
    "versionNumber": 1
   }

  const AUTH_DETAILS: AuthorizationDetails = new AuthorizationDetails(true, true, true, true, 'draft')


  let component: DistributionCentersComponent;
  let fixture: ComponentFixture<DistributionCentersComponent>;
  let dcService: DistributionCenterService;
  let authService: AuthorizationService;
  let mockTranslateService;
  let mockToasterService;

  beforeEach(() => {
    mockTranslateService  = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError', 'showNotification']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DistributionCentersComponent],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        { provide: ToasterService, useValue: mockToasterService },
        { provide: TranslatorService, useValue: mockTranslateService },
        StepperService,
        DistributionCenterService,
        AuthorizationService,
        PricingInformationService,
        DeleteExhibitComponent
      ]
    }).overrideTemplate(DistributionCentersComponent, '');

    fixture = TestBed.createComponent(DistributionCentersComponent);
    component = fixture.componentInstance;
    dcService = TestBed.get(DistributionCenterService);
    authService = TestBed.get(AuthorizationService);
  });


  it('should create the distribution center component', () => {
    expect(component).toBeTruthy();
  });

  it('should load distribution centers from server', () => {
     // GIVEN
    sessionStorage.setItem('contractInfo', '{"isFurtheranceMode":true}');
    spyOn(authService, 'fetchAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(authService, 'setAuthorizationDetails').and.returnValue(Observable.of([]));
    spyOn(authService, 'getAuthorizationDetails').and.returnValue(Observable.of(AUTH_DETAILS));
    spyOn(dcService, 'fetchDistributionCenters').and.returnValue(Observable.of([DISTRIBUTION_CENTERS]));
    component.initializeState();
    spyOn(dcService, 'fetchSelectedDistributionCenters').and.returnValue(Observable.of([SELECTED_DC]));

     // WHEN
    fixture.detectChanges();

     // THEN
    expect(component.distributionCenters.length).toBe(1);
    expect(component.showSpinner).toBeFalsy();
  });

  it('should initialize all distribution centers as unchecked', () => {
    // GIVEN
    component.distributionCenters = DISTRIBUTION_CENTERS;
    component.isActive = [];

    // WHEN
    component.initializeState();

    // THEN
    expect(component.isActive).toContain(false);
  });

  it('should select all Distribution Centers', () => {
    // GIVEN
    component.distributionCenters = DISTRIBUTION_CENTERS;
    component.isActive = [false, false];

    // WHEN
    component.selectALL();

    // THEN
    expect(component.isActive).toContain(true);
  });

  it('should clear all Distribution Centers', () => {
    // GIVEN
    component.distributionCenters = DISTRIBUTION_CENTERS;
    component.isActive = [true, true];

    // WHEN
    component.clear();

    // THEN
    expect(component.isActive).toContain(false);
  });

  it('should test ngDoCheck', () => {
    // GIVEN
    component.distributionCenters = DISTRIBUTION_CENTERS;
    component.isActive = [true, true];

    // WHEN
    component.ngDoCheck();

    // THEN
    expect(component.validDistributionCenterList).toEqual(true);
  });

  it('should build Distribution Center Object', () => {
    // GIVEN
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
    component.selectedDistributionCenters = ['1', '2'];

    // WHEN
    component.buildDistributionCenters(component.selectedDistributionCenters);

    // THEN
    expect(component.buildDistributionCenters).toBeTruthy();
  });


  it('should save selected distribution center', () => {
     // GIVEN
    component.selectedDistributionCenters = ['1', '2'];
    component.isActive = [true, false]
    spyOn(dcService, 'saveDistributionCenters').and.returnValue(Observable.of([DISTRIBUTION_CENTERS]));

     // WHEN
    component.saveDistributionCenters(component.selectedDistributionCenters);

     // THEN
    expect(component.selectedDistributionCenters.length).toBe(2);
    expect(component.showSpinner).toBeFalsy();
  });

  it('should save none distribution center', () => {
     // GIVEN
    let dcform = {
      value : {0: false, 1: false}
    };
    component.isActive = [false, false]
    spyOn(dcService, 'saveDistributionCenters').and.returnValue(Observable.of([DISTRIBUTION_CENTERS]));

     // WHEN
    component.saveDistributionCenters(dcform);

     // THEN
    expect(component.selectedDistributionCenters.length).toBe(0);
  });

  it('should disable checkboxes in view mode', () => {
    // GIVEN
    component.displayViewMode = true;
    const event = jasmine.createSpyObj('e', [ 'preventDefault' ]);

    // WHEN
    component.viewModeClick(event);

    // THEN
    expect(event.preventDefault).toHaveBeenCalled();
  });

  it('should check submit button is grayed out', () => {
    // GIVEN
    component.isActive = [true, true];

    // WHEN
    component.ngDoCheck();

    // THEN
    expect(component.saveGrayed).toBe(true);
  });

  it('should check attached pricing exhibit', () => {
    // GIVEN
    component.isPricingExhibitAttached = false;
    let dcform = {
      value : {0: false, 1: false}
    };
    component.isActive = [true, false];
    spyOn(dcService, 'saveDistributionCenters').and.returnValue(Observable.of([DISTRIBUTION_CENTERS]));

    // WHEN
    component.checkAttachedPricingExhibit(dcform);

    // THEN
    expect(dcService.saveDistributionCenters).toHaveBeenCalled();

  });

  it('should check attached pricing exhibit if pricing exhibit attached is true', () => {
    // GIVEN
    component.isPricingExhibitAttached = true;
    let dcform = {
      value : {0: false, 1: false}
    };

    // WHEN
    component.checkAttachedPricingExhibit(dcform);

  });

  it('should delete exhibit on edit of distribution centers', () => {
    // WHEN
    component.onExhibitDeletion(false);

    // THEN
    expect(component.showSpinner).toBeTruthy();
  });

  it('should save distribution centers on exhibit deletion', () => {
    // GIVEN
    component.isActive = [true, false];
    spyOn(dcService, 'saveDistributionCenters').and.returnValue(Observable.of([DISTRIBUTION_CENTERS]));

    // WHEN
    component.onExhibitDeletion(true);

    // THEN
    expect(dcService.saveDistributionCenters).toHaveBeenCalled();

  })

})
