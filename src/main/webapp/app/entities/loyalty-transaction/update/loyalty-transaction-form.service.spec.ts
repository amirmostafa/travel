import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../loyalty-transaction.test-samples';

import { LoyaltyTransactionFormService } from './loyalty-transaction-form.service';

describe('LoyaltyTransaction Form Service', () => {
  let service: LoyaltyTransactionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoyaltyTransactionFormService);
  });

  describe('Service methods', () => {
    describe('createLoyaltyTransactionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLoyaltyTransactionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            points: expect.any(Object),
            transactionType: expect.any(Object),
            description: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing ILoyaltyTransaction should create a new form with FormGroup', () => {
        const formGroup = service.createLoyaltyTransactionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            points: expect.any(Object),
            transactionType: expect.any(Object),
            description: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getLoyaltyTransaction', () => {
      it('should return NewLoyaltyTransaction for default LoyaltyTransaction initial value', () => {
        const formGroup = service.createLoyaltyTransactionFormGroup(sampleWithNewData);

        const loyaltyTransaction = service.getLoyaltyTransaction(formGroup) as any;

        expect(loyaltyTransaction).toMatchObject(sampleWithNewData);
      });

      it('should return NewLoyaltyTransaction for empty LoyaltyTransaction initial value', () => {
        const formGroup = service.createLoyaltyTransactionFormGroup();

        const loyaltyTransaction = service.getLoyaltyTransaction(formGroup) as any;

        expect(loyaltyTransaction).toMatchObject({});
      });

      it('should return ILoyaltyTransaction', () => {
        const formGroup = service.createLoyaltyTransactionFormGroup(sampleWithRequiredData);

        const loyaltyTransaction = service.getLoyaltyTransaction(formGroup) as any;

        expect(loyaltyTransaction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILoyaltyTransaction should not enable id FormControl', () => {
        const formGroup = service.createLoyaltyTransactionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLoyaltyTransaction should disable id FormControl', () => {
        const formGroup = service.createLoyaltyTransactionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
