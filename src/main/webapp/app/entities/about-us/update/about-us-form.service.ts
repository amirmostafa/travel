import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAboutUs, NewAboutUs } from '../about-us.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAboutUs for edit and NewAboutUsFormGroupInput for create.
 */
type AboutUsFormGroupInput = IAboutUs | PartialWithRequiredKeyOf<NewAboutUs>;

type AboutUsFormDefaults = Pick<NewAboutUs, 'id'>;

type AboutUsFormGroupContent = {
  id: FormControl<IAboutUs['id'] | NewAboutUs['id']>;
  description: FormControl<IAboutUs['description']>;
  contactDetails: FormControl<IAboutUs['contactDetails']>;
  additionalInfo: FormControl<IAboutUs['additionalInfo']>;
};

export type AboutUsFormGroup = FormGroup<AboutUsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AboutUsFormService {
  createAboutUsFormGroup(aboutUs: AboutUsFormGroupInput = { id: null }): AboutUsFormGroup {
    const aboutUsRawValue = {
      ...this.getFormDefaults(),
      ...aboutUs,
    };
    return new FormGroup<AboutUsFormGroupContent>({
      id: new FormControl(
        { value: aboutUsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(aboutUsRawValue.description, {
        validators: [Validators.required, Validators.maxLength(65535)],
      }),
      contactDetails: new FormControl(aboutUsRawValue.contactDetails),
      additionalInfo: new FormControl(aboutUsRawValue.additionalInfo, {
        validators: [Validators.maxLength(65535)],
      }),
    });
  }

  getAboutUs(form: AboutUsFormGroup): IAboutUs | NewAboutUs {
    return form.getRawValue() as IAboutUs | NewAboutUs;
  }

  resetForm(form: AboutUsFormGroup, aboutUs: AboutUsFormGroupInput): void {
    const aboutUsRawValue = { ...this.getFormDefaults(), ...aboutUs };
    form.reset(
      {
        ...aboutUsRawValue,
        id: { value: aboutUsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AboutUsFormDefaults {
    return {
      id: null,
    };
  }
}
