import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILoyaltyTransaction } from '../loyalty-transaction.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../loyalty-transaction.test-samples';

import { LoyaltyTransactionService, RestLoyaltyTransaction } from './loyalty-transaction.service';

const requireRestSample: RestLoyaltyTransaction = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('LoyaltyTransaction Service', () => {
  let service: LoyaltyTransactionService;
  let httpMock: HttpTestingController;
  let expectedResult: ILoyaltyTransaction | ILoyaltyTransaction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LoyaltyTransactionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a LoyaltyTransaction', () => {
      const loyaltyTransaction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(loyaltyTransaction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LoyaltyTransaction', () => {
      const loyaltyTransaction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(loyaltyTransaction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LoyaltyTransaction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LoyaltyTransaction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LoyaltyTransaction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLoyaltyTransactionToCollectionIfMissing', () => {
      it('should add a LoyaltyTransaction to an empty array', () => {
        const loyaltyTransaction: ILoyaltyTransaction = sampleWithRequiredData;
        expectedResult = service.addLoyaltyTransactionToCollectionIfMissing([], loyaltyTransaction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loyaltyTransaction);
      });

      it('should not add a LoyaltyTransaction to an array that contains it', () => {
        const loyaltyTransaction: ILoyaltyTransaction = sampleWithRequiredData;
        const loyaltyTransactionCollection: ILoyaltyTransaction[] = [
          {
            ...loyaltyTransaction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLoyaltyTransactionToCollectionIfMissing(loyaltyTransactionCollection, loyaltyTransaction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LoyaltyTransaction to an array that doesn't contain it", () => {
        const loyaltyTransaction: ILoyaltyTransaction = sampleWithRequiredData;
        const loyaltyTransactionCollection: ILoyaltyTransaction[] = [sampleWithPartialData];
        expectedResult = service.addLoyaltyTransactionToCollectionIfMissing(loyaltyTransactionCollection, loyaltyTransaction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loyaltyTransaction);
      });

      it('should add only unique LoyaltyTransaction to an array', () => {
        const loyaltyTransactionArray: ILoyaltyTransaction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const loyaltyTransactionCollection: ILoyaltyTransaction[] = [sampleWithRequiredData];
        expectedResult = service.addLoyaltyTransactionToCollectionIfMissing(loyaltyTransactionCollection, ...loyaltyTransactionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const loyaltyTransaction: ILoyaltyTransaction = sampleWithRequiredData;
        const loyaltyTransaction2: ILoyaltyTransaction = sampleWithPartialData;
        expectedResult = service.addLoyaltyTransactionToCollectionIfMissing([], loyaltyTransaction, loyaltyTransaction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(loyaltyTransaction);
        expect(expectedResult).toContain(loyaltyTransaction2);
      });

      it('should accept null and undefined values', () => {
        const loyaltyTransaction: ILoyaltyTransaction = sampleWithRequiredData;
        expectedResult = service.addLoyaltyTransactionToCollectionIfMissing([], null, loyaltyTransaction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(loyaltyTransaction);
      });

      it('should return initial array if no LoyaltyTransaction is added', () => {
        const loyaltyTransactionCollection: ILoyaltyTransaction[] = [sampleWithRequiredData];
        expectedResult = service.addLoyaltyTransactionToCollectionIfMissing(loyaltyTransactionCollection, undefined, null);
        expect(expectedResult).toEqual(loyaltyTransactionCollection);
      });
    });

    describe('compareLoyaltyTransaction', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLoyaltyTransaction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLoyaltyTransaction(entity1, entity2);
        const compareResult2 = service.compareLoyaltyTransaction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLoyaltyTransaction(entity1, entity2);
        const compareResult2 = service.compareLoyaltyTransaction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLoyaltyTransaction(entity1, entity2);
        const compareResult2 = service.compareLoyaltyTransaction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
