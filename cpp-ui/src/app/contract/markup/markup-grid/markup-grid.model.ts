import { ItemLevelMarkupModel, SubgroupMarkupModel } from './../../../contract';

export class MarkupGridModel {
  productType: string;
  itemPriceId: number;
  markup: string;
  unit: string;
  markupType: number;
  effectiveDate: Date;
  expirationDate: Date;
  isInvalidCurrency: boolean;
  invalidMarkup: boolean;

  constructor(productType: string,
    itemPriceId: number,
    markup: string,
    unit: string,
    markupType: number,
    effectiveDate: Date,
    expirationDate: Date,
    isInvalidCurrency: boolean,
    invalidMarkup: boolean
  ) {}
};

export class MarkupGridViewModel {
  productType: string;
  itemPriceId: number;
  markup: string;
  markupType: number;
  effectiveDate: Date;
  expirationDate: Date;

  constructor(productType: string,
    itemPriceId: number,
    markup: string,
    markupType: number,
    effectiveDate: Date,
    expirationDate: Date) {}
};

export class MarkupGridDetails {
  gfsCustomerId: string;
  gfsCustomerType: number;
  contractPriceProfileSeq: number;
  markupName: string;
  productMarkupList: MarkupGridModel[];
  subgroupMarkupList: SubgroupMarkupModel[];
  itemLevelMarkupList: ItemLevelMarkupModel[];
  isMarkupSaved: boolean;
  markupGridIndex: number;
  cppFurtheranceSeq?: number;

  constructor(gfsCustomerId: string,
    gfsCustomerType: number,
    contractPriceProfileSeq: number,
    markupName: string,
    productMarkupList: MarkupGridModel[],
    subgroupMarkupList: SubgroupMarkupModel[],
    itemLevelMarkupList: ItemLevelMarkupModel[],
    isMarkupSaved: boolean,
    markupGridIndex: number,
    cppFurtheranceSeq?: number
  ) {}
};

export class ExceptionModel {
  contractPriceProfileSeq: number;
  exceptionName: string;

  constructor( contractPriceProfileSeq: number,
    exceptionName: string) {}
};

export class MarkupIndicatorsModel {
  contractPriceProfileSeq: number;
  effectiveDate: Date;
  expirationDate: Date;
  markupOnSell: boolean;
  expireLower: boolean;

  constructor( contractPriceProfileSeq: number,
    effectiveDate: Date,
    expirationDate: Date,
    expireLower: boolean,
    markupOnSell: boolean) {}
};

export class RenameMarkupModel {
  contractPriceProfileSeq: number;
  gfsCustomerId: string;
  exceptionName: string;
  newExceptionName: string;

  constructor(contractPriceProfileSeq: number,
    gfsCustomerId: string,
    exceptionName: string,
    newExceptionName: string) {}
}
