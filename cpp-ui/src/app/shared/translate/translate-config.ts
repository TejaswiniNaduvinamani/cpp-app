import { TranslateService } from '@ngx-translate/core';

const DEFAULT_LANGUAGE = 'en';

export class TranslateConfig {
  public static configureTranslation(translate: TranslateService) {
      translate.addLangs(['en', 'fr', 'en-CA', 'fr-CA']);
      translate.setDefaultLang(DEFAULT_LANGUAGE);
      return TranslateConfig.loadEnglishAndUserLanguage(translate);
  }

  private static loadEnglishAndUserLanguage(translate) {
      const callback = () => TranslateConfig.loadUserLanguage(translate);

      return translate.use(DEFAULT_LANGUAGE).subscribe(null, callback, callback);
  }

  private static loadUserLanguage(translate) {
      const userLanguage = TranslateConfig.getUserLanguage(translate);
      if (userLanguage !== DEFAULT_LANGUAGE) {
          translate.use(userLanguage);
      }
  }

  private static getUserLanguage(translate): string {
      const browserLanguage = TranslateConfig.getBrowserLanguage(translate);

      return TranslateConfig.languageIsSupported(browserLanguage) ? browserLanguage : DEFAULT_LANGUAGE;
  }

  private static languageIsSupported(language: string) {
      return language && language.match(/^(en|fr|en-CA|fr-CA)$/);
  }

  private static getBrowserLanguage(translate) {
    const browserCultureLanguage = translate.getBrowserCultureLang();

      return TranslateConfig.languageIsSupported(browserCultureLanguage) ? browserCultureLanguage : translate.getBrowserLang();
  }
}
