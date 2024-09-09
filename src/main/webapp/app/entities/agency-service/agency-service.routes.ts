import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AgencyServiceComponent } from './list/agency-service.component';
import { AgencyServiceDetailComponent } from './detail/agency-service-detail.component';
import { AgencyServiceUpdateComponent } from './update/agency-service-update.component';
import AgencyServiceResolve from './route/agency-service-routing-resolve.service';

const agencyServiceRoute: Routes = [
  {
    path: '',
    component: AgencyServiceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgencyServiceDetailComponent,
    resolve: {
      agencyService: AgencyServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgencyServiceUpdateComponent,
    resolve: {
      agencyService: AgencyServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgencyServiceUpdateComponent,
    resolve: {
      agencyService: AgencyServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default agencyServiceRoute;
