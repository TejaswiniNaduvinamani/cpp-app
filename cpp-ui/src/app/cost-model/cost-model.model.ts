export class CostModelGridDetails {
  groupType: string;
  costModelId: number;
  itemPriceId: string;
  itemPriceLevelCode: number;
  gfsCustomerId: string;
  gfsCustomerTypeCode: number;
  contractPriceProfileSeq: number;
  costModelTypeValue: string;

  constructor(groupType: string,
    costModelId: number,
    itemPriceId: string,
    itemPriceLevelCode: number,
    gfsCustomerId: string,
    gfsCustomerTypeCode: number,
    contractPriceProfileSeq: number,
    costModelTypeValue: number,
  ) {}
}

export class CostModelDTO {
  costModelId: number;
  costModelTypeValue: string;

  constructor(
    costModelId: number,
    costModelTypeValue: string
  ) {}
}
