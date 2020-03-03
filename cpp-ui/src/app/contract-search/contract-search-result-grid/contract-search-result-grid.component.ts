import { Component, OnInit, Input,  } from '@angular/core';
import { ContractSearchGridModel } from 'app/contract-search/contract-search.model';

@Component({
  selector: 'app-contract-search-result-grid',
  templateUrl: './contract-search-result-grid.component.html',
  styleUrls: ['./contract-search-result-grid.component.scss']
})
export class ContractSearchResultGridComponent implements OnInit {

  @Input() contractSearchResults: ContractSearchGridModel[];
  @Input() isViewOnlyUser: boolean;
  public pageUrl: string;
  public contractUrl: string;

  datatable_messages = {
    'emptyMessage': ''
  };

  public selected = [];

  ngOnInit() {
    this.pageUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port +
     window.location.pathname + '#';
  }

  goToContract(row) {
    this.contractUrl = this.pageUrl + row.cppLink;
    window.open(this.contractUrl, '_blank');
  }

}
