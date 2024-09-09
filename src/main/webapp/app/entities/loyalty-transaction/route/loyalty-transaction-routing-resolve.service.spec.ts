import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ILoyaltyTransaction } from '../loyalty-transaction.model';
import { LoyaltyTransactionService } from '../service/loyalty-transaction.service';

import loyaltyTransactionResolve from './loyalty-transaction-routing-resolve.service';

describe('LoyaltyTransaction routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: LoyaltyTransactionService;
  let resultLoyaltyTransaction: ILoyaltyTransaction | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(LoyaltyTransactionService);
    resultLoyaltyTransaction = undefined;
  });

  describe('resolve', () => {
    it('should return ILoyaltyTransaction returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        loyaltyTransactionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultLoyaltyTransaction = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultLoyaltyTransaction).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        loyaltyTransactionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultLoyaltyTransaction = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLoyaltyTransaction).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ILoyaltyTransaction>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        loyaltyTransactionResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultLoyaltyTransaction = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultLoyaltyTransaction).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
