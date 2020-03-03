import { Observable } from 'rxjs/Rx';
import { TestBed } from '@angular/core/testing';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { TranslatorService } from './translator.service';

describe('Translator Service', () => {
    let component: TranslatorService;
    let translateService: TranslateService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                TranslateModule.forRoot()
            ],
             providers: [
                TranslatorService,
                TranslateService            
                ]
            }).compileComponents();

            component = TestBed.get(TranslatorService);
            translateService = TestBed.get(TranslateService);
    });

    it('should translate and return translated message', () => {
        // GIVEN
        const translationStringKey = 'key';
        spyOn(translateService, 'get').and.returnValue(Observable.of('key'));

        // WHEN
        component.translate(translationStringKey);

        // THEN
        expect(translateService.get).toHaveBeenCalled();
    });

    it('should translate with value and return translated message', () => {
        // GIVEN
        const translationStringKey = 'key';
        const value = 'value';
        spyOn(translateService, 'get').and.returnValue(Observable.of('key'));

        // WHEN
        component.translateWithValue(translationStringKey, value);

        // THEN
        expect(translateService.get).toHaveBeenCalled();
    });
})
