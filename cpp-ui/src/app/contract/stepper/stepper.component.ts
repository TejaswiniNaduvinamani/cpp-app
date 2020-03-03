import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

import { AuthorizationService } from './../../shared';
import { ContractInformation } from './../../contract';
import { ContractInformationComponent } from './../../contract';
import { StepperService, CONTRACT_TYPES, STEPPER_LABEL, IMAGE_PATH, STEPPER_URL, PricingInformationService } from './../../shared';
import { StepModel } from './step.model';


@Component({
  selector: 'app-stepper',
  templateUrl: './stepper.component.html',
  styleUrls: ['./stepper.component.scss']
})
export class StepperComponent implements OnInit, OnDestroy {
  private: ContractInformationComponent;
  public stepper: Array<StepModel> = [];
  public contractType: string;
  public step1; step2; step3; step4; step5;
  public currentStep: number;
  public imageDir = IMAGE_PATH;
  public contractPriceProfileId: number;
  public versionNumber: number;
  private subscription: Subscription[] = [];
  public displayViewMode: boolean;
  public agreementId: string;
  public cppStatus: string;
  public isAmendment: boolean;
  public isFurtheranceMode: boolean;
  public cppSeqId: number;

  constructor(
    private _route: ActivatedRoute,
    private _stepperService: StepperService,
    private _pricingInformationService: PricingInformationService,
    private _authorizationService: AuthorizationService,
    private cdr: ChangeDetectorRef,
  ) { }

  ngOnInit() {
    this.initializeStepper();
    this.detectContractTypeChanges();
    this.subscription.push(this._authorizationService.getAuthorizationDetails().subscribe( authorizationInformation => {
      this.displayViewMode = !authorizationInformation.priceProfileEditable;
      this.cppStatus = authorizationInformation.cppStatus;
    }));
    if (sessionStorage.contractInfo) {
      const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
      this.isFurtheranceMode = contractInfo.isFurtheranceMode;
    } else {
      this.isFurtheranceMode = false;
    }
    this.subscription.push(this._stepperService.componentChange$.subscribe(
      stepNumber => {
        this.navigateStepper(stepNumber);
    }));
    this.agreementId = this._route.snapshot.queryParams['agreementId'];
    this.contractType = this._route.snapshot.queryParams['contractType'];
    if (this.agreementId && this.contractType) {
      this.subscription.push(this._pricingInformationService.getClmContractDetails().subscribe(clmContractDetails => {
        this.isAmendment = clmContractDetails.isAmendment;
        this.updateContractInformation(clmContractDetails.cppInformationDto);
      }));
    } else {
      this.fetchContractInformation();
    }
    this.showHideSteps();
  }

  updateContractInformation(contractInformation: ContractInformation) {
    this.contractPriceProfileId = contractInformation.contractPriceProfileId;
    this.versionNumber = contractInformation.versionNumber;
    this.cdr.detectChanges();
    this.setContractInformation(contractInformation);
  }

  setContractInformation(contractInformation: ContractInformation) {
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    contractInfo.cppid = contractInformation.contractPriceProfileId;
    contractInfo.versionNumber = contractInformation.versionNumber;
    contractInfo.cppseqid = this.cppSeqId = contractInformation.contractPriceProfileSeq;
    sessionStorage.setItem('contractInfo', JSON.stringify(contractInfo));
  }

  fetchContractInformation() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractPriceProfileId = contractDetails.cppid;
    this.versionNumber = contractDetails.versionNumber;
    this.isAmendment = contractDetails.isAmendment;
    this.contractType = contractDetails.contractType;
  }

  initializeStepper() {
    this.step1 = this.stepper.push(new StepModel(1, STEPPER_LABEL.PRICING_INFORMATION, STEPPER_URL.PRICING_INFORMATION_URL));
    this.step2 = this.stepper.push(new StepModel(2, STEPPER_LABEL.DISTRIBUTION_CENTERS, STEPPER_URL.DISTRIBUTION_CENTERS_URL));
    this.step3 = this.stepper.push(new StepModel(3, STEPPER_LABEL.MARKUP, STEPPER_URL.MARKUP_URL));
    this.step4 = this.stepper.push(new StepModel(4, STEPPER_LABEL.SPLIT_CASE, STEPPER_URL.SPLIT_CASE_URL));
    this.step5 = this.stepper.push(new StepModel(5, STEPPER_LABEL.REVIEW, STEPPER_URL.REVIEW_URL));
  }

  detectContractTypeChanges() {
    this._stepperService.contractTypeChange$.subscribe(selectedContractType => {
      this.contractType = selectedContractType;
      this.showHideSteps();
    });
  }

  showHideSteps() {
    if (this.contractType === CONTRACT_TYPES.IFS || this.contractType === CONTRACT_TYPES.IFS_AMENDMENT) {
      if (this.stepper.length > 4) {
        setTimeout(() => {
          this.stepper.splice(3, 1);
        });
      }
    }
  }

  updateStepper(stepNumber: number) {
    this.currentStep = stepNumber;
    this.stepper.forEach(step => {
      if (step.stepNumber < this.currentStep) {
        step.isCurrent = false;
        step.isDisabled = false;
        step.isCompleted = true;
      } else if (step.stepNumber > this.currentStep) {
        step.isCurrent = false;
        step.isDisabled = true;
        step.isCompleted = false;
      } else if (step.stepNumber === this.currentStep) {
        step.isCurrent = true;
        step.isDisabled = false;
        step.isCompleted = false;
      }
    });
  }

  updateStepperViewMode(stepNumber: number) {
    this.currentStep = stepNumber;
    this.stepper.forEach(step => {
      step.isCurrent = false;
      step.isDisabled = false;
      step.isCompleted = true;
    })
  }

  navigateStepper(stepNumber) {
    if (!this.displayViewMode && !this.isFurtheranceMode) {
      this.updateStepper(stepNumber);
      this.cdr.detectChanges();
    } else if (this.isFurtheranceMode) {
      this._stepperService.furtheranceStepperChange$.subscribe((value) => {
        if (value > 0) {
          this.updateStepperViewMode(stepNumber);
        } else {
          this.updateStepper(stepNumber);
          this.cdr.detectChanges();
        }
      })
    } else {
        this.updateStepperViewMode(stepNumber);
        this.cdr.detectChanges();
  }}

  ngOnDestroy() {
    // prevent memory leak when component destroyed
    this.subscription.forEach(sub => sub.unsubscribe());
  }
}
