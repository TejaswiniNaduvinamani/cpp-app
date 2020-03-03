import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { ERROR_TYPES } from './../utils/app.constants';
import { ToasterService } from './../toaster/toaster.service';

@Injectable()
export class CppHttpInterceptor implements HttpInterceptor {
  constructor(
    private toaster: ToasterService,
    private _router: Router,
    private _route: ActivatedRoute
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req.clone({})).do(
      (httpEvent: HttpEvent<any>) => {
      },
      (errorResponse: any) => {
        if( errorResponse.error.errorType in ERROR_TYPES ){
            this._router.navigate(['/error'], { relativeTo: this._route, queryParams: { errorType: errorResponse.error.errorType }});
        } else {
          this.toaster.showError(errorResponse);
        }
      }).catch(this.handleError);
  }

  private handleError(error: HttpErrorResponse) {
    return Observable.throw(error);
  }

}
