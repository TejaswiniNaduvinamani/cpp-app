import { Component, OnInit, OnDestroy, Renderer2, ViewChildren, ElementRef, QueryList, ViewChild, DoCheck } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { IMAGE_PATH, CONTRACT_TYPES, STEPPER_URL, FURTHERANCE_STEPPER_CODE, UNIT_TYPES, ONE,
  AuthorizationService, FurtheranceService } from './../../shared';
import { ItemLevelDeleteModel } from 'app/contract';
import { MarkupGridDetails, MarkupGridViewModel, ExceptionModel,
  MarkupIndicatorsModel, RenameMarkupModel } from './markup-grid/markup-grid.model';
import { MarkupService, ToasterService, TranslatorService } from './../../shared';
import { StepperService, STEPPER_NUMBERS } from './../../shared';
import { SubgroupMarkupDeleteModel } from './subgroup-markup/subgroup-markup.model';
declare var $: any;

@Component({
  selector: 'app-markup',
  templateUrl: './markup.component.html',
  styleUrls: ['./markup.component.scss'],
})
export class MarkupComponent implements OnInit, OnDestroy, DoCheck {

  @ViewChildren('copy') public copySelects: QueryList<ElementRef>;
  @ViewChild('editModalTitle') public editModalTitle: ElementRef;
  @ViewChild('editModalInput') public editModalInput: ElementRef;
  @ViewChild('deleteModalTitle') public deleteModalTitle: ElementRef;
  @ViewChild('deleteItemModalTitle') public deleteItemModalTitle: ElementRef;
  @ViewChild('deleteItemBtn') public deleteItemBtn: ElementRef;
  @ViewChild('deleteExceptionBtn') public deleteExceptionBtn: ElementRef;
  @ViewChild('deleteSubgroupModalTitle') public deleteSubgroupModalTitle: ElementRef;
  @ViewChild('deleteSubgroupBtn') public deleteSubgroupBtn: ElementRef;

  public markupForm: FormGroup;
  public markupModalForm: FormGroup;
  public editmarkupModalForm: FormGroup;
  public imageDir = IMAGE_PATH;
  public markupOnSellLabel: string;
  public markupOnSellVal: boolean;
  public showMarkupOnSell: boolean;
  public copyMarkups = [];
  private subscription: Subscription[] = [];
  public selectedContractType: string;
  public contractTypeIFS = CONTRACT_TYPES.IFS;
  public contractTypeIFSAmendement = CONTRACT_TYPES.IFS_AMENDMENT;
  public contractPriceProfileId: any;
  public effectiveDate: Date;
  public expirationDate: Date;
  public contractName: string;
  public mode: string;
  public isNextDisabled = true;
  public markupAccordions: MarkupGridDetails[] = [];
  public saveMarkupGridModel = <MarkupGridDetails>{};
  public markupIndicatorsModel = <MarkupIndicatorsModel>{};
  public renameMarkupModel = <RenameMarkupModel>{};
  public markupGridViewModel = <MarkupGridViewModel>{};
  public displayViewMode: boolean;
  public showSpinner: boolean;
  public isSubmitDisabled = true;
  public disableAddException = true;
  public isPricingExhibitAttached: boolean;
  public actionId: string;
  public saveGridDetailsObj: Object;
  public isFurtheranceMode: boolean;
  public cppFurtheranceSeq: number;
  public determineFurtheranceMode: boolean;
  public canEditFurtherance = false;
  public isMarkupUnitDisabled = false;

  constructor(
    private _authorizationService: AuthorizationService,
    private _stepperService: StepperService,
    private _formBuilder: FormBuilder,
    private _markupService: MarkupService,
    private _furtheranceService: FurtheranceService,
    private _router: Router,
    private _route: ActivatedRoute,
    public _renderer: Renderer2,
    private _toaster: ToasterService,
    private _translatorService: TranslatorService
  ) { }

  ngOnInit() {
    this.showSpinner = true;
    this.loadForm();
    this.loadMarkupModalForm();
    this.loadEditMarkupModalForm();
    this.fetchGridDetails();
  }

  ngDoCheck() {
    this.markupOnSellLabel = this._translatorService.translate('MARKUP_LABELS.MARKUP_ON_SELL');
  }

