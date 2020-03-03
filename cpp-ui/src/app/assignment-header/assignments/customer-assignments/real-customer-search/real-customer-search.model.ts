export class RealCustomerSearchModel {
  customerName: string;
  validationCode: number;
  validationMessage: string;

  constructor(customerName: string, validationCode: number, validationMessage: string) {}
}

export class DeleteRealCustomerModel {
  realCustomerId: string;
  realCustomerType: string;

  constructor(realCustomerId: string, realCustomerType: string) {}
}

export class RealCustomerListModel {
  realCustomerId: string;
  realCustomerType: string;

  constructor(   realCustomerId: string, realCustomerType: string ) {}
}

export class ValidationResponseModel {
  statusCode: number;
  statusMsg: string;

  constructor( statusCode: number,
    statusMsg: string ) {}
}
