import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { PricingInformationService, TranslatorService, ToasterService } from 'app/shared';
declare var $;

@Component({
  selector: 'app-delete-exhibit',
  templateUrl: './delete-exhibit.component.html',
  styleUrls: ['./delete-exhibit.component.scss']
})
export class DeleteExhibitComponent implements OnInit, OnDestroy {

  @ViewChild('deletePricingExhibitBtn') public deletePricingExhibitBtn: ElementRef;

  public agreementId: string;
  private subscription: Subscription[] = [];
  public showSpinner: boolean;
  @Output() isExhibitDeleted = new EventEmitter<boolean>();

  constructor(private _pricingInformationService: PricingInformationService,
    private _translateService: TranslatorService,
    private _toasterService: ToasterService) { }

  ngOnInit() {
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.agreementId = contractInfo.agreementId;
    $('#deletePricingExhibit').on('shown.bs.modal', () => {
      this.deletePricingExhibitBtn.nativeElement.focus();
      }); 
  }

  deletePricingExhibit() {
    this.isExhibitDeleted.emit(false);
    const deleteExhibitSuccessMsg = this._translateService.translate('TOASTER_MESSAGES.DELETE_PRICING_EXHIBIT_SUCCESS_MSG');
    this.subscription.push(this._pricingInformationService.deletePricingExhibit(this.agreementId)
    .subscribe(() => {
      const contractInformation = JSON.parse(sessionStorage.getItem('contractInfo'));
      contractInformation.isPricingExhibitAttached = false;
      sessionStorage.setItem('contractInfo', JSON.stringify(contractInformation));
      this.isExhibitDeleted.emit(true);
      this.showSpinner = false;
      this._toasterService.showNotification(deleteExhibitSuccessMsg, '');
     }, error => {
      this.isExhibitDeleted.emit(true);
    }));
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}
