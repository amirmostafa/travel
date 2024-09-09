import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAgencyService } from '../agency-service.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../agency-service.test-samples';

import { AgencyServiceService } from './agency-service.service';

const requireRestSample: IAgencyService = {
  ...sampleWithRequiredData,
};

describe('AgencyService Service', () => {
  let service: AgencyServiceService;
  let httpMock: HttpTestingController;
  let expectedResult: IAgencyService | IAgencyService[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AgencyServiceService);
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

    it('should create a AgencyService', () => {
      const agencyService = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(agencyService).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AgencyService', () => {
      const agencyService = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(agencyService).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AgencyService', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AgencyService', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AgencyService', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAgencyServiceToCollectionIfMissing', () => {
      it('should add a AgencyService to an empty array', () => {
        const agencyService: IAgencyService = sampleWithRequiredData;
        expectedResult = service.addAgencyServiceToCollectionIfMissing([], agencyService);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agencyService);
      });

      it('should not add a AgencyService to an array that contains it', () => {
        const agencyService: IAgencyService = sampleWithRequiredData;
        const agencyServiceCollection: IAgencyService[] = [
          {
            ...agencyService,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAgencyServiceToCollectionIfMissing(agencyServiceCollection, agencyService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AgencyService to an array that doesn't contain it", () => {
        const agencyService: IAgencyService = sampleWithRequiredData;
        const agencyServiceCollection: IAgencyService[] = [sampleWithPartialData];
        expectedResult = service.addAgencyServiceToCollectionIfMissing(agencyServiceCollection, agencyService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agencyService);
      });

      it('should add only unique AgencyService to an array', () => {
        const agencyServiceArray: IAgencyService[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const agencyServiceCollection: IAgencyService[] = [sampleWithRequiredData];
        expectedResult = service.addAgencyServiceToCollectionIfMissing(agencyServiceCollection, ...agencyServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agencyService: IAgencyService = sampleWithRequiredData;
        const agencyService2: IAgencyService = sampleWithPartialData;
        expectedResult = service.addAgencyServiceToCollectionIfMissing([], agencyService, agencyService2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agencyService);
        expect(expectedResult).toContain(agencyService2);
      });

      it('should accept null and undefined values', () => {
        const agencyService: IAgencyService = sampleWithRequiredData;
        expectedResult = service.addAgencyServiceToCollectionIfMissing([], null, agencyService, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agencyService);
      });

      it('should return initial array if no AgencyService is added', () => {
        const agencyServiceCollection: IAgencyService[] = [sampleWithRequiredData];
        expectedResult = service.addAgencyServiceToCollectionIfMissing(agencyServiceCollection, undefined, null);
        expect(expectedResult).toEqual(agencyServiceCollection);
      });
    });

    describe('compareAgencyService', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAgencyService(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAgencyService(entity1, entity2);
        const compareResult2 = service.compareAgencyService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAgencyService(entity1, entity2);
        const compareResult2 = service.compareAgencyService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAgencyService(entity1, entity2);
        const compareResult2 = service.compareAgencyService(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
