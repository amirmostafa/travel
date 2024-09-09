import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITestimonial } from '../testimonial.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../testimonial.test-samples';

import { TestimonialService, RestTestimonial } from './testimonial.service';

const requireRestSample: RestTestimonial = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('Testimonial Service', () => {
  let service: TestimonialService;
  let httpMock: HttpTestingController;
  let expectedResult: ITestimonial | ITestimonial[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TestimonialService);
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

    it('should create a Testimonial', () => {
      const testimonial = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(testimonial).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Testimonial', () => {
      const testimonial = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(testimonial).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Testimonial', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Testimonial', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Testimonial', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTestimonialToCollectionIfMissing', () => {
      it('should add a Testimonial to an empty array', () => {
        const testimonial: ITestimonial = sampleWithRequiredData;
        expectedResult = service.addTestimonialToCollectionIfMissing([], testimonial);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testimonial);
      });

      it('should not add a Testimonial to an array that contains it', () => {
        const testimonial: ITestimonial = sampleWithRequiredData;
        const testimonialCollection: ITestimonial[] = [
          {
            ...testimonial,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTestimonialToCollectionIfMissing(testimonialCollection, testimonial);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Testimonial to an array that doesn't contain it", () => {
        const testimonial: ITestimonial = sampleWithRequiredData;
        const testimonialCollection: ITestimonial[] = [sampleWithPartialData];
        expectedResult = service.addTestimonialToCollectionIfMissing(testimonialCollection, testimonial);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testimonial);
      });

      it('should add only unique Testimonial to an array', () => {
        const testimonialArray: ITestimonial[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const testimonialCollection: ITestimonial[] = [sampleWithRequiredData];
        expectedResult = service.addTestimonialToCollectionIfMissing(testimonialCollection, ...testimonialArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const testimonial: ITestimonial = sampleWithRequiredData;
        const testimonial2: ITestimonial = sampleWithPartialData;
        expectedResult = service.addTestimonialToCollectionIfMissing([], testimonial, testimonial2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testimonial);
        expect(expectedResult).toContain(testimonial2);
      });

      it('should accept null and undefined values', () => {
        const testimonial: ITestimonial = sampleWithRequiredData;
        expectedResult = service.addTestimonialToCollectionIfMissing([], null, testimonial, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testimonial);
      });

      it('should return initial array if no Testimonial is added', () => {
        const testimonialCollection: ITestimonial[] = [sampleWithRequiredData];
        expectedResult = service.addTestimonialToCollectionIfMissing(testimonialCollection, undefined, null);
        expect(expectedResult).toEqual(testimonialCollection);
      });
    });

    describe('compareTestimonial', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTestimonial(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTestimonial(entity1, entity2);
        const compareResult2 = service.compareTestimonial(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTestimonial(entity1, entity2);
        const compareResult2 = service.compareTestimonial(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTestimonial(entity1, entity2);
        const compareResult2 = service.compareTestimonial(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
