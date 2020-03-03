import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit, Input, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import * as moment from 'moment';

import { CONTRACT_TYPES, CONTRACT_TYPE_DISPLAY_NAMES, ERROR_TYPES } from './../../shared/utils/app.constants';
import { ContractInformationDetails, ClmContractDetails } from './contract-information.model';
import { PricingInformationService } from './../../shared';
import { StepperService } from '../../shared';

@Component({
  selector: 'app-contract-information',
  templateUrl: './contract-information.component.html',
  styleUrls: ['./contract-information.component.scss']
})
export class ContractInformationComponent implements OnInit, OnDestroy {

  @Input() public contractInfoInd;

  public contractName: string;
  public contractType: string;
  public contractStartDate: Date;
  public contractEndDate: Date;
  public priceStartDate: Date;
  public furtheranceEffectiveDate: Date;
  public isFurtheranceMode: boolean;
  public priceEndDate: Date;
  public cppId: number;
  public mode: string;
  public agreementId: string;
  public contractTypeDisplayName: string;
  public isAmendment: boolean;
  private _subscription: Subscription[] = [];

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private cdr: ChangeDetectorRef,
    private _stepperService: StepperService,
    private _pricingInformationService: PricingInformationService,
  ) { }

  ngOnInit() {
    this.agreementId = this._route.snapshot.queryParams['agreementId'];
    this.contractType = this._route.snapshot.queryParams['contractType'];
    if (!this.agreementId && sessionStorage.length === 0) {
      this._router.navigate(['/error'], { relativeTo: this._route, queryParams: { errorType: ERROR_TYPES.RETURN_TO_CLM }});
    } else {
    this._stepperService.getCurrentStep().subscribe(stepNumber => {
      if (stepNumber > 1) {
        this.contractInfoInd = true;
        this.fetchPricingDates();
        this._pricingInformationService.setPricingInformationStatus(true);
      } else {
        this.contractInfoInd = false;
      }
    });
    this.fetchContractDetails();
    }
  }

  fetchContractDetails() {
    if (this.agreementId && this.contractType) {
      this._subscription.push(this._pricingInformationService.getClmContractDetails().subscribe(clmContractDetails => {
        this.updateContractInformation(clmContractDetails);
      }));
    } else {
      this.fetchAgreementInfo();
      this._subscription.push(this._pricingInformationService.fetchContractPriceProfileInfo(this.agreementId.trim(),
       this.contractType.trim())
        .subscribe(clmContractDetails => {
          this.updateContractInformation(clmContractDetails);
        }));
    }
  }

  setContractInformationDetails(clmContractDetails: ClmContractDetails) {
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    contractInfo.cname = clmContractDetails.contractName;
    contractInfo.cstdate = moment.utc(clmContractDetails.contractStartDate);
    contractInfo.cenddate = moment.utc(clmContractDetails.contractEndDate);
    contractInfo.ctype = clmContractDetails.contractType;
    contractInfo.contractStatus = clmContractDetails.contractStatus;
    contractInfo.isAmendment = clmContractDetails.isAmendment;
    sessionStorage.setItem('contractInfo', JSON.stringify(contractInfo));
  }

  fetchAgreementInfo() {
    const agreementInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.agreementId = agreementInfo.agreementId;
    this.contractType = agreementInfo.contractType;
  }

  updateContractInformation(clmContractDetails: ClmContractDetails) {
    this.setContractInformationDetails(clmContractDetails);
    this.contractName = clmContractDetails.contractName;
    this.contractType = clmContractDetails.contractType;
    this.isAmendment = clmContractDetails.isAmendment;
    this.contractStartDate = clmContractDetails.contractStartDate;
    this.contractEndDate = clmContractDetails.contractEndDate;
    this.determineContractName();
    this.fetchPricingDates();
    this.cdr.detectChanges();
  }

  determineContractName() {
    if (this.contractType === CONTRACT_TYPES.DAN || this.contractType === CONTRACT_TYPES.DAN_AMENDMENT) {
      this.contractTypeDisplayName = CONTRACT_TYPE_DISPLAY_NAMES.DAN
    } else if (this.contractType === CONTRACT_TYPES.DAR || this.contractType === CONTRACT_TYPES.DAR_AMENDMENT) {
      this.contractTypeDisplayName = CONTRACT_TYPE_DISPLAY_NAMES.DAR
    } else if (this.contractType === CONTRACT_TYPES.IFS || this.contractType === CONTRACT_TYPES.IFS_AMENDMENT) {
      this.contractTypeDisplayName = CONTRACT_TYPE_DISPLAY_NAMES.IFS
    } else if (this.contractType === CONTRACT_TYPES.GPO || this.contractType === CONTRACT_TYPES.GPO_AMENDMENT) {
      this.contractTypeDisplayName = CONTRACT_TYPE_DISPLAY_NAMES.GPO
    } else {
      this.contractTypeDisplayName = '';
    }
  }

  fetchPricingDates() {
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.isFurtheranceMode = contractInfo.isFurtheranceMode;
    this.priceStartDate = new Date(moment.utc(contractInfo.pstdate, 'YYYY-MM-DD').format('MM/DD/YYYY'));
    this.priceEndDate = new Date(moment.utc(contractInfo.penddate, 'YYYY-MM-DD').format('MM/DD/YYYY'));
    if (contractInfo.furtherancedate) {
      this.furtheranceEffectiveDate = new Date(moment.utc(contractInfo.furtherancedate, 'YYYY-MM-DD').format('MM/DD/YYYY'));
    }
  }

  ngOnDestroy() {
    this._subscription.forEach(sub => sub.unsubscribe());
    this.cdr.detach();
  }
}
