export class MarkupDisplayModel {
  productType: string;
  itemPriceId: number;
  markup: string;
  unit: string;
  markupType: number;
  effectiveDate: Date;
  expirationDate: Date;

  constructor(
    productType: string,
    itemPriceId: number,
    markup: string,
    unit: string,
    markupType: number,
    effectiveDate: Date,
    expirationDate: Date
  ) {}
}

export class SubgroupDisplayModel {
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

export class ItemLevelDisplayModel {
  noItemId: boolean;
  itemId: string;
  stockingCode: string;
  itemDesc: string;
  markup: string;
  unit: string;
  markupType: number;
  effectiveDate: Date;
  expirationDate: Date;


constructor(noItemId: boolean,
  itemId: string,
  stockingCode: string,
  itemDesc: string,
  markup: string,
  unit: string,
  markupType: number,
  expirationDate: Date,
  effectiveDate: Date) {}

}
