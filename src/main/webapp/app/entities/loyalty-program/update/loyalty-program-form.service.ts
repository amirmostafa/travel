import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILoyaltyProgram, NewLoyaltyProgram } from '../loyalty-program.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILoyaltyProgram for edit and NewLoyaltyProgramFormGroupInput for create.
 */
type LoyaltyProgramFormGroupInput = ILoyaltyProgram | PartialWithRequiredKeyOf<NewLoyaltyProgram>;

type LoyaltyProgramFormDefaults = Pick<NewLoyaltyProgram, 'id'>;

type LoyaltyProgramFormGroupContent = {
  id: FormControl<ILoyaltyProgram['id'] | NewLoyaltyProgram['id']>;
  name: FormControl<ILoyaltyProgram['name']>;
  description: FormControl<ILoyaltyProgram['description']>;
  pointsPerDollar: FormControl<ILoyaltyProgram['pointsPerDollar']>;
  rewardThreshold: FormControl<ILoyaltyProgram['rewardThreshold']>;
};

export type LoyaltyProgramFormGroup = FormGroup<LoyaltyProgramFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LoyaltyProgramFormService {
  createLoyaltyProgramFormGroup(loyaltyProgram: LoyaltyProgramFormGroupInput = { id: null }): LoyaltyProgramFormGroup {
    const loyaltyProgramRawValue = {
      ...this.getFormDefaults(),
      ...loyaltyProgram,
    };
    return new FormGroup<LoyaltyProgramFormGroupContent>({
      id: new FormControl(
        { value: loyaltyProgramRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(loyaltyProgramRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(loyaltyProgramRawValue.description),
      pointsPerDollar: new FormControl(loyaltyProgramRawValue.pointsPerDollar, {
        validators: [Validators.required, Validators.min(0)],
      }),
      rewardThreshold: new FormControl(loyaltyProgramRawValue.rewardThreshold, {
        validators: [Validators.required, Validators.min(0)],
      }),
    });
  }

  getLoyaltyProgram(form: LoyaltyProgramFormGroup): ILoyaltyProgram | NewLoyaltyProgram {
    return form.getRawValue() as ILoyaltyProgram | NewLoyaltyProgram;
  }

  resetForm(form: LoyaltyProgramFormGroup, loyaltyProgram: LoyaltyProgramFormGroupInput): void {
    const loyaltyProgramRawValue = { ...this.getFormDefaults(), ...loyaltyProgram };
    form.reset(
      {
        ...loyaltyProgramRawValue,
        id: { value: loyaltyProgramRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LoyaltyProgramFormDefaults {
    return {
      id: null,
    };
  }
}
