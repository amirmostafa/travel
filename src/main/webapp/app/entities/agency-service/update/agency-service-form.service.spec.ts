import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../agency-service.test-samples';

import { AgencyServiceFormService } from './agency-service-form.service';

describe('AgencyService Form Service', () => {
  let service: AgencyServiceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgencyServiceFormService);
  });

  describe('Service methods', () => {
    describe('createAgencyServiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAgencyServiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            icon: expect.any(Object),
            content: expect.any(Object),
          }),
        );
      });

      it('passing IAgencyService should create a new form with FormGroup', () => {
        const formGroup = service.createAgencyServiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            icon: expect.any(Object),
            content: expect.any(Object),
          }),
        );
      });
    });

    describe('getAgencyService', () => {
      it('should return NewAgencyService for default AgencyService initial value', () => {
        const formGroup = service.createAgencyServiceFormGroup(sampleWithNewData);

        const agencyService = service.getAgencyService(formGroup) as any;

        expect(agencyService).toMatchObject(sampleWithNewData);
      });

      it('should return NewAgencyService for empty AgencyService initial value', () => {
        const formGroup = service.createAgencyServiceFormGroup();

        const agencyService = service.getAgencyService(formGroup) as any;

        expect(agencyService).toMatchObject({});
      });

      it('should return IAgencyService', () => {
        const formGroup = service.createAgencyServiceFormGroup(sampleWithRequiredData);

        const agencyService = service.getAgencyService(formGroup) as any;

        expect(agencyService).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAgencyService should not enable id FormControl', () => {
        const formGroup = service.createAgencyServiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAgencyService should disable id FormControl', () => {
        const formGroup = service.createAgencyServiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
