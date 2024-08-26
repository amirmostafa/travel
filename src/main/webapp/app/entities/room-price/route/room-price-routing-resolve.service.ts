import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoomPrice } from '../room-price.model';
import { RoomPriceService } from '../service/room-price.service';

const roomPriceResolve = (route: ActivatedRouteSnapshot): Observable<null | IRoomPrice> => {
  const id = route.params['id'];
  if (id) {
    return inject(RoomPriceService)
      .find(id)
      .pipe(
        mergeMap((roomPrice: HttpResponse<IRoomPrice>) => {
          if (roomPrice.body) {
            return of(roomPrice.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default roomPriceResolve;
