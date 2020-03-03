import { CanActivate } from '@angular/router';
import { Injectable } from '@angular/core';

@Injectable()
export class PricingStatusGuard implements CanActivate {

  canActivate(): boolean {
    const contractInformation = JSON.parse(sessionStorage.getItem('contractInfo'));
    return contractInformation.isPricingSaved ? true : false ;
  }
}
