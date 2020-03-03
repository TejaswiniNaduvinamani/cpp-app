import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class StepperService {
  // Observable string sources
  private contractTypeChangeSource = new Subject<string>();
  private componentChangeSource = new Subject<any>();
  private furtheranceStepperStatusSource = new BehaviorSubject<number>(0);

  // Observable string streams
  contractTypeChange$ = this.contractTypeChangeSource.asObservable();
  componentChange$ = this.componentChangeSource.asObservable();
  furtheranceStepperChange$ = this.furtheranceStepperStatusSource.asObservable();

  contractTypeChange(contractType: string) {
    this.contractTypeChangeSource.next(contractType);
  }

  // current component loaded
  currentStep(stepNumber: number) {
    this.componentChangeSource.next(stepNumber);
  }

  determineFurtheranceStepperMode(cppSeq: number) {
    this.furtheranceStepperStatusSource.next(cppSeq);
  }

  getCurrentStep() {
    return this.componentChangeSource;
  }
}
