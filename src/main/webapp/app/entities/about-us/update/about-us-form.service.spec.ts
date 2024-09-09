import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../about-us.test-samples';

import { AboutUsFormService } from './about-us-form.service';

describe('AboutUs Form Service', () => {
  let service: AboutUsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AboutUsFormService);
  });

  describe('Service methods', () => {
    describe('createAboutUsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAboutUsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            contactDetails: expect.any(Object),
            additionalInfo: expect.any(Object),
          }),
        );
      });

      it('passing IAboutUs should create a new form with FormGroup', () => {
        const formGroup = service.createAboutUsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            contactDetails: expect.any(Object),
            additionalInfo: expect.any(Object),
          }),
        );
      });
    });

    describe('getAboutUs', () => {
      it('should return NewAboutUs for default AboutUs initial value', () => {
        const formGroup = service.createAboutUsFormGroup(sampleWithNewData);

        const aboutUs = service.getAboutUs(formGroup) as any;

        expect(aboutUs).toMatchObject(sampleWithNewData);
      });

      it('should return NewAboutUs for empty AboutUs initial value', () => {
        const formGroup = service.createAboutUsFormGroup();

        const aboutUs = service.getAboutUs(formGroup) as any;

        expect(aboutUs).toMatchObject({});
      });

      it('should return IAboutUs', () => {
        const formGroup = service.createAboutUsFormGroup(sampleWithRequiredData);

        const aboutUs = service.getAboutUs(formGroup) as any;

        expect(aboutUs).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAboutUs should not enable id FormControl', () => {
        const formGroup = service.createAboutUsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAboutUs should disable id FormControl', () => {
        const formGroup = service.createAboutUsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
