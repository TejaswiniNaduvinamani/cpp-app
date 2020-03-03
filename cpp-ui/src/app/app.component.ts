import { Component } from '@angular/core';

import { TranslateConfig } from './shared';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  template: `
        <app-header></app-header>
          <router-outlet></router-outlet>

  `
})
export class AppComponent {
  constructor(private _translate: TranslateService) {
    TranslateConfig.configureTranslation(_translate);
  }
}
