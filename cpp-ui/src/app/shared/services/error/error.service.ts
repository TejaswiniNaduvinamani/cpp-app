import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class ErrorService {

  private isErrorPage = new Subject<boolean>();
  isErrorPage$ = this.isErrorPage.asObservable();

  checkErrorStatus(data) {
    this.isErrorPage.next(data);
  }

}
