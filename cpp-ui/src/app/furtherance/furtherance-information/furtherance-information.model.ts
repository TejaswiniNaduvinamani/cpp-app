export class FurtheranceBaseModel {
  agreementId: string;
  contractType: string;
  cppFurtheranceSeq: number;

  constructor(agreementId: string,
    contractType: string,
    cppFurtheranceSeq: number
  ) { }
}

export class ValidateFurtheranceModel {
  hasInProgressFurtherance: boolean

  constructor(hasInProgressFurtherance: boolean) { }
}

export class FurtheranceInformationModel {
  cppFurtheranceSeq: number;
  contractPriceProfileSeq: number;
  furtheranceStatusCode: number;
  parentCLMAgreementId: string;
  furtheranceEffectiveDate: Date;
  changeReasonTxt: string;
  contractReferenceTxt: string;

  constructor(  cppFurtheranceSeq: number,
    contractPriceProfileSeq: number,
    furtheranceStatusCode: number,
    parentCLMAgreementId: string,
    furtheranceEffectiveDate: Date,
    changeReasonTxt: string,
    contractReferenceTxt: string) {}
}
