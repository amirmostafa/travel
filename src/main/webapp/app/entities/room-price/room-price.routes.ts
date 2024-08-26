import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RoomPriceComponent } from './list/room-price.component';
import { RoomPriceDetailComponent } from './detail/room-price-detail.component';
import { RoomPriceUpdateComponent } from './update/room-price-update.component';
import RoomPriceResolve from './route/room-price-routing-resolve.service';

const roomPriceRoute: Routes = [
  {
    path: '',
    component: RoomPriceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomPriceDetailComponent,
    resolve: {
      roomPrice: RoomPriceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomPriceUpdateComponent,
    resolve: {
      roomPrice: RoomPriceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomPriceUpdateComponent,
    resolve: {
      roomPrice: RoomPriceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default roomPriceRoute;
