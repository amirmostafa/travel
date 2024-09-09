import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../testimonial.test-samples';

import { TestimonialFormService } from './testimonial-form.service';

describe('Testimonial Form Service', () => {
  let service: TestimonialFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestimonialFormService);
  });

  describe('Service methods', () => {
    describe('createTestimonialFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTestimonialFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            authorName: expect.any(Object),
            content: expect.any(Object),
            rating: expect.any(Object),
            date: expect.any(Object),
          }),
        );
      });

      it('passing ITestimonial should create a new form with FormGroup', () => {
        const formGroup = service.createTestimonialFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            authorName: expect.any(Object),
            content: expect.any(Object),
            rating: expect.any(Object),
            date: expect.any(Object),
          }),
        );
      });
    });

    describe('getTestimonial', () => {
      it('should return NewTestimonial for default Testimonial initial value', () => {
        const formGroup = service.createTestimonialFormGroup(sampleWithNewData);

        const testimonial = service.getTestimonial(formGroup) as any;

        expect(testimonial).toMatchObject(sampleWithNewData);
      });

      it('should return NewTestimonial for empty Testimonial initial value', () => {
        const formGroup = service.createTestimonialFormGroup();

        const testimonial = service.getTestimonial(formGroup) as any;

        expect(testimonial).toMatchObject({});
      });

      it('should return ITestimonial', () => {
        const formGroup = service.createTestimonialFormGroup(sampleWithRequiredData);

        const testimonial = service.getTestimonial(formGroup) as any;

        expect(testimonial).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITestimonial should not enable id FormControl', () => {
        const formGroup = service.createTestimonialFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTestimonial should disable id FormControl', () => {
        const formGroup = service.createTestimonialFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
