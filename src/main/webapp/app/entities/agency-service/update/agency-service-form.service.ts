import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAgencyService, NewAgencyService } from '../agency-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgencyService for edit and NewAgencyServiceFormGroupInput for create.
 */
type AgencyServiceFormGroupInput = IAgencyService | PartialWithRequiredKeyOf<NewAgencyService>;

type AgencyServiceFormDefaults = Pick<NewAgencyService, 'id'>;

type AgencyServiceFormGroupContent = {
  id: FormControl<IAgencyService['id'] | NewAgencyService['id']>;
  title: FormControl<IAgencyService['title']>;
  icon: FormControl<IAgencyService['icon']>;
  content: FormControl<IAgencyService['content']>;
};

export type AgencyServiceFormGroup = FormGroup<AgencyServiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgencyServiceFormService {
  createAgencyServiceFormGroup(agencyService: AgencyServiceFormGroupInput = { id: null }): AgencyServiceFormGroup {
    const agencyServiceRawValue = {
      ...this.getFormDefaults(),
      ...agencyService,
    };
    return new FormGroup<AgencyServiceFormGroupContent>({
      id: new FormControl(
        { value: agencyServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(agencyServiceRawValue.title, {
        validators: [Validators.required],
      }),
      icon: new FormControl(agencyServiceRawValue.icon),
      content: new FormControl(agencyServiceRawValue.content, {
        validators: [Validators.required, Validators.maxLength(65535)],
      }),
    });
  }

  getAgencyService(form: AgencyServiceFormGroup): IAgencyService | NewAgencyService {
    return form.getRawValue() as IAgencyService | NewAgencyService;
  }

  resetForm(form: AgencyServiceFormGroup, agencyService: AgencyServiceFormGroupInput): void {
    const agencyServiceRawValue = { ...this.getFormDefaults(), ...agencyService };
    form.reset(
      {
        ...agencyServiceRawValue,
        id: { value: agencyServiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AgencyServiceFormDefaults {
    return {
      id: null,
    };
  }
}
