import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILoyaltyTransaction, NewLoyaltyTransaction } from '../loyalty-transaction.model';

export type PartialUpdateLoyaltyTransaction = Partial<ILoyaltyTransaction> & Pick<ILoyaltyTransaction, 'id'>;

type RestOf<T extends ILoyaltyTransaction | NewLoyaltyTransaction> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestLoyaltyTransaction = RestOf<ILoyaltyTransaction>;

export type NewRestLoyaltyTransaction = RestOf<NewLoyaltyTransaction>;

export type PartialUpdateRestLoyaltyTransaction = RestOf<PartialUpdateLoyaltyTransaction>;

export type EntityResponseType = HttpResponse<ILoyaltyTransaction>;
export type EntityArrayResponseType = HttpResponse<ILoyaltyTransaction[]>;

@Injectable({ providedIn: 'root' })
export class LoyaltyTransactionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/loyalty-transactions');

  create(loyaltyTransaction: NewLoyaltyTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(loyaltyTransaction);
    return this.http
      .post<RestLoyaltyTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(loyaltyTransaction: ILoyaltyTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(loyaltyTransaction);
    return this.http
      .put<RestLoyaltyTransaction>(`${this.resourceUrl}/${this.getLoyaltyTransactionIdentifier(loyaltyTransaction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(loyaltyTransaction: PartialUpdateLoyaltyTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(loyaltyTransaction);
    return this.http
      .patch<RestLoyaltyTransaction>(`${this.resourceUrl}/${this.getLoyaltyTransactionIdentifier(loyaltyTransaction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLoyaltyTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLoyaltyTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLoyaltyTransactionIdentifier(loyaltyTransaction: Pick<ILoyaltyTransaction, 'id'>): number {
    return loyaltyTransaction.id;
  }

  compareLoyaltyTransaction(o1: Pick<ILoyaltyTransaction, 'id'> | null, o2: Pick<ILoyaltyTransaction, 'id'> | null): boolean {
    return o1 && o2 ? this.getLoyaltyTransactionIdentifier(o1) === this.getLoyaltyTransactionIdentifier(o2) : o1 === o2;
  }

  addLoyaltyTransactionToCollectionIfMissing<Type extends Pick<ILoyaltyTransaction, 'id'>>(
    loyaltyTransactionCollection: Type[],
    ...loyaltyTransactionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const loyaltyTransactions: Type[] = loyaltyTransactionsToCheck.filter(isPresent);
    if (loyaltyTransactions.length > 0) {
      const loyaltyTransactionCollectionIdentifiers = loyaltyTransactionCollection.map(loyaltyTransactionItem =>
        this.getLoyaltyTransactionIdentifier(loyaltyTransactionItem),
      );
      const loyaltyTransactionsToAdd = loyaltyTransactions.filter(loyaltyTransactionItem => {
        const loyaltyTransactionIdentifier = this.getLoyaltyTransactionIdentifier(loyaltyTransactionItem);
        if (loyaltyTransactionCollectionIdentifiers.includes(loyaltyTransactionIdentifier)) {
          return false;
        }
        loyaltyTransactionCollectionIdentifiers.push(loyaltyTransactionIdentifier);
        return true;
      });
      return [...loyaltyTransactionsToAdd, ...loyaltyTransactionCollection];
    }
    return loyaltyTransactionCollection;
  }

  protected convertDateFromClient<T extends ILoyaltyTransaction | NewLoyaltyTransaction | PartialUpdateLoyaltyTransaction>(
    loyaltyTransaction: T,
  ): RestOf<T> {
    return {
      ...loyaltyTransaction,
      date: loyaltyTransaction.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restLoyaltyTransaction: RestLoyaltyTransaction): ILoyaltyTransaction {
    return {
      ...restLoyaltyTransaction,
      date: restLoyaltyTransaction.date ? dayjs(restLoyaltyTransaction.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLoyaltyTransaction>): HttpResponse<ILoyaltyTransaction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLoyaltyTransaction[]>): HttpResponse<ILoyaltyTransaction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
