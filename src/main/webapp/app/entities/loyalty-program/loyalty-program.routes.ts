import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LoyaltyProgramComponent } from './list/loyalty-program.component';
import { LoyaltyProgramDetailComponent } from './detail/loyalty-program-detail.component';
import { LoyaltyProgramUpdateComponent } from './update/loyalty-program-update.component';
import LoyaltyProgramResolve from './route/loyalty-program-routing-resolve.service';

const loyaltyProgramRoute: Routes = [
  {
    path: '',
    component: LoyaltyProgramComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoyaltyProgramDetailComponent,
    resolve: {
      loyaltyProgram: LoyaltyProgramResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoyaltyProgramUpdateComponent,
    resolve: {
      loyaltyProgram: LoyaltyProgramResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoyaltyProgramUpdateComponent,
    resolve: {
      loyaltyProgram: LoyaltyProgramResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default loyaltyProgramRoute;
