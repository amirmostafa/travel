import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AgencyComponent } from './list/agency.component';
import { AgencyDetailComponent } from './detail/agency-detail.component';
import { AgencyUpdateComponent } from './update/agency-update.component';
import AgencyResolve from './route/agency-routing-resolve.service';

const agencyRoute: Routes = [
  {
    path: '',
    component: AgencyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgencyDetailComponent,
    resolve: {
      agency: AgencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgencyUpdateComponent,
    resolve: {
      agency: AgencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgencyUpdateComponent,
    resolve: {
      agency: AgencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default agencyRoute;
