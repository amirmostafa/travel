import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoomPrice, NewRoomPrice } from '../room-price.model';

export type PartialUpdateRoomPrice = Partial<IRoomPrice> & Pick<IRoomPrice, 'id'>;

type RestOf<T extends IRoomPrice | NewRoomPrice> = Omit<T, 'fromDate' | 'toDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
};

export type RestRoomPrice = RestOf<IRoomPrice>;

export type NewRestRoomPrice = RestOf<NewRoomPrice>;

export type PartialUpdateRestRoomPrice = RestOf<PartialUpdateRoomPrice>;

export type EntityResponseType = HttpResponse<IRoomPrice>;
export type EntityArrayResponseType = HttpResponse<IRoomPrice[]>;

@Injectable({ providedIn: 'root' })
export class RoomPriceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/room-prices');

  create(roomPrice: NewRoomPrice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomPrice);
    return this.http
      .post<RestRoomPrice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(roomPrice: IRoomPrice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomPrice);
    return this.http
      .put<RestRoomPrice>(`${this.resourceUrl}/${this.getRoomPriceIdentifier(roomPrice)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(roomPrice: PartialUpdateRoomPrice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomPrice);
    return this.http
      .patch<RestRoomPrice>(`${this.resourceUrl}/${this.getRoomPriceIdentifier(roomPrice)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRoomPrice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRoomPrice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoomPriceIdentifier(roomPrice: Pick<IRoomPrice, 'id'>): number {
    return roomPrice.id;
  }

  compareRoomPrice(o1: Pick<IRoomPrice, 'id'> | null, o2: Pick<IRoomPrice, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoomPriceIdentifier(o1) === this.getRoomPriceIdentifier(o2) : o1 === o2;
  }

  addRoomPriceToCollectionIfMissing<Type extends Pick<IRoomPrice, 'id'>>(
    roomPriceCollection: Type[],
    ...roomPricesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roomPrices: Type[] = roomPricesToCheck.filter(isPresent);
    if (roomPrices.length > 0) {
      const roomPriceCollectionIdentifiers = roomPriceCollection.map(roomPriceItem => this.getRoomPriceIdentifier(roomPriceItem));
      const roomPricesToAdd = roomPrices.filter(roomPriceItem => {
        const roomPriceIdentifier = this.getRoomPriceIdentifier(roomPriceItem);
        if (roomPriceCollectionIdentifiers.includes(roomPriceIdentifier)) {
          return false;
        }
        roomPriceCollectionIdentifiers.push(roomPriceIdentifier);
        return true;
      });
      return [...roomPricesToAdd, ...roomPriceCollection];
    }
    return roomPriceCollection;
  }

  protected convertDateFromClient<T extends IRoomPrice | NewRoomPrice | PartialUpdateRoomPrice>(roomPrice: T): RestOf<T> {
    return {
      ...roomPrice,
      fromDate: roomPrice.fromDate?.format(DATE_FORMAT) ?? null,
      toDate: roomPrice.toDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRoomPrice: RestRoomPrice): IRoomPrice {
    return {
      ...restRoomPrice,
      fromDate: restRoomPrice.fromDate ? dayjs(restRoomPrice.fromDate) : undefined,
      toDate: restRoomPrice.toDate ? dayjs(restRoomPrice.toDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRoomPrice>): HttpResponse<IRoomPrice> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRoomPrice[]>): HttpResponse<IRoomPrice[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
