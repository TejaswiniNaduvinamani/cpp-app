import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { AuthorizationService, ErrorService, ContractSearchService, PricingInformationService } from './../../shared';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  public isErrorPage = false;
  public isContractSearchPage = false;
  public isPricingInformationSaved: boolean;
  private subscription: Subscription[] = [];

  constructor(
    private _errorService: ErrorService,
    private cdr: ChangeDetectorRef,
    private _contractSerachService: ContractSearchService,
    private _pricingInformationService: PricingInformationService
  ) { }

  ngOnInit() {
    this.subscription.push(this._errorService.isErrorPage$.subscribe(
      errorStatus => {
        this.isErrorPage = errorStatus;
        this.cdr.detectChanges();
    }));
    this.checkForSearchPage();
    this.checkIfPricingInformationSaved();
  }

  checkForSearchPage() {
    this.subscription.push(this._contractSerachService.isContractSearchPage$.subscribe(
      contractSearchPage => {
        this.isContractSearchPage = contractSearchPage;
        this.cdr.detectChanges();
      }));
  }

  disableForSearchPage($event) {
    if (this.isContractSearchPage) {
      $event.preventDefault();
    }
  }

  checkIfPricingInformationSaved() {
    this.subscription.push(this._pricingInformationService.getPricingInformationStatus().subscribe(
      isPricingInformationSaved => {
        this.isPricingInformationSaved = isPricingInformationSaved;
        this.cdr.detectChanges();
      }));
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
    this.cdr.detach();
  }

}
