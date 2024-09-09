import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILoyaltyProgram } from '../loyalty-program.model';
import { LoyaltyProgramService } from '../service/loyalty-program.service';

const loyaltyProgramResolve = (route: ActivatedRouteSnapshot): Observable<null | ILoyaltyProgram> => {
  const id = route.params['id'];
  if (id) {
    return inject(LoyaltyProgramService)
      .find(id)
      .pipe(
        mergeMap((loyaltyProgram: HttpResponse<ILoyaltyProgram>) => {
          if (loyaltyProgram.body) {
            return of(loyaltyProgram.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default loyaltyProgramResolve;
