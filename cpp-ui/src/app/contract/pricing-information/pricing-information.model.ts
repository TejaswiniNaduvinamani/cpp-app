export class PricingInformation {
  contractName: string;
  contractType: string;
  contractPriceProfileId: number;
  pricingEffectiveDate: Date;
  pricingExpirationDate: Date;
  scheduleForCostChange: string;
  priceVerificationFlag: boolean;
  priceAuditFlag: boolean;
  transferFeeFlag: boolean;
  assessmentFeeFlag: boolean;
  pricingExhibitSysId: string;
  contractPriceProfileSeq: number;
  agreementId: string;
  clmContractStartDate: Date;
  clmContractEndDate: Date;
  parentAgreementId: string;
  versionNbr: number;


  constructor(
    contractName: string,
    contractType: string,
    contractPriceProfileId: number,
    pricingEffectiveDate: Date,
    pricingExpirationDate: Date,
    scheduleForCostChange: string,
    priceVerificationFlag: boolean,
    priceAuditFlag: boolean,
    transferFeeFlag: boolean,
    assessmentFeeFlag: boolean,
    pricingExhibitSysId: string,
    contractPriceProfileSeq: number,
    agreementId: string,
    clmContractStartDate: Date,
    clmContractEndDate: Date,
    parentAgreementId: string,
    versionNbr: number,
  ) {}

}

export class ContractInformation {
  contractPriceProfileId: number;
  versionNumber: number;
  contractPriceProfileSeq: number

  constructor(
    contractPriceProfileId: number,
    versionNumber: number,
    contractPriceProfileSeq: number,
  ) {}
}


