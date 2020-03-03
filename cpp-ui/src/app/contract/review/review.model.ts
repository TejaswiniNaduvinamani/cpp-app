export class MarkupGridModel {
  productType: string;
  itemPriceId: number;
  markup: string;
  unit: string;
  markupType: number;
  effectiveDate: Date;
  expirationDate: Date;

  constructor( productType: string,
    itemPriceId: number,
    markup: string,
    unit: string,
    markupType: number,
    effectiveDate: Date,
    expirationDate: Date) {}
};

export class ItemGridModel {
  noItemId: boolean;
  itemId: string;
  itemDesc: string;
  stockingCode: string;
  markup: string;
  unit: string;
  markupType: number;
  effectiveDate: Date;
  expirationDate: Date;

  constructor( noItemId: boolean,
    itemId: string,
    itemDesc: string,
    stockingCode: string,
    markup: string,
    unit: string,
    markupType: number,
    effectiveDate: Date,
    expirationDate: Date) {}

}

export class SubgroupGridModel {
  subgroupDesc: string;
  markup: string;
  unit: string;
  markupType: number;

  constructor(
    subgroupDesc: string,
    markup: string,
    unit: string,
    markupType: number
  ) {}
}

export class SplitCaseFeesModel {
  productType: string;
  splitCaseFee: number;
  unit: string;
  effectiveDate: Date;
  expirationDate: Date;
  itemPriceId: string;

  constructor( productType: string,
    splitCaseFee: number,
    unit: string,
    effectiveDate: Date,
    expirationDate: Date,
    itemPriceId: string) {}
};

export class MarkupGridDTO {
  markupName: string;
  markup: MarkupGridModel[];
  item: ItemGridModel[];
  subgroup: SubgroupGridModel[];

  constructor(  markupName: string,
    markup: MarkupGridModel[], item: ItemGridModel[], subgroup: SubgroupGridModel[] ) {}
};

export class ContractPricingDTO {
  scheduleForCost: string;
  scheduleForCostContractLanguage?: string;
  formalPriceAudit: string;
  formalPriceAuditContractLanguage?: string;
  priceVerification: string;
  priceVerificationContractLanguage?: string;
  gfsTransferFee: string;
  gfsLabelAssesmentFee: string;
  costOfProductsContractLanguage?: string;

  constructor(scheduleForCost: string,
    scheduleForCostContractLanguage: string,
    formalPriceAudit: string,
    formalPriceAuditContractLanguage: string,
    priceVerification: string,
    priceVerificationContractLanguage: string,
    gfsTransferFee: string,
    gfsLabelAssesmentFee: string,
    costOfProductsContractLanguage: string) {}
};

export class SplitCaseReviewDTO {
  feeTypeContractLanguage: string;
  feeType: number;
  splitCaseFeeValues: SplitCaseFeesModel[];

  constructor( feeTypeContractLanguage: string,
    feeType: number,
    splitCaseFeeValues: SplitCaseFeesModel[]) {}
}

export class MarkupReviewDTO {
  markupBasedOnSell: string;
  markupBasedOnSellContractLanguage1?: string;
  markupTypeDefinitionSellUnitLanguage?: string;
  markupTypeDefinitionPerCaseLanguage?: string;
  markupTypeDefinitionPerWeightLanguage?: string;
  markupBasedOnSellContractLanguage2?: string;
  markupGridDTOs: MarkupGridDTO[];

  constructor( markupBasedOnSell: string,
    markupBasedOnSellContractLanguage1: string,
    markupTypeDefinitionSellUnitLanguage: string,
    markupTypeDefinitionPerCaseLanguage: string,
    markupTypeDefinitionPerWeightLanguage: string,
    markupBasedOnSellContractLanguage2: string,
    markupGridDTOs: MarkupGridDTO[]) {}
}

export class ReviewData {
  contractPricingReviewDTO: ContractPricingDTO;
  distributionCenter: string[];
  markupReviewDTO: MarkupReviewDTO;
  splitCaseReviewDTO: SplitCaseReviewDTO;

  constructor(contractPricingReviewDTO: ContractPricingDTO,
    distributionCenter: string[],
    markupReviewDTO: MarkupReviewDTO,
    splitCaseReviewDTO: SplitCaseReviewDTO) {}
}

export class CPPExhibitDownload {
  contractPriceProfileSeq: string;

  constructor(contractPriceProfileSeq: string) {}
}

export class ReturnToCLM {
  clmUrlKey: string;

  constructor(clmUrlKey: string) {}
}

export class PricingExhibitModel {
  contractPriceProfileSeq: number;
  contractAgeementId: string;
  contractTypeName: string;

  constructor(
    contractPriceProfileSeq: number,
    contractAgeementId: string,
    contractTypeName: string) {}
}
