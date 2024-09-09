import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILoyaltyTransaction } from '../loyalty-transaction.model';
import { LoyaltyTransactionService } from '../service/loyalty-transaction.service';

const loyaltyTransactionResolve = (route: ActivatedRouteSnapshot): Observable<null | ILoyaltyTransaction> => {
  const id = route.params['id'];
  if (id) {
    return inject(LoyaltyTransactionService)
      .find(id)
      .pipe(
        mergeMap((loyaltyTransaction: HttpResponse<ILoyaltyTransaction>) => {
          if (loyaltyTransaction.body) {
            return of(loyaltyTransaction.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default loyaltyTransactionResolve;
