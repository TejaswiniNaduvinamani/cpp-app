export class DistributionCenters {
  constructor(
    public dcNumber: string,
    public name: string,
    public shortName: string) {}
}

export class DistributionCenterList {
  constructor(
    public contractPriceProfileSeq: number,
    public distributionCenters: string[],
    public expirationDate: Date,
    public effectiveDate: Date) {}
}

export class SelectedDistributionCenters {
  constructor(
    public dcCode: string,
    public cppId: string,
    public expirationDate: Date,
    public effectiveDate: Date) {}
}
