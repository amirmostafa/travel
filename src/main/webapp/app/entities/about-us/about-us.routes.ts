import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AboutUsComponent } from './list/about-us.component';
import { AboutUsDetailComponent } from './detail/about-us-detail.component';
import { AboutUsUpdateComponent } from './update/about-us-update.component';
import AboutUsResolve from './route/about-us-routing-resolve.service';

const aboutUsRoute: Routes = [
  {
    path: '',
    component: AboutUsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AboutUsDetailComponent,
    resolve: {
      aboutUs: AboutUsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AboutUsUpdateComponent,
    resolve: {
      aboutUs: AboutUsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AboutUsUpdateComponent,
    resolve: {
      aboutUs: AboutUsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aboutUsRoute;
