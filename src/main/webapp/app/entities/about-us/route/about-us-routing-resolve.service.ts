import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAboutUs } from '../about-us.model';
import { AboutUsService } from '../service/about-us.service';

const aboutUsResolve = (route: ActivatedRouteSnapshot): Observable<null | IAboutUs> => {
  const id = route.params['id'];
  if (id) {
    return inject(AboutUsService)
      .find(id)
      .pipe(
        mergeMap((aboutUs: HttpResponse<IAboutUs>) => {
          if (aboutUs.body) {
            return of(aboutUs.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default aboutUsResolve;
