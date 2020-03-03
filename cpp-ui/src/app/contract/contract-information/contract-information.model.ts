export class ContractInformationDetails {

  contractName: string;
  contractType: string;
  contractStartDate: Date;
  contractEndDate: Date;
  contractStatus: string;
  priceStartDate: Date;
  priceEndDate: Date;
  agreementId: string;
  contractTypeDisplayName: string;
  parentAgreementId: string;
  isAmendment: boolean;

  constructor(
    contractName: string,
    contractType: string,
    contractStartDate: Date,
    contractEndDate: Date,
    contractStatus: string,
    priceStartDate?: Date,
    priceEndDate?: Date,
    agreementId?: string,
    contractTypeDisplayName?: string,
    parentAgreementId?: string,
    isAmendment?: boolean
  ) { }
}

export class ClmContractDetails {
  contractName: string;
  contractType: string;
  contractStartDate: Date;
  contractEndDate: Date;
  contractStatus: string;
  agreementId: string;
  parentAgreementId: string;
  isAmendment: boolean;
  cppInformationDto: CPPInformationDetails;

  constructor(
    contractName: string,
    contractType: string,
    contractStartDate: Date,
    contractEndDate: Date,
    contractStatus: string,
    agreementId: string,
    parentAgreementId: string,
    isAmendment: boolean,
    cppInformationDto: CPPInformationDetails
  ) {}
}

export class CPPInformationDetails {
  contractPriceProfileId: number;
  versionNumber: number;
  contractPriceProfileSeq: number

  constructor(
    contractPriceProfileId: number,
    versionNumber: number,
    contractPriceProfileSeq: number,
  ) {}
}

// do not remove double quotes.
export interface SessionStorageInterface {
  "agreementId"?: string;
  "cenddate"?: Date;
  "cname"?: string;
  "contractStatus"?: string;
  "contractType"?: string;
  "cppid"?: number;
  "cppseqid"?: number;
  "cppFurtheranceSeq"?: number
  "cstdate"?: Date;
  "ctype"?: string;
  "isAmendment"?: boolean;
  "isFurtheranceMode"?: boolean;
  "isPricingExhibitAttached"?: boolean;
  "penddate"?: Date;
  "pstdate"?: Date;
  "versionNumber"?: number;
}

