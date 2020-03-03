import { Component, OnInit, Input, Renderer2, Output, EventEmitter, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { MarkupService, IMAGE_PATH, UNIT_TYPES, MARKUP_TYPES, OK,
  MARKUP_PATTERN, ZERO_VALUE, BACKSPACE_KEY, SUBGROUP_ID_PATTERN, DUPLICATE_VALIDATION_CODE, VALIDATION_CODE } from '../../../shared';
import { MarkupGridDetails } from '../markup-grid/markup-grid.model';
import { SubgroupMarkupModel, SubgroupMarkupDeleteModel } from './subgroup-markup.model';
import { Subscription } from 'rxjs/Subscription';
declare var $;

@Component({
  selector: 'app-subgroup-markup',
  templateUrl: './subgroup-markup.component.html',
  styleUrls: ['./subgroup-markup.component.scss']
})
export class SubgroupMarkupComponent implements OnDestroy {
  @Input() subgroupMarkup: Array<SubgroupMarkupModel>;
  @Input() markupGridDetails: MarkupGridDetails;
  @Input() contractPriceProfileId: string;
  @Input() displayViewMode: boolean;
  @Input() isFurtheranceMode: boolean;
  @Input() determineFurtheranceMode: boolean;
  @Output() editSubgroupGrid = new EventEmitter<string>();
  @Output() onSubgroupUnitChangeVal = new EventEmitter<string>();
  @Output() subgroupDescriptionStatus = new EventEmitter<string>();
  @Output() subgroupMarkupDeleteInfo = new EventEmitter<SubgroupMarkupDeleteModel>();

  public imageDir = IMAGE_PATH;
  public selected = [];
  public subscription: Subscription[] = [];
  public subgroupDeleteModel = <SubgroupMarkupDeleteModel>{};

  constructor(
    private _decimalPipe: DecimalPipe,
    public renderer: Renderer2,
    public markupService: MarkupService
  ) { }

  onMarkupKeyUp($event, row: SubgroupMarkupModel) {
    row.markup = $event.target.value;
    this.markupGridDetails.isMarkupSaved = false;
    this.editSubgroupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onblurMarkup(event, row: SubgroupMarkupModel) {
    event.target.value = (event.target.value).trim();
    if (!MARKUP_PATTERN.test(event.target.value) && (event.target.value)) {
      this.setSubgroupMarkupValidationFlag(event, row, false, true);
    } else {
      const markupVal = event.target.value ? this._decimalPipe.transform(event.target.value, '1.2-2') : '';
      const markupValArray = (markupVal).split('.');
      if (markupValArray[0] && markupValArray[0].length > 2 || markupVal === ZERO_VALUE) {
        if (markupVal === ZERO_VALUE) {
        this.roundOffMarkupValue(event, row);
      }
        this.setSubgroupMarkupValidationFlag(event, row, true, false);
      } else {
        this.roundOffMarkupValue(event, row);
        this.setSubgroupMarkupValidationFlag(event, row, false, false);
      }
    }
  }

  roundOffMarkupValue(event, row) {
    const markupVal = event.target.value ? this._decimalPipe.transform(event.target.value, '1.2-2') : '';
    row.markup = markupVal.replace(/,/g, '');
    this.renderer.setProperty(event.target, 'value', markupVal.replace(/,/g, ''));
  }

  setSubgroupMarkupValidationFlag(event, row, invalidMarkup, isInvalidMarkupCurrency) {
    row.isInvalidMarkupCurrency = isInvalidMarkupCurrency;
    row.invalidMarkup = invalidMarkup;
    if (invalidMarkup || isInvalidMarkupCurrency) {
      this.renderer.addClass(event.target, 'subgroup-markup-error');
    } else {
      this.renderer.removeClass(event.target, 'subgroup-markup-error');
    }
  }

  onblurSubgroupId(event, row: SubgroupMarkupModel) {
    const subgroupId = (event.target.value).trim();
    row.subgroupId = subgroupId;
    const gfsCustomerId = this.markupGridDetails.gfsCustomerId;
    const gfsCustomerType = String(this.markupGridDetails.gfsCustomerType);
    const contractPriceProfileSeq = String(this.markupGridDetails.contractPriceProfileSeq);
    row.subgroupDesc = '';
    if (subgroupId) {
      this.subscription.push(this.markupService.fetchSubgroupDescription(subgroupId, gfsCustomerId,
        gfsCustomerType, contractPriceProfileSeq)
        .subscribe(subgroupDetails => {
          if (subgroupDetails.validationCode === OK) {
            row.subgroupDesc = subgroupDetails.subgroupDesc;
            this.setSubgroupDescDetails(row, false, false);
          } else if (subgroupDetails.validationCode in DUPLICATE_VALIDATION_CODE) {
            this.setSubgroupDescDetails(row, true, false);
          } else if (subgroupDetails.validationCode in VALIDATION_CODE) {
            this.setSubgroupDescDetails(row, false, true);
          }
        }));
    }
  }

  setSubgroupDescDetails(row, isSubgroupAlreadyExist, invalid) {
    row.isSubgroupAlreadyExist = isSubgroupAlreadyExist;
    row.invalid = invalid;
    this.subgroupDescriptionStatus.emit();
  }

  onMarkupTypeChange(event, row) {
    row.markupType = event.target.value;
    this.markupGridDetails.isMarkupSaved = false;
    this.editSubgroupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onSubgroupIdKeyPress($event, row) {
    const inputChar = String.fromCharCode($event.charCode);
    if ($event.keyCode !== BACKSPACE_KEY && !SUBGROUP_ID_PATTERN.test(inputChar) ||
      (inputChar === '0' && $event.target.value.length === 0)) {
      $event.preventDefault();
    }
  }

  onSubgroupIdKeyUp($event, row) {
    row.itemId = $event.target.value;
    this.markupGridDetails.isMarkupSaved = false;
  }

  onUnitChange(event, row) {
    const selectedUnit = event.target.value;
    row.unit = selectedUnit;
    if (selectedUnit === UNIT_TYPES.PERCENT) {
      row.markupType = MARKUP_TYPES.SELL_UNIT;
    }
    this.onSubgroupUnitChangeVal.emit(row.unit);
    this.markupGridDetails.isMarkupSaved = false;
    this.editSubgroupGrid.emit(this.markupGridDetails.gfsCustomerId);
  }

  onDeleteSubgroupMarkup($element) {
    this.subgroupDeleteModel.subgroupDesc = $element.getAttribute('subgroupDesc');
    this.subgroupDeleteModel.subgroupId = $element.getAttribute('subgroupId');
    this.subgroupDeleteModel.subgroupIndex = $element.getAttribute('rowIndex');
    this.subgroupMarkupDeleteInfo.emit(this.subgroupDeleteModel);
  }

  onSubgroupTipContent() {
      $('#markuptipContent').modal('show');
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }
}
