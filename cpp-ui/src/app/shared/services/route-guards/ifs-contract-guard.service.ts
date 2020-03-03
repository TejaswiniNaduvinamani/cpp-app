import { CanActivate } from '@angular/router';
import { Injectable } from '@angular/core';

import { CONTRACT_TYPES } from './../../utils/app.constants';

@Injectable()
export class IfsContractGuard implements CanActivate {

  public selectedContractType: string;

  constructor() { }

  canActivate(): boolean {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.selectedContractType = contractDetails.ctype;
    if (this.selectedContractType !== CONTRACT_TYPES.IFS && this.selectedContractType !== CONTRACT_TYPES.IFS_AMENDMENT) {
      return true;
    } else {
      return false;
    }
  }

}
