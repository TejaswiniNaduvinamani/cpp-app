import { async } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed, inject } from '@angular/core/testing';

import { ContractInformationDetails } from 'app/contract/contract-information/contract-information.model';
import { IfsContractGuard } from './ifs-contract-guard.service';

describe('IfsContractGuard', () => {

  let service: IfsContractGuard;

  const CONTRACT_DETAILS: ContractInformationDetails = new ContractInformationDetails('Contract',
    'DAR', new Date('01/26/2020'), new Date('01/30/2020'), 'draft', new Date('01/26/2020'), new Date('01/30/2020'), '123', 'edit', '2');

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [IfsContractGuard]
    });
    service = TestBed.get(IfsContractGuard);
  }));

  beforeEach(() => {
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
  });

  it('should return true if Contract type is not IFS', () => {
    // GIVEN
    sessionStorage.setItem('contractInfo', JSON.stringify(CONTRACT_DETAILS));
    service.selectedContractType = 'DAR';

    // THEN
    expect(service.canActivate()).toBe(true);
  });

});
