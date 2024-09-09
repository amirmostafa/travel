import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAboutUs } from '../about-us.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../about-us.test-samples';

import { AboutUsService } from './about-us.service';

const requireRestSample: IAboutUs = {
  ...sampleWithRequiredData,
};

describe('AboutUs Service', () => {
  let service: AboutUsService;
  let httpMock: HttpTestingController;
  let expectedResult: IAboutUs | IAboutUs[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AboutUsService);
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

    it('should create a AboutUs', () => {
      const aboutUs = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aboutUs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AboutUs', () => {
      const aboutUs = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aboutUs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AboutUs', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AboutUs', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AboutUs', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAboutUsToCollectionIfMissing', () => {
      it('should add a AboutUs to an empty array', () => {
        const aboutUs: IAboutUs = sampleWithRequiredData;
        expectedResult = service.addAboutUsToCollectionIfMissing([], aboutUs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutUs);
      });

      it('should not add a AboutUs to an array that contains it', () => {
        const aboutUs: IAboutUs = sampleWithRequiredData;
        const aboutUsCollection: IAboutUs[] = [
          {
            ...aboutUs,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAboutUsToCollectionIfMissing(aboutUsCollection, aboutUs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AboutUs to an array that doesn't contain it", () => {
        const aboutUs: IAboutUs = sampleWithRequiredData;
        const aboutUsCollection: IAboutUs[] = [sampleWithPartialData];
        expectedResult = service.addAboutUsToCollectionIfMissing(aboutUsCollection, aboutUs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutUs);
      });

      it('should add only unique AboutUs to an array', () => {
        const aboutUsArray: IAboutUs[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aboutUsCollection: IAboutUs[] = [sampleWithRequiredData];
        expectedResult = service.addAboutUsToCollectionIfMissing(aboutUsCollection, ...aboutUsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aboutUs: IAboutUs = sampleWithRequiredData;
        const aboutUs2: IAboutUs = sampleWithPartialData;
        expectedResult = service.addAboutUsToCollectionIfMissing([], aboutUs, aboutUs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutUs);
        expect(expectedResult).toContain(aboutUs2);
      });

      it('should accept null and undefined values', () => {
        const aboutUs: IAboutUs = sampleWithRequiredData;
        expectedResult = service.addAboutUsToCollectionIfMissing([], null, aboutUs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutUs);
      });

      it('should return initial array if no AboutUs is added', () => {
        const aboutUsCollection: IAboutUs[] = [sampleWithRequiredData];
        expectedResult = service.addAboutUsToCollectionIfMissing(aboutUsCollection, undefined, null);
        expect(expectedResult).toEqual(aboutUsCollection);
      });
    });

    describe('compareAboutUs', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAboutUs(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAboutUs(entity1, entity2);
        const compareResult2 = service.compareAboutUs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAboutUs(entity1, entity2);
        const compareResult2 = service.compareAboutUs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAboutUs(entity1, entity2);
        const compareResult2 = service.compareAboutUs(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
