import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router, ActivatedRoute, Params } from '@angular/router';

import { ActivatedRouteStub } from './../../../../test/unit-testing/mock/activated-route-stub';
import { ErrorScreenComponent } from './error-screen.component';
import { TranslatorService, ErrorService } from './../../shared';

describe('ErrorScreenComponent', () => {
  let component: ErrorScreenComponent;
  let fixture: ComponentFixture<ErrorScreenComponent>;
  let mockTranslateService;
  let route: ActivatedRoute;

  beforeEach(() => {
    mockTranslateService = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ ErrorScreenComponent ],
      providers: [
        { provide: ActivatedRoute, useClass: ActivatedRouteStub },
        {provide: TranslatorService, useValue: mockTranslateService},
        ErrorService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParams: {
                subscribe: (fn: (value: Params) => void) => fn({
                  errorType: 'ACCESS_DENIED',
                }),
              },
            },
          },
        },
      ]
    }).overrideTemplate(ErrorScreenComponent, '');

    fixture = TestBed.createComponent(ErrorScreenComponent);
    component = fixture.componentInstance;
    route = TestBed.get(ActivatedRoute);
  });

  it('should create error screen component', () => {
    // GIVEN
    fixture.detectChanges();

    // THEN
    expect(component).toBeTruthy();
  });
});
