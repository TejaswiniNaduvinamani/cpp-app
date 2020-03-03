export class RealCustomerModel {
  realCustomerName: string;
  realCustomerId: string;
  realCustomerType: string;
  realCustomerGroupType: string;
  isCustomerSaved: boolean;
  duplicateCustomerFound: boolean;
  validationError: boolean;

  constructor(realCustomerName: string,
    realCustomerId: string,
    realCustomerType: string,
    realCustomerGroupType: string,
    isCustomerSaved: boolean,
    duplicateCustomerFound: boolean,
    validationError: boolean) {}
}

export class SaveAssignmentModel {
  contractPriceProfileId: number;
  contractPriceProfileSeq: number;
  cmgCustomerId: string;
  cmgCustomerType: number;
  clmContractStartDate: Date;
  clmContractEndDate: Date;
  realCustomerDTOList: RealCustomerModel[]

  constructor(
  contractPriceProfileSeq: number,
  cmgCustomerId: string,
  cmgCustomerType: number,
  realCustomerDTOList: RealCustomerModel[]
  ) {}
}

export class SaveAssignmentResponseModel {
  errorCode: number;
  errorMessage: string;

  constructor( errorCode: number, errorMessage: string ) {}
}
