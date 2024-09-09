import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITestimonial, NewTestimonial } from '../testimonial.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestimonial for edit and NewTestimonialFormGroupInput for create.
 */
type TestimonialFormGroupInput = ITestimonial | PartialWithRequiredKeyOf<NewTestimonial>;

type TestimonialFormDefaults = Pick<NewTestimonial, 'id'>;

type TestimonialFormGroupContent = {
  id: FormControl<ITestimonial['id'] | NewTestimonial['id']>;
  authorName: FormControl<ITestimonial['authorName']>;
  content: FormControl<ITestimonial['content']>;
  rating: FormControl<ITestimonial['rating']>;
  date: FormControl<ITestimonial['date']>;
};

export type TestimonialFormGroup = FormGroup<TestimonialFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestimonialFormService {
  createTestimonialFormGroup(testimonial: TestimonialFormGroupInput = { id: null }): TestimonialFormGroup {
    const testimonialRawValue = {
      ...this.getFormDefaults(),
      ...testimonial,
    };
    return new FormGroup<TestimonialFormGroupContent>({
      id: new FormControl(
        { value: testimonialRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      authorName: new FormControl(testimonialRawValue.authorName, {
        validators: [Validators.required],
      }),
      content: new FormControl(testimonialRawValue.content, {
        validators: [Validators.required, Validators.maxLength(65535)],
      }),
      rating: new FormControl(testimonialRawValue.rating, {
        validators: [Validators.min(1), Validators.max(5)],
      }),
      date: new FormControl(testimonialRawValue.date, {
        validators: [Validators.required],
      }),
    });
  }

  getTestimonial(form: TestimonialFormGroup): ITestimonial | NewTestimonial {
    return form.getRawValue() as ITestimonial | NewTestimonial;
  }

  resetForm(form: TestimonialFormGroup, testimonial: TestimonialFormGroupInput): void {
    const testimonialRawValue = { ...this.getFormDefaults(), ...testimonial };
    form.reset(
      {
        ...testimonialRawValue,
        id: { value: testimonialRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TestimonialFormDefaults {
    return {
      id: null,
    };
  }
}
