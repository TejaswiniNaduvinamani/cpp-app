import { Injector } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { ToastrService, ToastrModule } from 'ngx-toastr';
import { ToasterService } from './toaster.service';
import { TranslatorService } from './../translate/translator.service';

describe('Toaster Service', () => {
   let mockTranslateService  = jasmine.createSpyObj<TranslatorService>('translateService', ['translate', 'translateWithValue']);
   
   let component: ToasterService;
   let inj: Injector;
   let toastrService: ToastrService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                ToastrModule.forRoot()
            ],
             providers: [
                 { provide: TranslatorService, useValue: mockTranslateService },
                 ToasterService            
                ]
            }).compileComponents();

            component = TestBed.get(ToasterService);
            inj = TestBed.get(Injector);
            toastrService = TestBed.get(ToastrService);    
    });

    it('should show success', () => {
        // GIVEN
        spyOn(toastrService, 'success');

        // WHEN
        component.showSuccess('it was successful', 'success msg');

        // THEN
        expect(toastrService.success).toHaveBeenCalledWith('it was successful', 'success msg', {
            positionClass: 'toast-top-full-width',
            enableHtml: true,
            timeOut: 5000,
            closeButton: true
          });
    });

    it('should show notification', () => {
        // GIVEN
        spyOn(toastrService, 'info');

        // WHEN
        component.showNotification('it was successful', 'success msg');

        // THEN
        expect(toastrService.info).toHaveBeenCalledWith('it was successful', 'success msg', {
            positionClass: 'toast-top-full-width',
            enableHtml: true,
            timeOut: 5000,
            closeButton: true
        });
    });

    it('should show error if error response contains message', () => {
        // GIVEN
        let errorResponse = {
            error : {
                errorMessage : 'error message'
            }
        };
        spyOn(toastrService, 'error');

        // WHEN
        component.showError(errorResponse);

        // THEN
        expect(toastrService.error).toHaveBeenCalled();
    });

    it('should show error if error response does not contain message', () => {
        // GIVEN
        let errorResponse = {};
        spyOn(toastrService, 'error');

        // WHEN
        component.showError(errorResponse);

        // THEN
        expect(toastrService.error).toHaveBeenCalled();
    });
    
});
