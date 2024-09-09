import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILoyaltyTransaction, NewLoyaltyTransaction } from '../loyalty-transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILoyaltyTransaction for edit and NewLoyaltyTransactionFormGroupInput for create.
 */
type LoyaltyTransactionFormGroupInput = ILoyaltyTransaction | PartialWithRequiredKeyOf<NewLoyaltyTransaction>;

type LoyaltyTransactionFormDefaults = Pick<NewLoyaltyTransaction, 'id'>;

type LoyaltyTransactionFormGroupContent = {
  id: FormControl<ILoyaltyTransaction['id'] | NewLoyaltyTransaction['id']>;
  date: FormControl<ILoyaltyTransaction['date']>;
  points: FormControl<ILoyaltyTransaction['points']>;
  transactionType: FormControl<ILoyaltyTransaction['transactionType']>;
  description: FormControl<ILoyaltyTransaction['description']>;
  customer: FormControl<ILoyaltyTransaction['customer']>;
};

export type LoyaltyTransactionFormGroup = FormGroup<LoyaltyTransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LoyaltyTransactionFormService {
  createLoyaltyTransactionFormGroup(loyaltyTransaction: LoyaltyTransactionFormGroupInput = { id: null }): LoyaltyTransactionFormGroup {
    const loyaltyTransactionRawValue = {
      ...this.getFormDefaults(),
      ...loyaltyTransaction,
    };
    return new FormGroup<LoyaltyTransactionFormGroupContent>({
      id: new FormControl(
        { value: loyaltyTransactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(loyaltyTransactionRawValue.date, {
        validators: [Validators.required],
      }),
      points: new FormControl(loyaltyTransactionRawValue.points, {
        validators: [Validators.required],
      }),
      transactionType: new FormControl(loyaltyTransactionRawValue.transactionType, {
        validators: [Validators.required],
      }),
      description: new FormControl(loyaltyTransactionRawValue.description),
      customer: new FormControl(loyaltyTransactionRawValue.customer),
    });
  }

  getLoyaltyTransaction(form: LoyaltyTransactionFormGroup): ILoyaltyTransaction | NewLoyaltyTransaction {
    return form.getRawValue() as ILoyaltyTransaction | NewLoyaltyTransaction;
  }

  resetForm(form: LoyaltyTransactionFormGroup, loyaltyTransaction: LoyaltyTransactionFormGroupInput): void {
    const loyaltyTransactionRawValue = { ...this.getFormDefaults(), ...loyaltyTransaction };
    form.reset(
      {
        ...loyaltyTransactionRawValue,
        id: { value: loyaltyTransactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LoyaltyTransactionFormDefaults {
    return {
      id: null,
    };
  }
}
