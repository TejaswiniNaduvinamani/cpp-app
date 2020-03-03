import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { AuthorizationService, PricingInformationService } from '../shared';

@Component({
  selector: 'app-assignment-header',
  templateUrl: './assignment-header.component.html',
  styleUrls: ['./assignment-header.component.scss']
})
export class AssignmentHeaderComponent implements OnInit, OnDestroy {

  public contractPriceProfileId: string;
  public versionNumber: string;
  public contractInfoInd = true;
  private subscription: Subscription[] = [];
  public cppStatus: string;

  constructor(
    private _authorizationService: AuthorizationService,
    private _pricingInformationService: PricingInformationService
  ) { }

  ngOnInit() {
    this._pricingInformationService.setPricingInformationStatus(true);
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractPriceProfileId = contractDetails.cppid;
    this.versionNumber = contractDetails.versionNumber;
    this.subscription.push(this._authorizationService.getAuthorizationDetails().subscribe(authorizationInformation => {
      this.cppStatus = authorizationInformation.cppStatus;
      const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
      contractInfo.cppStatus = this.cppStatus;
      sessionStorage.setItem('contractInfo', JSON.stringify(contractInfo));
    }));
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}
