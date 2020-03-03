import { Injectable, Injector } from '@angular/core';

import { ToastrService } from 'ngx-toastr';

import { TranslatorService } from './../translate/translator.service';

@Injectable()
export class ToasterService {
  translatorService: TranslatorService;
  constructor(private toastr: ToastrService, private inj: Injector) { }

  showSuccess(message: string, title: string) {
    this.toastr.success(message, title, {
      positionClass: 'toast-top-full-width',
      enableHtml: true,
      timeOut: 5000,
      closeButton: true
    });
  }

  showNotification(message: string, title: string) {
    this.toastr.info(message, title, {
      positionClass: 'toast-top-full-width',
      enableHtml: true,
      timeOut: 5000,
      closeButton: true
    });
  }

  showError(errorResponse: any) {
    let exceptionMessage;
    this.translatorService = this.inj.get(TranslatorService);
    if (errorResponse && errorResponse.error && errorResponse.error.errorMessage) {
      exceptionMessage = errorResponse.error.errorMessage;
    } else {
      exceptionMessage = this.translatorService.translate('COMMON.UNEXPECTED_EXCEPTION');
    }
    const message = this.translatorService.translate('COMMON.ERROR')
      + exceptionMessage
      + this.translatorService.translate('COMMON.CONTACT_SUPPORT');
    this.toastr.error(message, '', {
      positionClass: 'toast-top-full-width',
      tapToDismiss: false,
      enableHtml: true,
      extendedTimeOut: 0,
      timeOut: 0,
      closeButton: true
    });
  }

}
