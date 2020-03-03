
export class ContractSearchGridModel {
  contractName: string;
  contractPriceProfileId: number;
  version: number;
  customerName: string;
  isFurtheranceExist: string;
  agreementId: string;
  contractType: string;
  effectiveDate: Date;
  status: string;
  cppLink: string;

  constructor( contractName: string,
    contractPriceProfileId: number,
    version: number,
    customerName: string,
    isFurtheranceExist: string,
    agreementId: string,
    contractType: string,
    effectiveDate: Date,
    status: string,
    cppLink: string) {}
}
