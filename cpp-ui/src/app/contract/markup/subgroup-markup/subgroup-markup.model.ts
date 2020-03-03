export class SubgroupMarkupModel {
    subgroupId: string;
    subgroupDesc: string;
    markup: string;
    unit: string;
    markupType: number;
    effectiveDate: Date;
    expirationDate: Date;
    isInvalidMarkupCurrency: boolean;
    invalidMarkup: boolean;
    invalid: boolean;
    isSubgroupSaved: boolean;
    isSubgroupAlreadyExist: boolean;

    constructor(subgroupId: string,
      subgroupDesc: string,
      markup: string,
      unit: string,
      markupType: number,
      effectiveDate: Date,
      expirationDate: Date,
      isInvalidMarkupCurrency: boolean,
      invalidMarkup: boolean,
      invalid: boolean,
      isSubgroupSaved: boolean,
      isSubgroupAlreadyExist: boolean) { }
  }

  export class SubgroupSearchModel {
    subgroupId: string;
    subgroupDesc: string;
    validationMessage: string;
    validationCode: number;

  constructor(subgroupId: string,
    subgroupDesc: string,
    validationMessage: string,
    validationCode: number) { }
  }

  export class SubgroupMarkupDeleteModel {
    subgroupId: string;
    subgroupDesc: string;
    subgroupIndex: number;
    gfsCustomerId: string;
    gfsCustomerType: number;
  
    constructor(subgroupId: string,
      subgroupDesc: string,
      subgroupIndex: number,
      gfsCustomerId: string,
      gfsCustomerType: number) { }
  }