  loadForm() {
    this.markupForm = this._formBuilder.group({
      markupOnSellToggle: this._formBuilder.group({
        selectedValue: [false]
      }),
      expireLowerQuestion: new FormControl()
    });
  }

  loadMarkupModalForm() {
    this.markupModalForm = new FormGroup({
      markupStructure: new FormControl(null, [Validators.required, this.markupNameValidator.bind(this)]),
    });
  }

  loadEditMarkupModalForm() {
    this.editmarkupModalForm = new FormGroup({
      editMarkupStructure: new FormControl(null, [Validators.required, this.markupNameValidator.bind(this)]),
    });
  }

  markupNameValidator(control: FormControl) {
    const input = control.value;
    const isWhitespace = (input || '').trim().length === 0;
    const isValid = !isWhitespace;
    if (!isValid) {
      return { 'whitespace': true };
    } else if (input && this.markupAccordions.some(element => (element.markupName).toLowerCase() == (input.toLowerCase()).trim())) {
      return { 'duplicateMarkupName': true };
    } else {
      return null;
    }
  }

  fetchGridDetails() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.contractPriceProfileId = contractDetails.cppseqid;
    this.contractName = contractDetails.cname;
    this.effectiveDate = contractDetails.pstdate;
    this.expirationDate = contractDetails.penddate;
    this.selectedContractType = contractDetails.ctype;
    this.isPricingExhibitAttached = contractDetails.isPricingExhibitAttached;
    this.isFurtheranceMode = contractDetails.isFurtheranceMode;
    this.cppFurtheranceSeq = contractDetails.cppFurtheranceSeq;
    this.subscription.push(this._authorizationService.fetchAuthorizationDetails(String(this.contractPriceProfileId),
      contractDetails.isAmendment, contractDetails.contractStatus)
      .subscribe(authorizationDetails => {
        this._authorizationService.setAuthorizationDetails(authorizationDetails);
        this._stepperService.currentStep(STEPPER_NUMBERS.MARKUP);
        this.displayViewMode = authorizationDetails.priceProfileEditable ? false : true;
        if (contractDetails.isFurtheranceMode) {
          this._stepperService.determineFurtheranceStepperMode(FURTHERANCE_STEPPER_CODE);
          this.subscription.push(this._furtheranceService.canEditFurtherance(this.cppFurtheranceSeq).subscribe(displayMode => {
            this.canEditFurtherance = displayMode.canEditFurtherance;
            this.determineFurtheranceIndicator()
          }))
          } else {
            this.determineFurtheranceIndicator()
          }
        this.subscription.push(this._markupService.fetchMarkupGridDetails(this.contractPriceProfileId, this.effectiveDate,
          this.expirationDate, this.contractName)
          .subscribe(response => {
            this.markupAccordions = response;
            this.fetchMarkupIndicators();
            this.isNextDisabled = this.markupAccordions.some(element => !element.isMarkupSaved);
            if (this.markupAccordions[0].isMarkupSaved) {
              this.disableAddException = false;
            }
          }));
      }));
  }

  fetchMarkupIndicators() {
    this.subscription.push(this._markupService.fetchMarkupIndicators(this.contractPriceProfileId)
      .subscribe(markupIndicators => {
        this.markupForm.get('expireLowerQuestion').setValue(markupIndicators.expireLower);
        this.markupOnSellVal = markupIndicators.markupOnSell;
        if (this.markupOnSellVal) {
          this.showMarkupOnSell = true;
        } else {
          this.showMarkupOnSell = this.scanGridforPercent();
          if (!this.showMarkupOnSell) {
            this.markupOnSellVal = false;
          }
        }
        if (this.isFurtheranceMode) {
          this.isMarkupUnitDisabled = this.fetchMarkupUnitState(this.markupAccordions);
        }
        if (markupIndicators.expireLower === null) {
          this.isSubmitDisabled = true;
        } else {
          this.isSubmitDisabled = false;
        }
        if (!this.determineFurtheranceMode) {
          this.createViewMarkup(this.markupAccordions);
          this.createItemLevelViewMarkup(this.markupAccordions);
          this.createSubgroupViewMarkup(this.markupAccordions);
        } else {
          this.markupAccordions.forEach(markupGrid => {
            if (markupGrid.isMarkupSaved) {
              this.copyMarkups.push({ markupName: markupGrid.markupName, gfsCustomerId: markupGrid.gfsCustomerId });
            }
          });
        }
        this.showSpinner = false;
      }));
  }

  fetchMarkupUnitState(markupAccordions) {
    for ( let accordion of markupAccordions) {
      if (accordion.productMarkupList.every( item => item.unit === UNIT_TYPES.PERCENT )) {
        continue;
      } else {
        return false
      }
    }
    return true
    }

  onClickExpireLower() {
    this.isSubmitDisabled = false;
  }

  createViewMarkup(markupAccordions) {
    markupAccordions.forEach((markupGrid, index) => {
      markupGrid.isCompleted = true;
      markupGrid.productMarkupList = markupGrid.productMarkupList.map(markupGridObj => {
        let viewMarkupGridObj = {};
        viewMarkupGridObj['productType'] = markupGridObj.productType;
        viewMarkupGridObj['itemPriceId'] = markupGridObj.itemPriceId;
        viewMarkupGridObj['markupType'] = markupGridObj.markupType;
        viewMarkupGridObj['effectiveDate'] = markupGridObj.effectiveDate;
        viewMarkupGridObj['expirationDate'] = markupGridObj.expirationDate;
        if (markupGridObj.unit === UNIT_TYPES.PERCENT) {
          viewMarkupGridObj['markup'] = markupGridObj.markup + markupGridObj.unit;
        } else {
          viewMarkupGridObj['markup'] = markupGridObj.unit + markupGridObj.markup;
        }
        return viewMarkupGridObj;
      });
    });
  }

  createItemLevelViewMarkup(markupAccordions) {
    markupAccordions.forEach((markupGrid, index) => {
      markupGrid.isCompleted = true;
      markupGrid.itemLevelMarkupList = markupGrid.itemLevelMarkupList.map(itemLevelMarkupGridObj => {
        let viewItemMarkupGridObj = {};
        viewItemMarkupGridObj['noItemId'] = itemLevelMarkupGridObj.noItemId;
        viewItemMarkupGridObj['itemId'] = itemLevelMarkupGridObj.itemId;
        viewItemMarkupGridObj['itemDesc'] = itemLevelMarkupGridObj.itemDesc;
        viewItemMarkupGridObj['stockingCode'] = itemLevelMarkupGridObj.stockingCode;
        viewItemMarkupGridObj['markupType'] = itemLevelMarkupGridObj.markupType;
        viewItemMarkupGridObj['effectiveDate'] = itemLevelMarkupGridObj.effectiveDate;
        viewItemMarkupGridObj['expirationDate'] = itemLevelMarkupGridObj.expirationDate;
        if (itemLevelMarkupGridObj.unit === UNIT_TYPES.PERCENT) {
          viewItemMarkupGridObj['markup'] = itemLevelMarkupGridObj.markup + itemLevelMarkupGridObj.unit;
        } else {
          viewItemMarkupGridObj['markup'] = itemLevelMarkupGridObj.unit + itemLevelMarkupGridObj.markup;
        }
        return viewItemMarkupGridObj;
      });
    });
  }

  createSubgroupViewMarkup(markupAccordions) {
    markupAccordions.forEach((markupGrid, index) => {
      markupGrid.isCompleted = true;
      markupGrid.subgroupMarkupList = markupGrid.subgroupMarkupList.map(subgroupMarkupGridObj => {
        let viewSubgroupMarkupGridObj = {};
        viewSubgroupMarkupGridObj['subgroupId'] = subgroupMarkupGridObj.subgroupId;
        viewSubgroupMarkupGridObj['subgroupDesc'] = subgroupMarkupGridObj.subgroupDesc;
        viewSubgroupMarkupGridObj['markupType'] = subgroupMarkupGridObj.markupType;
        viewSubgroupMarkupGridObj['effectiveDate'] = subgroupMarkupGridObj.effectiveDate;
        viewSubgroupMarkupGridObj['expirationDate'] = subgroupMarkupGridObj.expirationDate;
        if (subgroupMarkupGridObj.unit === UNIT_TYPES.PERCENT) {
          viewSubgroupMarkupGridObj['markup'] = subgroupMarkupGridObj.markup + subgroupMarkupGridObj.unit;
        } else {
          viewSubgroupMarkupGridObj['markup'] = subgroupMarkupGridObj.unit + subgroupMarkupGridObj.markup;
        }
        return viewSubgroupMarkupGridObj;
      });
    });
  }

  onEditMarkupGrid(editgfsCustomerId) {
    this.isNextDisabled = this.markupAccordions.some(markup => !markup.isMarkupSaved);
    let editCopy = this.copyMarkups.some(markup => markup.gfsCustomerId === editgfsCustomerId);
    if (editCopy) {
      this.copySelects.map(copyDropdown => {
        if (copyDropdown.nativeElement.value === editgfsCustomerId) {
          copyDropdown.nativeElement.value = '';
        }
      });
      this.copyMarkups.forEach((markups, index) => {
        if (markups.gfsCustomerId === editgfsCustomerId) {
          let removeIndex = this.copyMarkups.indexOf(markups);
          this.copyMarkups.splice(removeIndex, 1);
        }
      });
    }
  }

  onSaveMarkupGridDetails(saveMarkupGridDetails, actionId?: string) {
    this.actionId = actionId;
    this.saveGridDetailsObj = saveMarkupGridDetails;
    if (this.isPricingExhibitAttached && !this.isFurtheranceMode) {
      $('#deletePricingExhibit').modal('show');
    } else {
    this.saveMarkupGridModel = saveMarkupGridDetails;
    if (this.isFurtheranceMode) {
      this.saveMarkupGridModel.cppFurtheranceSeq =  this.cppFurtheranceSeq;
      this.subscription.push(this._furtheranceService.saveMarkupGridForFurtherance(this.saveMarkupGridModel)
      .subscribe(() => {
      this.setMarkupValuesForSave(saveMarkupGridDetails);
    }));
    } else {
      this.subscription.push(this._markupService.saveMarkupGridDetails(this.saveMarkupGridModel)
      .subscribe(() => {
        this.setMarkupValuesForSave(saveMarkupGridDetails);
        let copyPush = this.copyMarkups.some(markup => markup.gfsCustomerId === saveMarkupGridDetails.gfsCustomerId);
        if (!copyPush) {
          this.copyMarkups.push({ markupName: saveMarkupGridDetails.markupName, gfsCustomerId: saveMarkupGridDetails.gfsCustomerId });
        }
        if (saveMarkupGridDetails.markupGridIndex === 0) {
          this.disableAddException = false;
        }
      }));
  }}}

  setMarkupValuesForSave(saveMarkupGridDetails) {
    saveMarkupGridDetails.itemLevelMarkupList.forEach(itemLevel => {itemLevel.isItemSaved = true});
    saveMarkupGridDetails.isMarkupSaved = true;
    saveMarkupGridDetails.subgroupMarkupList.forEach(subgroup => {subgroup.isSubgroupSaved = true});
    this.isNextDisabled = this.markupAccordions.some(element => !element.isMarkupSaved);
  }

  navigateToUrl() {
    if (this.selectedContractType !== CONTRACT_TYPES.IFS &&
       this.selectedContractType !== CONTRACT_TYPES.IFS_AMENDMENT) {
      this._router.navigate([STEPPER_URL.SPLIT_CASE_URL], { relativeTo: this._route });
    } else {
      this._router.navigate([STEPPER_URL.REVIEW_URL], { relativeTo: this._route });
    }
  }

  buildMarkupOnSellDetails() {
    const contractDetails = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.markupIndicatorsModel.contractPriceProfileSeq = this.contractPriceProfileId;
    this.markupIndicatorsModel.effectiveDate =contractDetails.pstdate;
    this.markupIndicatorsModel.expirationDate = contractDetails.penddate;
    this.markupIndicatorsModel.expireLower = this.markupForm.get('expireLowerQuestion').value;
    this.markupIndicatorsModel.markupOnSell = this.markupOnSellVal;
  }

  onSubmit(actionId?: string) {
    this.actionId = actionId;
    if (this.isPricingExhibitAttached && !this.isFurtheranceMode) {
      $('#deletePricingExhibit').modal('show');
    } else if (!this.isFurtheranceMode) {
        this.showSpinner = true;
        this.buildMarkupOnSellDetails();
        this.subscription.push(this._markupService.saveMarkupIndicators(this.markupIndicatorsModel)
          .subscribe(response => {
            this.showSpinner = false;
            this.navigateToUrl();
      }, error => {
        this.showSpinner = false;
      }));
  } else {
    this.navigateToUrl();
  }}

  toggleCollapse($ele) {
    const element = $ele.querySelector('.fa');
    if (element.classList[1] === 'fa-angle-down') {
      this._renderer.removeClass(element, 'fa-angle-down');
      this._renderer.addClass(element, 'fa-angle-right');
    } else {
      this._renderer.removeClass(element, 'fa-angle-right');
      this._renderer.addClass(element, 'fa-angle-down');
    }
  }

  onAddException(actionId?: string) {
    this.actionId = actionId;
    if (this.isPricingExhibitAttached) {
      $('#deletePricingExhibit').modal('show');
    } else {
    const exceptionName = (this.markupModalForm.get('markupStructure').value).trim();
    this.subscription.push(this._markupService.addExceptionDetails(this.contractPriceProfileId, exceptionName,
      this.effectiveDate, this.expirationDate)
      .subscribe((exceptionDetails) => {
        this.markupAccordions.push(exceptionDetails);
        this.isNextDisabled = true;
        this.markupModalForm.reset()
      }));
  }}

  onCancelException() {
    this.markupModalForm.reset();
    this.editmarkupModalForm.reset();
  }

  onCopyFrom($event) {
    let currentIndex = $event.getAttribute('copyIndex');
    let markups = this.markupAccordions[currentIndex];
    let gfsCustomerId = $event.value;
    this.markupAccordions.forEach((markupGridDetails) => {
      if (markupGridDetails.gfsCustomerId === gfsCustomerId) {
        markups.productMarkupList = JSON.parse(JSON.stringify(markupGridDetails.productMarkupList));
      }
    });
    this.showMarkupOnSell = this.scanGridforPercent();
    if (!this.showMarkupOnSell) {
      this.markupOnSellVal = false;
    }
    markups.isMarkupSaved = false;
  }

  toggleMarkupOnSell(selectedValue) {
    this.markupOnSellVal = selectedValue;
  }

  scanGridforPercentCount() {
    let percentCount = 0;
    this.markupAccordions.forEach((ele) => {
      ele.productMarkupList.forEach((gridData) => {
        if (gridData.unit === UNIT_TYPES.PERCENT) {
          percentCount++;
        }
      });
      ele.itemLevelMarkupList.forEach((itemLevelData) => {
        if (itemLevelData.unit === UNIT_TYPES.PERCENT) {
          percentCount++;
        }
      });
      ele.subgroupMarkupList.forEach((subgroupData) => {
        if (subgroupData.unit === UNIT_TYPES.PERCENT) {
          percentCount++;
        }
      });
    });
    return percentCount;
  }

  setMarkupOnSell(value) {
    if (value === UNIT_TYPES.PERCENT) {
      this.showMarkupOnSell = true;
      if (this.scanGridforPercentCount() === ONE) {
        this.markupOnSellVal = true;
      }
    } else {
      this.showMarkupOnSell = this.scanGridforPercent();
      if (!this.showMarkupOnSell) {
        this.markupOnSellVal = false;
      }
    }
  }

  scanGridforPercent() {
    return this.markupAccordions.map((element) => {
      return element.productMarkupList.some(rowDetail => rowDetail.unit === UNIT_TYPES.PERCENT)
        || element.itemLevelMarkupList.some(rowDetail => rowDetail.unit === UNIT_TYPES.PERCENT)
        || element.subgroupMarkupList.some(rowDetail => rowDetail.unit === UNIT_TYPES.PERCENT)
    }).some(e => e);
  }

  onEdit(editElement) {
    const editTitle = this._translatorService.translate('MARKUP_LABELS.EDIT');
    let markupName = editElement.getAttribute('markupName');
    let gfsCustomerId = editElement.getAttribute('gfsCustomerId');
    this.editModalTitle.nativeElement.textContent = editTitle + ' "' + markupName + '"';
    this.editModalInput.nativeElement.value = markupName;
    this._renderer.setAttribute(this.editModalInput.nativeElement, 'gfsCustomerId', gfsCustomerId);
    this._renderer.setAttribute(this.editModalInput.nativeElement, 'markupName', markupName);
  }

  onRenameMarkupException(actionId?: string) {
    this.actionId = actionId;
    if (this.isPricingExhibitAttached) {
      $('#deletePricingExhibit').modal('show');
    } else {
    let selectedgfsCustomerId = this.editModalInput.nativeElement.getAttribute('gfsCustomerId');
    let selectedExceptionName = this.editModalInput.nativeElement.getAttribute('markupName');
    let newExceptionName = (this.editmarkupModalForm.get('editMarkupStructure').value).trim();
    this.renameMarkupModel.contractPriceProfileSeq = this.contractPriceProfileId;
    this.renameMarkupModel.gfsCustomerId = selectedgfsCustomerId;
    this.renameMarkupModel.exceptionName = selectedExceptionName;
    this.renameMarkupModel.newExceptionName = newExceptionName;

    this.subscription.push(this._markupService.renameMarkupException(this.renameMarkupModel)
      .subscribe(() => {
        this.markupAccordions.forEach((markupGridDetails) => {
          if (markupGridDetails.gfsCustomerId == selectedgfsCustomerId) {
            markupGridDetails.markupName = newExceptionName;
            this.editmarkupModalForm.reset();
          }
        });
      }));
  }}

  onDeleteClick() {
    const deleteTitle = this._translatorService.translate('MARKUP_LABELS.DELETE');
    $('#editmarkupModal').modal('hide');
    $('#deleteModal').on('shown.bs.modal', () => {
      this.deleteExceptionBtn.nativeElement.focus();
      });
    let toDeletemarkupName = this.editModalInput.nativeElement.getAttribute('markupName');
    this.deleteModalTitle.nativeElement.textContent = deleteTitle + ' "' + toDeletemarkupName + '"?';
  }

  onDeleteException(actionId?: string) {
    this.actionId = actionId;
    if (this.isPricingExhibitAttached) {
      $('#deletePricingExhibit').modal('show');
    } else {
    const deleteSuccessMsg = this._translatorService.translate('TOASTER_MESSAGES.DELETE_SUCCESS');
    let deletegfsCustomerId = this.editModalInput.nativeElement.getAttribute('gfsCustomerId');
    let deletemarkupName = this.editModalInput.nativeElement.getAttribute('markupName');
    this.subscription.push(this._markupService.deleteMarkupException(this.contractPriceProfileId, deletegfsCustomerId, deletemarkupName)
      .subscribe(() => {
        this.markupAccordions.forEach((markupGridDetails, index) => {
          if (markupGridDetails.gfsCustomerId == deletegfsCustomerId) {
            this.markupAccordions.splice(index, 1);
            this.showMarkupOnSell = this.scanGridforPercent();
            if (!this.showMarkupOnSell) {
              this.markupOnSellVal = false;
            }
            this.onEditMarkupGrid(deletegfsCustomerId);
            this._toaster.showSuccess('"' + deletemarkupName + '"' + deleteSuccessMsg, '');
          }
        });
      }));
  }}

  onItemLevelDeleteInfo(itemLevelDeleteInfo: ItemLevelDeleteModel) {
    const deleteTitle = this._translatorService.translate('MARKUP_LABELS.DELETE');
    if (itemLevelDeleteInfo.itemDescription) {
      this.deleteItemModalTitle.nativeElement.textContent = deleteTitle + ' "' + itemLevelDeleteInfo.itemDescription + '" ?';
    } else {
      this.deleteItemModalTitle.nativeElement.textContent = deleteTitle + ' ?';
    }
    this._renderer.setAttribute(this.deleteItemModalTitle.nativeElement, 'itemDesc', itemLevelDeleteInfo.itemDescription);
    this._renderer.setAttribute(this.deleteItemModalTitle.nativeElement, 'itemId', itemLevelDeleteInfo.itemNo);
    this._renderer.setAttribute(this.deleteItemModalTitle.nativeElement, 'itemIndex', String(itemLevelDeleteInfo.itemIndex));
    this._renderer.setAttribute(this.deleteItemModalTitle.nativeElement, 'gfsCustomerId', String(itemLevelDeleteInfo.gfsCustomerId));
    this._renderer.setAttribute(this.deleteItemModalTitle.nativeElement, 'gfsCustomerType', String(itemLevelDeleteInfo.gfsCustomerType));
    $('#itemDeleteModal').modal('show');
    $('#itemDeleteModal').on('shown.bs.modal', () => {
      this.deleteItemBtn.nativeElement.focus();
      });
  }

  deleteItemMarkup(actionId?: string) {
    this.actionId = actionId;
    if (this.isPricingExhibitAttached && !this.isFurtheranceMode) {
      $('#deletePricingExhibit').modal('show');
    } else {
    let deleteItemId = this.deleteItemModalTitle.nativeElement.getAttribute('itemId');
    let deleteItemIndex = this.deleteItemModalTitle.nativeElement.getAttribute('itemIndex');
    let deleteItemDesc = this.deleteItemModalTitle.nativeElement.getAttribute('itemDesc');
    let gfsCustomerId = this.deleteItemModalTitle.nativeElement.getAttribute('gfsCustomerId');
    let gfsCustomerType = this.deleteItemModalTitle.nativeElement.getAttribute('gfsCustomerType');
    if (this.isFurtheranceMode) {
      this.deleteItemLevelFurtherance( deleteItemId, deleteItemIndex, deleteItemDesc, gfsCustomerId, gfsCustomerType)
    } else {
    this.subscription.push(this._markupService.deleteItemLevelMarkup(this.contractPriceProfileId, gfsCustomerId, gfsCustomerType,
      deleteItemId, deleteItemDesc)
      .subscribe(() => {
         this.showDeleteItemSuccessMsg(deleteItemIndex, gfsCustomerId, deleteItemDesc);
        this.showMarkupOnSell = this.scanGridforPercent();
            if (!this.showMarkupOnSell) {
              this.markupOnSellVal = false;
            }
      }));
  }}}

  deleteItemLevelFurtherance(deleteItemId, deleteItemIndex, deleteItemDesc, gfsCustomerId, gfsCustomerType) {
    const contractInfo = JSON.parse(sessionStorage.getItem('contractInfo'));
    this.cppFurtheranceSeq = contractInfo.cppFurtheranceSeq;
    this.subscription.push(this._furtheranceService.deleteItemLevelMarkupFurtherance(this.contractPriceProfileId, this.cppFurtheranceSeq,
      gfsCustomerId, gfsCustomerType, deleteItemId, deleteItemDesc)
    .subscribe(() => {
      this.showDeleteItemSuccessMsg(deleteItemIndex, gfsCustomerId, deleteItemDesc);
    }));
  }

  showDeleteItemSuccessMsg( deleteItemIndex, gfsCustomerId, deleteItemDesc) {
    let deleteSuccessMsg = this._translatorService.translate('TOASTER_MESSAGES.DELETE_SUCCESS');
    this.markupAccordions.forEach(markupGrid => {
      if (markupGrid.gfsCustomerId === gfsCustomerId) {
        markupGrid.itemLevelMarkupList.splice(deleteItemIndex, 1);
      }
    });
    if (!deleteItemDesc) {
      deleteItemDesc = this._translatorService.translate('TOASTER_MESSAGES.ITEM_LEVEL_MARKUP');
    }
    this._toaster.showSuccess('"' + deleteItemDesc + '"' + deleteSuccessMsg, '');
  }

  onSubgroupDeleteInfo(subgroupDeleteInfo: SubgroupMarkupDeleteModel) {
    const deleteTitle = this._translatorService.translate('MARKUP_LABELS.DELETE');
    if (subgroupDeleteInfo.subgroupDesc) {
      this.deleteSubgroupModalTitle.nativeElement.textContent = deleteTitle + ' "' + subgroupDeleteInfo.subgroupDesc + '" ?';
    } else {
      this.deleteSubgroupModalTitle.nativeElement.textContent = deleteTitle + ' ?';
    }
    this._renderer.setAttribute(this.deleteSubgroupModalTitle.nativeElement, 'subgroupDesc', subgroupDeleteInfo.subgroupDesc);
    this._renderer.setAttribute(this.deleteSubgroupModalTitle.nativeElement, 'subgroupId', subgroupDeleteInfo.subgroupId);
    this._renderer.setAttribute(this.deleteSubgroupModalTitle.nativeElement, 'subgroupIndex', String(subgroupDeleteInfo.subgroupIndex));
    this._renderer.setAttribute(this.deleteSubgroupModalTitle.nativeElement, 'gfsCustomerId', String(subgroupDeleteInfo.gfsCustomerId));
    this._renderer.setAttribute(this.deleteSubgroupModalTitle.nativeElement, 'gfsCustomerType', String(subgroupDeleteInfo.gfsCustomerType));
    $('#subgroupDeleteModal').modal('show');
    $('#subgroupDeleteModal').on('shown.bs.modal', () => {
      this.deleteSubgroupBtn.nativeElement.focus();
      });
  }

  deleteSubgroupMarkup(actionId?: string) {
    this.actionId = actionId;
    if (this.isPricingExhibitAttached) {
      $('#deletePricingExhibit').modal('show');
    } else {
    let deleteSuccessMsg = this._translatorService.translate('TOASTER_MESSAGES.DELETE_SUCCESS');
    let deleteSubgroupId = this.deleteSubgroupModalTitle.nativeElement.getAttribute('subgroupId');
    let deleteSubgroupIndex = this.deleteSubgroupModalTitle.nativeElement.getAttribute('subgroupIndex');
    let deleteSubgroupDesc = this.deleteSubgroupModalTitle.nativeElement.getAttribute('subgroupDesc');
    let gfsCustomerId = this.deleteSubgroupModalTitle.nativeElement.getAttribute('gfsCustomerId');
    let gfsCustomerType = this.deleteSubgroupModalTitle.nativeElement.getAttribute('gfsCustomerType');
     this.subscription.push(this._markupService.deleteSubgroupMarkup(this.contractPriceProfileId, gfsCustomerId, gfsCustomerType,
      deleteSubgroupId)
      .subscribe(() => {
        this.markupAccordions.forEach(markupGrid => {
          if (markupGrid.gfsCustomerId === gfsCustomerId) {
            markupGrid.subgroupMarkupList.splice(deleteSubgroupIndex, 1);
          }
        });
        if (!deleteSubgroupDesc) {
          deleteSubgroupDesc = this._translatorService.translate('TOASTER_MESSAGES.SUBGROUP_MARKUP');
        }
        this._toaster.showSuccess('"' + deleteSubgroupDesc + '"' + deleteSuccessMsg, '');
        this.showMarkupOnSell = this.scanGridforPercent();
            if (!this.showMarkupOnSell) {
              this.markupOnSellVal = false;
            }
      }));
  }}

  determineFurtheranceIndicator() {
    if (this.displayViewMode && this.canEditFurtherance) {
      this.determineFurtheranceMode = true;
    } else if (this.displayViewMode && !this.canEditFurtherance) {
      this.determineFurtheranceMode = false;
    } else if (!this.displayViewMode && !this.canEditFurtherance) {
      this.determineFurtheranceMode = true;
    } else if (!this.displayViewMode && this.canEditFurtherance) {
      this.determineFurtheranceMode = true;
    }
  }

  onExhibitDeletion(isExhibitDeleted) {
    if (isExhibitDeleted) {
      this.showSpinner = false;
      this.isPricingExhibitAttached = false;
      switch (this.actionId) {
      case 'ItemMarkupDeletion': {
        this.deleteItemMarkup();
        break;
      }
      case 'SubgroupMarkupDeletion': {
        this.deleteSubgroupMarkup();
        break;
      }
      case 'SaveMarkup': {
        this.onSaveMarkupGridDetails(this.saveGridDetailsObj, this.actionId)
        break;
      }
      case 'RenameExceptionMarkup': {
        this.onRenameMarkupException();
        break;
      }
      case 'ExceptionDeletion': {
        this.onDeleteException();
        break;
      }
      case 'AddException': {
        this.onAddException();
        break;
      }
      case 'Submit': {
        this.onSubmit();
        break;
      }}
    } else {
      this.showSpinner = true;
    }
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }
}
