import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { ContractSearchResultGridComponent } from './contract-search-result-grid.component';
import { ContractSearchGridModel } from 'app/contract-search';

describe('ContractSearchResultGridComponent', () => {

  const CONTRACT_NAME_SEARCH_RESPONSE: Array<ContractSearchGridModel> = [
    new ContractSearchGridModel('Brinker International', 123, 1, 'Heinz', 'No', '4e4321ca-3d4d-40d5-a3c4-20b56ba1a308',
    'ICMDistributionAgreementRegional',  new Date('2018-01-20'), 'Approved',
    '/furtheranceinformation?agreementId=c03c2f79-9192-4216-b098-821f88342875&contractType=ICMDistributionAgreementRegional')
  ];

  let component: ContractSearchResultGridComponent;
  let fixture: ComponentFixture<ContractSearchResultGridComponent>;

  beforeEach(() => {
     TestBed.configureTestingModule({
       imports: [HttpClientTestingModule],
       declarations: [ContractSearchResultGridComponent],
     }).overrideTemplate(ContractSearchResultGridComponent, '');

     fixture = TestBed.createComponent(ContractSearchResultGridComponent);
     component = fixture.componentInstance;
     fixture.detectChanges();
   });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should go to contract on click of link on grid', () => {
    // GIVEN
    const row = CONTRACT_NAME_SEARCH_RESPONSE;
    spyOn(window, 'open');

    // WHEN
    component.goToContract(row);

    // THEN
    expect(window.open).toHaveBeenCalled();
  })
});
