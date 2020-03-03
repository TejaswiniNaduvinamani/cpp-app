export class FutureItemModel {
  contractPriceProfileSeq: number;
  futureItemDesc: string;
  exceptionName: string;
  gfsCustomerId: string;
  gfsCustomerTypeCode: number;
  itemAssignmentList: ItemAssignmentModel[];
  isFutureItemSaved: boolean;
  isItemAlreadyExist: boolean;
  duplicateItemIdList: string[];

  constructor(contractPriceProfileSeq: number,
    futureItemDesc: string,
    exceptionName: string,
    gfsCustomerId: string,
    gfsCustomerTypeCode: number,
    itemAssignmentList: ItemAssignmentModel[],
    isFutureItemSaved: boolean,
    isItemAlreadyExist: boolean,
    duplicateItemIdList: string[] ) {}
};

export class ItemSaveStatusModel {
  errorCode: number;
  errorMessage: string;

  constructor(errorCode: number,
  errorMessage: string) {}
}

export class ItemAssignmentModel {
  itemId: string;
  itemDescription: string;
  isItemSaved: boolean;
  invalidItem: boolean;
  itemExists: boolean;

  constructor(itemId: string,
    itemDescription: string,
    isItemSaved: boolean,
    invalidItem: boolean,
    itemExists: boolean) {}
}

export class AssignmentItemDescriptionModel {
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
