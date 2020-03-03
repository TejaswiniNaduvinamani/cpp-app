import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-toggle',
  templateUrl: './toggle.component.html',
  styleUrls: ['./toggle.component.scss']
})
export class ToggleComponent implements OnInit {
  @Input() toggelLabel: string;
  @Input() toggleDefaultState: boolean;
  @Input() toggleForm: FormGroup;
  @Output() selectedValue = new EventEmitter<boolean>();

  constructor() {}

  ngOnInit() {
    if (this.toggleDefaultState) {
      (<FormControl>this.toggleForm.get('selectedValue')).setValue(true);
    } else {
      (<FormControl>this.toggleForm.get('selectedValue')).setValue(false);
    }
  }

  checkState($event) {
    if ($event.target.checked) {
      (<FormControl>this.toggleForm.get('selectedValue')).setValue(true);
    } else {
      (<FormControl>this.toggleForm.get('selectedValue')).setValue(false);
    }
    this.selectedValue.emit((<FormControl>this.toggleForm.get('selectedValue')).value)
  }
}
