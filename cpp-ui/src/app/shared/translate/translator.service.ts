import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class TranslatorService {

    translatedMessage: string;

    constructor(private translateService: TranslateService) {
    }

    translate(translationStringKey: string): string {
        this.translateService.get(translationStringKey).subscribe((res: string) => {
            this.translatedMessage = res;
        });
        return this.translatedMessage;
    }

    translateWithValue(translationStringKey: string, value: string): string {
        this.translateService.get(translationStringKey, {'value': value}).subscribe((res: string) => {
            this.translatedMessage = res;
        });
        return this.translatedMessage;
    }
}
