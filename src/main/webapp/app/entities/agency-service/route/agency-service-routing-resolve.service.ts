import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgencyService } from '../agency-service.model';
import { AgencyServiceService } from '../service/agency-service.service';

const agencyServiceResolve = (route: ActivatedRouteSnapshot): Observable<null | IAgencyService> => {
  const id = route.params['id'];
  if (id) {
    return inject(AgencyServiceService)
      .find(id)
      .pipe(
        mergeMap((agencyService: HttpResponse<IAgencyService>) => {
          if (agencyService.body) {
            return of(agencyService.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default agencyServiceResolve;
