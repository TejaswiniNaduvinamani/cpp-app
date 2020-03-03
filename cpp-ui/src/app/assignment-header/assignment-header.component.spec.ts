import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Observable';

import { CONTRACT_TYPES } from './../shared/utils/app.constants';

import { AssignmentHeaderComponent } from './assignment-header.component';
import { ContractInformationDetails } from 'app/contract/contract-information/contract-information.model';
import { AuthorizationService, PricingInformationService } from '../shared';

describe('AssignmentHeaderComponent', () => {

  const CONTRACT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    CONTRACT_TYPES.IFS, new Date('01/26/2020'), new Date('01/30/2020'),
    'Draft', new Date('01/26/2020'), new Date('01/30/2020'), '123', 'edit', '2');

  let component: AssignmentHeaderComponent;
  let fixture: ComponentFixture<AssignmentHeaderComponent>;
  let authorizationService: AuthorizationService;
  let pricingInformationService: PricingInformationService;

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ AssignmentHeaderComponent ],
      providers: [
        AuthorizationService,
        PricingInformationService
      ]
    }).overrideTemplate(AssignmentHeaderComponent, '');

    fixture = TestBed.createComponent(AssignmentHeaderComponent);
    component = fixture.componentInstance;
    authorizationService = TestBed.get(AuthorizationService);
    pricingInformationService = TestBed.get(PricingInformationService);

    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should create Assignment-Header Component', () => {
    expect(component).toBeTruthy();
  });

  it('should check the contractPriceProfileId to be same as in session storage', () => {

     // GIVEN
     component.contractPriceProfileId = '123';
     component.versionNumber = '2';

     // WHEN
     fixture.detectChanges()

     // THEN
     expect(pricingInformationService.setPricingInformationStatus).toBeTruthy();
     expect(fixture.detectChanges).toBeTruthy();

  });

  it('should fetch cpp status from Authorization Service on page load', () => {
    // GIVEN
    spyOn(authorizationService, 'getAuthorizationDetails').and.returnValue(Observable.of({'cppStatus': 'DRAFT'}));

    // WHEN
    fixture.detectChanges()

    // THEN
    expect(component.cppStatus).toBe('DRAFT');
  });

});
