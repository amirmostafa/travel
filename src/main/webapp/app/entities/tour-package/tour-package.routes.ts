import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TourPackageComponent } from './list/tour-package.component';
import { TourPackageDetailComponent } from './detail/tour-package-detail.component';
import { TourPackageUpdateComponent } from './update/tour-package-update.component';
import TourPackageResolve from './route/tour-package-routing-resolve.service';

const tourPackageRoute: Routes = [
  {
    path: '',
    component: TourPackageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TourPackageDetailComponent,
    resolve: {
      tourPackage: TourPackageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TourPackageUpdateComponent,
    resolve: {
      tourPackage: TourPackageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TourPackageUpdateComponent,
    resolve: {
      tourPackage: TourPackageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tourPackageRoute;
