
export class StepModel {

    public constructor(
        public stepNumber: number,
        public stepName: string,
        public stepUrl: string,
        public isCurrent: boolean = false,
        public isLastStep: boolean = false,
        public isCompleted: boolean = false,
        public isDisabled: boolean  = false ) {
    }
}
