import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tour-package.test-samples';

import { TourPackageFormService } from './tour-package-form.service';

describe('TourPackage Form Service', () => {
  let service: TourPackageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TourPackageFormService);
  });

  describe('Service methods', () => {
    describe('createTourPackageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTourPackageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            durationDays: expect.any(Object),
            available: expect.any(Object),
            agency: expect.any(Object),
          }),
        );
      });

      it('passing ITourPackage should create a new form with FormGroup', () => {
        const formGroup = service.createTourPackageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            durationDays: expect.any(Object),
            available: expect.any(Object),
            agency: expect.any(Object),
          }),
        );
      });
    });

    describe('getTourPackage', () => {
      it('should return NewTourPackage for default TourPackage initial value', () => {
        const formGroup = service.createTourPackageFormGroup(sampleWithNewData);

        const tourPackage = service.getTourPackage(formGroup) as any;

        expect(tourPackage).toMatchObject(sampleWithNewData);
      });

      it('should return NewTourPackage for empty TourPackage initial value', () => {
        const formGroup = service.createTourPackageFormGroup();

        const tourPackage = service.getTourPackage(formGroup) as any;

        expect(tourPackage).toMatchObject({});
      });

      it('should return ITourPackage', () => {
        const formGroup = service.createTourPackageFormGroup(sampleWithRequiredData);

        const tourPackage = service.getTourPackage(formGroup) as any;

        expect(tourPackage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITourPackage should not enable id FormControl', () => {
        const formGroup = service.createTourPackageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTourPackage should disable id FormControl', () => {
        const formGroup = service.createTourPackageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
