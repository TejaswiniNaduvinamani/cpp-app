import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { DistributionCenters, DistributionCenterList } from './distribution-center.model';
import { StepperService, STEPPER_URL, DistributionCenterService, STEPPER_NUMBERS,
  AuthorizationService, CONTRACT_TYPES, FURTHERANCE_STEPPER_CODE } from './../../shared';
declare var $;

@Component({
  selector: 'app-distribution-centers',
  templateUrl: './distribution-centers.component.html',
  styleUrls: ['./distribution-centers.componet.scss']
})

export class DistributionCentersComponent
  implements OnInit, OnDestroy, DoCheck {
  public isActive: boolean[] = [false];
  public distributionCenters: DistributionCenters[] = [];
  private _subscription: Subscription[] = [];
  public selectedDistributionCenters: string[] = [];
  public validDistributionCenterList = true;
  public distributionCenterList = <DistributionCenterList>{};
  public showSpinner: boolean;
  public displayViewMode: boolean;
  public contractPriceProfileSeq: number;
  public saveGrayed = true;
  public isPricingExhibitAttached: boolean;

  constructor(
    private _authorizationService: AuthorizationService,
    private _stepperService: StepperService,
    private _distributonService: DistributionCenterService,
    private _router: Router,
    private _route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.showSpinner = true;
    this._subscription.push(
      this._distributonService.fetchDistributionCenters().subscribe(response => {
        this.distributionCenters = response;
        this.initializeState();
        const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
        this.contractPriceProfileSeq = contractInfo.cppseqid;
        this.isPricingExhibitAttached = contractInfo.isPricingExhibitAttached;
        this._subscription.push(this._authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileSeq),
          contractInfo.isAmendment, contractInfo.contractStatus)
          .subscribe(authorizationDetails => {
            this._authorizationService.setAuthorizationDetails(authorizationDetails);
            this._stepperService.currentStep(STEPPER_NUMBERS.DISTRIBUTION_CENTERS);
            if (contractInfo.isFurtheranceMode) {
              this._stepperService.determineFurtheranceStepperMode(FURTHERANCE_STEPPER_CODE);
              }
            this.displayViewMode = authorizationDetails.priceProfileEditable ? false : true;
            this._subscription.push(this._distributonService.fetchSelectedDistributionCenters(String(this.contractPriceProfileSeq))
              .subscribe(response => {
                response.forEach((distributionCenter, index) => {
                  this.isActive[distributionCenter.dcCode] = true;
                });
                this.showSpinner = false;
              }));
          }));
      }));
  }

  initializeState() {
    this.distributionCenters.forEach((distributionCenter, index) => {
      this.isActive[distributionCenter.dcNumber] = false;
    });
  }

  viewModeClick(event) {
    if (this.displayViewMode) {
      event.preventDefault();
      return false;
    }
  }

  selectALL() {
    this.distributionCenters.forEach((distributionCenter) => {
      this.isActive[distributionCenter.dcNumber] = true;
    });
  }

  clear() {
    this.distributionCenters.forEach((distributionCenter) => {
      this.isActive[distributionCenter.dcNumber] = false;
    });
  }

  buildDistributionCenters(selectedDistributionCenters) {
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    const effectiveDate = contractInfo.pstdate;
    const expirationDate = contractInfo.penddate;
    this.distributionCenterList.contractPriceProfileSeq = this.contractPriceProfileSeq;
    this.distributionCenterList.distributionCenters = selectedDistributionCenters;
    this.distributionCenterList.effectiveDate = effectiveDate;
    this.distributionCenterList.expirationDate = expirationDate;
  }

  checkAttachedPricingExhibit(dcForm) {
    this.selectedDistributionCenters = [];
    Object.keys(dcForm.value).forEach((element) => {
      if (this.isActive[element]) {
        this.selectedDistributionCenters.push(element);
      }
    });
    if (this.selectedDistributionCenters.length > 0) {
      this.validDistributionCenterList = true;
    } else {
      this.validDistributionCenterList = false;
    }
    if (this.validDistributionCenterList) {
      if (this.isPricingExhibitAttached) {
        $('#deletePricingExhibit').modal('show');
      } else {
        this.saveDistributionCenters(this.selectedDistributionCenters)
      }
    }
  }

  onExhibitDeletion(isExhibitDeleted) {
    if (isExhibitDeleted) {
      this.showSpinner = false;
      this.saveDistributionCenters(this.selectedDistributionCenters);
    } else {
      this.showSpinner = true;
    }
  }

  saveDistributionCenters(selectedDistributionCenters) {
    this.showSpinner = true;
    this.buildDistributionCenters(selectedDistributionCenters);
    this._subscription.push(
      this._distributonService
        .saveDistributionCenters(this.distributionCenterList)
        .subscribe(() => {
          this.showSpinner = false;
          this._router.navigate([STEPPER_URL.MARKUP_URL], { relativeTo: this._route });
        }, error => {
          this.showSpinner = false;
        }));
  }

  ngDoCheck() {
    this.saveGrayed = this.isActive.some(element => element);
    this.isActive.forEach(element => {
      if (element === true) {
        this.validDistributionCenterList = true;
      }
    });
  }

  ngOnDestroy() {
    this._subscription.forEach(sub => sub.unsubscribe());
  }
}
