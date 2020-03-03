// Ng imports
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { DecimalPipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

// third party
import { NgbModule, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ToastrModule } from 'ngx-toastr';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';


//  cpp imports
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AssignmentsComponent } from './assignment-header';
import { AssignmentHeaderComponent } from './assignment-header';
import { AssignmentsService, AuthorizationService } from './shared';
import { ContractInformationComponent } from './contract';
import { CostModelComponent } from './cost-model';
import { CostModelService } from './shared';
import { ContractSearchService } from './shared';
import { ContractSearchComponent } from './contract-search';
import { ContractSearchResultGridComponent } from './contract-search';
import { CustomerAssignmentsComponent } from './assignment-header';
import { CppHttpInterceptor } from './shared';
import { DeleteExhibitComponent } from './contract';
import { DistributionCenterService } from './shared';
import { DistributionCentersComponent } from './contract';
import { ErrorScreenComponent } from './contract';
import { ErrorService } from './shared';
import { FurtheranceService } from './shared';
import { HeaderComponent } from './layout';
import { FooterComponent } from './layout';
import { FurtheranceInformationComponent } from './furtherance';
import { IfsContractGuard } from './shared';
import { ItemAssignmentsComponent } from './assignment-header';
import { ItemLevelMarkupComponent } from './contract';
import { ItemAssignmentService } from './shared';
import { ItemDisplayComponent } from './assignment-header';
import { ItemSearchComponent } from './assignment-header';
import { MarkupComponent } from './contract';
import { MarkupDisplayComponent } from './assignment-header';
import { MarkupGridComponent } from './contract';
import { MarkupService } from './shared';
import { NgbDateMomentParserFormatter } from './shared';
import { PricingInformationComponent } from './contract';
import { PricingInformationService } from './shared';
import { PricingStatusGuard } from './shared';
import { RealCustomerSearchComponent } from './assignment-header';
import { ReviewService } from './shared';
import { ReviewComponent } from './contract';
import { SplitCaseFeeService } from './shared';
import { StepperService } from './shared';
import { StepperComponent } from './contract';
import { SplitCaseComponent } from './contract';
import { StringLengthPipe } from './shared';
import { SubgroupMarkupComponent } from './contract';
import { ToggleComponent } from './shared';
import { ToasterService } from './shared';
import { TranslatorService } from './shared';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

// AoT requires an exported function for factories
export function NgbDateMomentParserFormatterFactory() {
  return new NgbDateMomentParserFormatter('MM/DD/YYYY');
}

@NgModule({
  declarations: [
    AppComponent,
    AssignmentsComponent,
    AssignmentHeaderComponent,
    ContractInformationComponent,
    CostModelComponent,
    ContractSearchComponent,
    ContractSearchResultGridComponent,
    PricingInformationComponent,
    CustomerAssignmentsComponent,
    DeleteExhibitComponent,
    DistributionCentersComponent,
    ErrorScreenComponent,
    HeaderComponent,
    FooterComponent,
    FurtheranceInformationComponent,
    ItemAssignmentsComponent,
    ItemLevelMarkupComponent,
    ItemDisplayComponent,
    ItemSearchComponent,
    MarkupComponent,
    MarkupGridComponent,
    MarkupDisplayComponent,
    PricingInformationComponent,
    RealCustomerSearchComponent,
    ReviewComponent,
    SplitCaseComponent,
    StringLengthPipe,
    StepperComponent,
    SubgroupMarkupComponent,
    ToggleComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxDatatableModule,
    BrowserAnimationsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    AppRoutingModule,
    NgbModule.forRoot(),
    ToastrModule.forRoot()
  ],
  providers: [
    {
      provide: NgbDateParserFormatter,
      useFactory: NgbDateMomentParserFormatterFactory,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CppHttpInterceptor,
      multi: true
    },
    AssignmentsService,
    AuthorizationService,
    CostModelService,
    ContractSearchService,
    DecimalPipe,
    DistributionCenterService,
    ErrorService,
    FurtheranceService,
    IfsContractGuard,
    ItemAssignmentService,
    MarkupService,
    PricingInformationService,
    PricingStatusGuard,
    ReviewService,
    SplitCaseFeeService,
    StepperService,
    TranslatorService,
    ToasterService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
