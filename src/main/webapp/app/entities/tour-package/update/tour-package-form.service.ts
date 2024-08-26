import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITourPackage, NewTourPackage } from '../tour-package.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITourPackage for edit and NewTourPackageFormGroupInput for create.
 */
type TourPackageFormGroupInput = ITourPackage | PartialWithRequiredKeyOf<NewTourPackage>;

type TourPackageFormDefaults = Pick<NewTourPackage, 'id' | 'available'>;

type TourPackageFormGroupContent = {
  id: FormControl<ITourPackage['id'] | NewTourPackage['id']>;
  name: FormControl<ITourPackage['name']>;
  description: FormControl<ITourPackage['description']>;
  price: FormControl<ITourPackage['price']>;
  durationDays: FormControl<ITourPackage['durationDays']>;
  available: FormControl<ITourPackage['available']>;
  agency: FormControl<ITourPackage['agency']>;
};

export type TourPackageFormGroup = FormGroup<TourPackageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TourPackageFormService {
  createTourPackageFormGroup(tourPackage: TourPackageFormGroupInput = { id: null }): TourPackageFormGroup {
    const tourPackageRawValue = {
      ...this.getFormDefaults(),
      ...tourPackage,
    };
    return new FormGroup<TourPackageFormGroupContent>({
      id: new FormControl(
        { value: tourPackageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(tourPackageRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(tourPackageRawValue.description, {
        validators: [Validators.required],
      }),
      price: new FormControl(tourPackageRawValue.price, {
        validators: [Validators.required, Validators.min(0)],
      }),
      durationDays: new FormControl(tourPackageRawValue.durationDays, {
        validators: [Validators.required, Validators.min(1)],
      }),
      available: new FormControl(tourPackageRawValue.available, {
        validators: [Validators.required],
      }),
      agency: new FormControl(tourPackageRawValue.agency),
    });
  }

  getTourPackage(form: TourPackageFormGroup): ITourPackage | NewTourPackage {
    return form.getRawValue() as ITourPackage | NewTourPackage;
  }

  resetForm(form: TourPackageFormGroup, tourPackage: TourPackageFormGroupInput): void {
    const tourPackageRawValue = { ...this.getFormDefaults(), ...tourPackage };
    form.reset(
      {
        ...tourPackageRawValue,
        id: { value: tourPackageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TourPackageFormDefaults {
    return {
      id: null,
      available: false,
    };
  }
}
