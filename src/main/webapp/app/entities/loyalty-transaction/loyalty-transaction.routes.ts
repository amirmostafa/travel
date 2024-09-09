import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LoyaltyTransactionComponent } from './list/loyalty-transaction.component';
import { LoyaltyTransactionDetailComponent } from './detail/loyalty-transaction-detail.component';
import { LoyaltyTransactionUpdateComponent } from './update/loyalty-transaction-update.component';
import LoyaltyTransactionResolve from './route/loyalty-transaction-routing-resolve.service';

const loyaltyTransactionRoute: Routes = [
  {
    path: '',
    component: LoyaltyTransactionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoyaltyTransactionDetailComponent,
    resolve: {
      loyaltyTransaction: LoyaltyTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoyaltyTransactionUpdateComponent,
    resolve: {
      loyaltyTransaction: LoyaltyTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoyaltyTransactionUpdateComponent,
    resolve: {
      loyaltyTransaction: LoyaltyTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default loyaltyTransactionRoute;
