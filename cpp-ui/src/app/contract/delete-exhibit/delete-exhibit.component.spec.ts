import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable } from 'rxjs/Rx';

import { ActivatedRouteStub } from '../../../../test/unit-testing/mock/activated-route-stub';
import { PricingInformationService, TranslatorService, ToasterService } from './../../shared';
import { DeleteExhibitComponent } from './delete-exhibit.component';
import { Router, ActivatedRoute } from '@angular/router';
import { RouterStub } from '../../../../test/unit-testing/mock/router-stub';


describe('DeleteExhibitComponent', () => {
  let component: DeleteExhibitComponent;
  let fixture: ComponentFixture<DeleteExhibitComponent>;
  let mockTranslateService;
  let mockToasterService;
  let pricingInformationService: PricingInformationService;

  beforeEach(() => {
    mockTranslateService  = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError', 'showNotification']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ DeleteExhibitComponent ],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        { provide: ToasterService, useValue: mockToasterService },
        { provide: TranslatorService, useValue: mockTranslateService },
        PricingInformationService
      ]
    }).overrideTemplate(DeleteExhibitComponent, '');

    fixture = TestBed.createComponent(DeleteExhibitComponent);
    component = fixture.componentInstance;
    pricingInformationService = TestBed.get(PricingInformationService);
    fixture.detectChanges();
  });

  it('should create delete exhibit component', () => {
    expect(component).toBeTruthy();
  });

  it('should delete exhibit component', () => {
    // GIVEN
    component.agreementId = '123';
    spyOn(pricingInformationService, 'deletePricingExhibit').and.returnValue(Observable.of({}));
    spyOn(component.isExhibitDeleted, 'emit');
    // WHEN
    component.deletePricingExhibit();

    // THEN
    expect(pricingInformationService.deletePricingExhibit).toHaveBeenCalled();
    expect(component.isExhibitDeleted.emit).toHaveBeenCalledWith(true);
  });
});
