import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITourPackage } from '../tour-package.model';
import { TourPackageService } from '../service/tour-package.service';

const tourPackageResolve = (route: ActivatedRouteSnapshot): Observable<null | ITourPackage> => {
  const id = route.params['id'];
  if (id) {
    return inject(TourPackageService)
      .find(id)
      .pipe(
        mergeMap((tourPackage: HttpResponse<ITourPackage>) => {
          if (tourPackage.body) {
            return of(tourPackage.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tourPackageResolve;
