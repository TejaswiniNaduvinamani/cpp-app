import { ItemAssignmentModel } from 'app/assignment-header';

export class ItemLevelMarkupModel {
  noItemId: boolean;
  itemId: string;
  stockingCode: string;
  itemDesc: string;
  markup: string;
  unit: string;
  markupType: number;
  effectiveDate: Date;
  expirationDate: Date;
  inactive: boolean;
  invalid: boolean;
  isItemSaved: boolean;
  isInvalidMarkupCurrency: boolean;
  invalidMarkup: boolean;
  isItemAlreadyExist: boolean;

  constructor(noItemId: boolean,
    itemId: string,
    stockingCode: string,
    itemDesc: string,
    markup: string,
    unit: string,
    markupType: number,
    expirationDate: Date,
    effectiveDate: Date,
    inactive: boolean,
    invalid: boolean,
    isItemSaved: boolean,
    isInvalidMarkupCurrency: boolean,
    invalidMarkup: boolean,
    isItemAlreadyExist: boolean) { }
}


export class ItemLevelMarkupViewModel {
  noItemId: number;
  itemId: string;
  itemDesc: string;
  stockingCode: string;
  markup: string;
  markupType: number;
  effectiveDate: string;
  expirationDate: string;


  constructor(noItemId: number,
    itemId: string,
    stockingCode: string,
    itemDesc: string,
    markup: string,
    markupType: number,
    expirationDate: string,
    effectiveDate: string) { }
}

export class ItemDescriptionModel {
  itemNo: string;
  itemDescription: string;
  itemStatusCode: string;
  stockingCode: string;
  isActive: boolean;
  isValid: boolean;
  isItemAlreadyExist: boolean;

  constructor(itemNo: number,
    itemDescription: string,
    itemStatusCode: string,
    stockingCode: string,
    isActive: boolean,
    isValid: boolean,
    isItemAlreadyExist: boolean) { }
}

export class ItemLevelDeleteModel {
  itemNo: string;
  itemDescription: string;
  itemIndex: number;
  gfsCustomerId: string;
  gfsCustomerType: number;

  constructor(itemNo: string,
    itemDescription: string,
    itemIndex: number,
    gfsCustomerId: string) { }
}

export class ItemDetailsForFurtheranceModel {
  contractPriceProfileSeq: number;
  cppFurtheranceSeq: number;
  futureItemDesc: string;
  gfsCustomerId: string;
  gfsCustomerTypeCode: number;
  itemAssignmentList: ItemAssignmentModel[];
  duplicateItemIdList: string[];

  constructor(
  contractPriceProfileSeq: number,
  cppFurtheranceSeq: number,
  futureItemDesc: string,
  gfsCustomerId: string,
  gfsCustomerTypeCode: number,
  itemAssignmentList: ItemAssignmentModel[],
  duplicateItemIdList?: string[]
  ) {}
}
