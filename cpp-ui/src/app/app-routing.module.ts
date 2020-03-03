import { NgModule } from '@angular/core';
import { Routes, RouterModule, PreloadAllModules } from '@angular/router';

import { AssignmentHeaderComponent } from './assignment-header/assignment-header.component';
import { AssignmentsComponent } from './assignment-header/assignments/assignments.component';
import { CostModelComponent } from './cost-model/cost-model.component';
import { ContractSearchComponent } from 'app/contract-search';
import { DistributionCentersComponent } from './contract/distribution-centers/distribution-centers.component';
import { ErrorScreenComponent } from './contract/error-screen/error-screen.component';
import { FurtheranceInformationComponent } from './furtherance';
import { IfsContractGuard } from './shared';
import { ItemAssignmentsComponent } from './assignment-header/item-assignments/item-assignments.component';
import { MarkupComponent } from './contract/markup/markup.component';
import { PricingInformationComponent } from './contract/pricing-information/pricing-information.component';
import { PricingStatusGuard } from './shared';
import { ReviewComponent } from './contract/review/review.component';
import { SplitCaseComponent } from './contract/split-case/split-case.component';
import { StepperComponent } from './contract/stepper/stepper.component';


export const ROUTES: Routes = [
  {
    path: 'assignments', component: AssignmentHeaderComponent, canActivate: [PricingStatusGuard],
    children: [
      { path: 'customers', component: AssignmentsComponent },
      { path: 'items', component: ItemAssignmentsComponent },
      { path: '', redirectTo: 'customers', pathMatch: 'full' }
    ]
  },
  {
    path: '', component: StepperComponent,
    children: [
      { path: 'pricinginformation', component: PricingInformationComponent },
      { path: 'distributioncenters', component: DistributionCentersComponent },
      { path: 'markup', component: MarkupComponent},
      { path: 'splitcasefee', component: SplitCaseComponent, canActivate: [IfsContractGuard]},
      { path: 'review', component: ReviewComponent},
      { path: '', redirectTo: 'pricinginformation', pathMatch: 'full' }
    ]
  },
  { path: 'costinformation', component: CostModelComponent, canActivate: [PricingStatusGuard]},
  { path: 'search', component: ContractSearchComponent },
  { path: 'furtheranceinformation', component: FurtheranceInformationComponent },
  { path: 'error', component: ErrorScreenComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' }

];

@NgModule({
  imports: [RouterModule.forRoot(ROUTES, { useHash: true, preloadingStrategy: PreloadAllModules })],
  exports: [RouterModule]
})

export class AppRoutingModule {}
