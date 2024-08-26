import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITourPackage } from '../tour-package.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tour-package.test-samples';

import { TourPackageService } from './tour-package.service';

const requireRestSample: ITourPackage = {
  ...sampleWithRequiredData,
};

describe('TourPackage Service', () => {
  let service: TourPackageService;
  let httpMock: HttpTestingController;
  let expectedResult: ITourPackage | ITourPackage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TourPackageService);
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

    it('should create a TourPackage', () => {
      const tourPackage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tourPackage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TourPackage', () => {
      const tourPackage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tourPackage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TourPackage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TourPackage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TourPackage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTourPackageToCollectionIfMissing', () => {
      it('should add a TourPackage to an empty array', () => {
        const tourPackage: ITourPackage = sampleWithRequiredData;
        expectedResult = service.addTourPackageToCollectionIfMissing([], tourPackage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tourPackage);
      });

      it('should not add a TourPackage to an array that contains it', () => {
        const tourPackage: ITourPackage = sampleWithRequiredData;
        const tourPackageCollection: ITourPackage[] = [
          {
            ...tourPackage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTourPackageToCollectionIfMissing(tourPackageCollection, tourPackage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TourPackage to an array that doesn't contain it", () => {
        const tourPackage: ITourPackage = sampleWithRequiredData;
        const tourPackageCollection: ITourPackage[] = [sampleWithPartialData];
        expectedResult = service.addTourPackageToCollectionIfMissing(tourPackageCollection, tourPackage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tourPackage);
      });

      it('should add only unique TourPackage to an array', () => {
        const tourPackageArray: ITourPackage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tourPackageCollection: ITourPackage[] = [sampleWithRequiredData];
        expectedResult = service.addTourPackageToCollectionIfMissing(tourPackageCollection, ...tourPackageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tourPackage: ITourPackage = sampleWithRequiredData;
        const tourPackage2: ITourPackage = sampleWithPartialData;
        expectedResult = service.addTourPackageToCollectionIfMissing([], tourPackage, tourPackage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tourPackage);
        expect(expectedResult).toContain(tourPackage2);
      });

      it('should accept null and undefined values', () => {
        const tourPackage: ITourPackage = sampleWithRequiredData;
        expectedResult = service.addTourPackageToCollectionIfMissing([], null, tourPackage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tourPackage);
      });

      it('should return initial array if no TourPackage is added', () => {
        const tourPackageCollection: ITourPackage[] = [sampleWithRequiredData];
        expectedResult = service.addTourPackageToCollectionIfMissing(tourPackageCollection, undefined, null);
        expect(expectedResult).toEqual(tourPackageCollection);
      });
    });

    describe('compareTourPackage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTourPackage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTourPackage(entity1, entity2);
        const compareResult2 = service.compareTourPackage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTourPackage(entity1, entity2);
        const compareResult2 = service.compareTourPackage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTourPackage(entity1, entity2);
        const compareResult2 = service.compareTourPackage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
