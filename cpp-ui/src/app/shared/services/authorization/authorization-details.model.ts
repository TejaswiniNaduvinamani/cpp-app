export class AuthorizationDetails {
  priceProfileEditable: boolean;
  customerAssignmentEditable: boolean;
  itemAssignmentEditable: boolean;
  costModelEditable: boolean;
  isPowerUser: boolean;
  cppStatus: string;

  constructor(
    priceProfileEditable: boolean,
    customerAssignmentEditable: boolean,
    itemAssignmentEditable: boolean,
    isPowerUser: boolean,
    cppStatus: string,
    costModelEditable?: boolean,
  ) { }
}
