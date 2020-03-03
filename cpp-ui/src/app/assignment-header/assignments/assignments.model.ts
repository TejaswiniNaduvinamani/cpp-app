import { ItemLevelDisplayModel, MarkupDisplayModel, SubgroupDisplayModel } from 'app/assignment-header';
import { RealCustomerModel } from './customer-assignments/customer-assignments.model';

export class AssignmentMarkupModel {
  gfsCustomerId: string;
  markupName: string;
  gfsCustomerType: string;
  gfsCustomerTypeId: number;
  markupList: MarkupDisplayModel[];
  subgroupList: SubgroupDisplayModel[];
  itemList: ItemLevelDisplayModel[];
  isDefault: boolean;
  isAssignmentSaved: boolean;
  realCustomerDTOList: RealCustomerModel[];
  expireLowerInd: number;

  constructor(
    gfsCustomerId: string,
    markupName: string,
    gfsCustomerType: string,
    gfsCustomerTypeId: number,
    markupList: MarkupDisplayModel[],
    subgroupList: SubgroupDisplayModel[],
    itemList: ItemLevelDisplayModel[],
    isDefault: boolean,
    isAssignmentSaved: boolean,
    realCustomerDTOList: RealCustomerModel[],
    expireLowerInd: number
  ) {}
}
