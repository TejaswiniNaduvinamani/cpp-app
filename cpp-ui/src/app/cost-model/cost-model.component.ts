import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { AuthorizationService, CostModelService, ReviewService, RETURN_TO_CLM, ToasterService,
  TranslatorService, PricingInformationService  } from '../shared';
import { CostModelGridDetails, CostModelDTO } from 'app/cost-model';

@Component({
  selector: 'app-cost-model',
  templateUrl: './cost-model.component.html',
  styleUrls: ['./cost-model.component.scss']
})
export class CostModelComponent implements OnInit, OnDestroy {

  public contractPriceProfileId: string;
  public versionNumber: string;
  public contractInfoInd = true;
  private subscription: Subscription[] = [];
  public cppStatus: string;
  public contractPriceProfileSeq: string;
  public costModelList: CostModelDTO[];
  public selected = [];
  public costModelGridDetails: CostModelGridDetails[];
  public showSpinner: boolean;
  public displayViewMode: boolean;
  public isSaveDisabled = false;
  public clmHttpLink: string;
  public agreementId: string;
  public contractTypeName: string;

  constructor(
    private _authorizationService: AuthorizationService,
    private _costModelService: CostModelService,
    private _pricingInformationService: PricingInformationService,
    private _reviewService: ReviewService,
    private _toaster: ToasterService,
    private _translatorService: TranslatorService
  ) { }

  ngOnInit() {
    this.showSpinner = true;
    this._pricingInformationService.setPricingInformationStatus(true);
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractPriceProfileId = contractDetails.cppid;
    this.contractPriceProfileSeq = contractDetails.cppseqid;
    this.versionNumber = contractDetails.versionNumber;
    this.agreementId = contractDetails.agreementId;
    this.contractTypeName = contractDetails.contractType;
    this.subscription.push(this._authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileSeq),
      contractDetails.isAmendment, contractDetails.contractStatus)
        .subscribe(authorizationDetails => {
          this.cppStatus = authorizationDetails.cppStatus;
          this.displayViewMode = authorizationDetails.costModelEditable ? false : true;
        }));
      this.fetchCostModelGridDetails();
  }

  fetchCostModelGridDetails() {
    this.showSpinner = true;
    this.subscription.push(
      this._costModelService.fetchCostGridDetails(this.contractPriceProfileSeq).subscribe(costModelGridDetails => {
        this._costModelService.fetchCostModelList().subscribe(costModelList => {
          this.costModelGridDetails = costModelGridDetails;
          this.costModelList = costModelList;
          if (!this.costModelGridDetails.length) {
            this.isSaveDisabled = true;
          }
          this.showSpinner = false;
        });
      }));
  }

  copyRow(row) {
    this.costModelGridDetails.forEach((ele) => {
      ele.costModelId = row.costModelId;
    });
  }

  saveCostModel() {
    this.showSpinner = true;
    const saveCostModelSuccess = this._translatorService.translate('TOASTER_MESSAGES.COST_MODEL_UPDATE_SUCCESS');
    this.subscription.push(this._costModelService.saveCostModel(this.costModelGridDetails)
        .subscribe(() => {
          this.showSpinner = false;
          this._toaster.showSuccess(saveCostModelSuccess, '');
        }, error => {
          this.showSpinner = false;
        }));
  }

  returnToCLM() {
    this.showSpinner = true;
    this.subscription.push(
      this._reviewService.fetchClmUrl()
        .subscribe(response => {
          if (response) {
            this.clmHttpLink = response.clmUrlKey;
            const clmURL = this.clmHttpLink + this.contractTypeName + RETURN_TO_CLM.AND_ID + this.agreementId;
            sessionStorage.clear();
            window.open(clmURL, '_self');
          }
        },
        error => {
          this.showSpinner = false;
      }));
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }
}
