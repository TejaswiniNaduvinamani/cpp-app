import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';

import { ErrorService } from './../../shared';

@Component({
  selector: 'app-error-screen',
  templateUrl: './error-screen.component.html',
  styleUrls: ['./error-screen.component.scss']
})
export class ErrorScreenComponent implements OnInit, OnDestroy {

  public errorType: string;

  constructor(
    private _route: ActivatedRoute,
    private _errorService: ErrorService,
  ) { }

  ngOnInit() {
    this._errorService.checkErrorStatus(true);
    this.errorType = this._route.snapshot.queryParams['errorType'];
  }

  ngOnDestroy() {
    this._errorService.checkErrorStatus(false);
  }

}
