import { ActivatedRouteStub } from '../../../../test/unit-testing/mock/activated-route-stub';
import { CppHttpInterceptor, TranslatorService, ToasterService, AssignmentsService } from 'app/shared';
import { HttpClientTestingModule,  HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterStub } from '../../../../test/unit-testing/mock/router-stub';
import { Router, ActivatedRoute } from '@angular/router';
import { TestBed } from '@angular/core/testing';

describe('cppHttpInterceptors', () => {
    let httpMock: HttpTestingController;
    let mockTranslateService;
    let mockToasterService;
    let router: RouterStub;
    let route: ActivatedRouteStub;
    let service: AssignmentsService;

  beforeEach(() => {
    mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
    mockToasterService = jasmine.createSpyObj<ToasterService>('toasterService', ['showSuccess', 'showError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        CppHttpInterceptor,
        AssignmentsService,
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        { provide: Router, useClass: RouterStub },
        { provide: TranslatorService, useValue: mockTranslateService },
        { provide: ToasterService, useValue: mockToasterService },       
        {
          provide: HTTP_INTERCEPTORS,
          useClass: CppHttpInterceptor,
          multi: true,
        },
      ],
    });    
    httpMock = TestBed.get(HttpTestingController);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
    service = TestBed.get(AssignmentsService);
  });

  it('should intercept and handle response', () => {
      service.fetchCustomerType().subscribe(response => {
          expect(response).toBeTruthy();
      });     
  });
});

