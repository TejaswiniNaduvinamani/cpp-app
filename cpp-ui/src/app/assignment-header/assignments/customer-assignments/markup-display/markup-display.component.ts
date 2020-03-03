import { Component, OnInit, Input } from '@angular/core';

import { MarkupDisplayModel, ItemLevelDisplayModel, SubgroupDisplayModel } from './markup-display.model';

@Component({
  selector: 'app-markup-display',
  templateUrl: './markup-display.component.html',
  styleUrls: ['./markup-display.component.scss']
})
export class MarkupDisplayComponent implements OnInit {

  @Input() markupValues: MarkupDisplayModel;
  @Input() itemMarkupValues: ItemLevelDisplayModel;
  @Input() subgroupValues: SubgroupDisplayModel;
  @Input() expireLowerInd: number;

  constructor() { }

  ngOnInit() {
  }

}
