export class SplitCaseFees {
  productType: string;
  splitCaseFee: number;
  unit: string;
  effectiveDate: Date;
  expirationDate: Date;
  itemPriceId: string;
  isNonNumericSplitCaseFee: boolean;
  invalidSplitCaseFee: boolean;

  constructor(productType: string,
    splitCaseFee: number,
    unit: string,
    effectiveDate: Date,
    expirationDate: Date,
    itemPriceId: string,
    isNonNumericSplitCaseFee: boolean,
    invalidSplitCaseFee: boolean) {}
}

export class SplitCaseList {
  contractPriceProfileSeq: number;
  splitCaseFeeValues: SplitCaseFees[];

  constructor(contractPriceProfileSeq: number,
    splitCaseFeeValues: SplitCaseFees[],
  ) {}
}

export class SaveFurtheranceSplitCaseModel {
  contractPriceProfileSeq: number;
  splitCaseFeeValues: SplitCaseFees[];
  cppFurtheranceSeq: number;

  constructor(contractPriceProfileSeq: number,
    splitCaseFeeValues: SplitCaseFees[],
    cppFurtheranceSeq: number
  ) {}
}

export class SplitCaseViewModel {
  productType: string;
  splitCaseFee: string;
  effectiveDate: Date;
  expirationDate: Date;
  itemPriceId: string;

  constructor(productType: string,
    splitCaseFee: string,
    effectiveDate: Date,
    expirationDate: Date,
    itemPriceId: string) {}
}
