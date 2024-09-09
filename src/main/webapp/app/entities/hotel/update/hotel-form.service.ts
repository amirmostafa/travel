import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHotel, NewHotel } from '../hotel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHotel for edit and NewHotelFormGroupInput for create.
 */
type HotelFormGroupInput = IHotel | PartialWithRequiredKeyOf<NewHotel>;

type HotelFormDefaults = Pick<NewHotel, 'id'>;

type HotelFormGroupContent = {
  id: FormControl<IHotel['id'] | NewHotel['id']>;
  name: FormControl<IHotel['name']>;
  address: FormControl<IHotel['address']>;
  starRating: FormControl<IHotel['starRating']>;
  contactNumber: FormControl<IHotel['contactNumber']>;
  email: FormControl<IHotel['email']>;
  countryCode: FormControl<IHotel['countryCode']>;
  cityCode: FormControl<IHotel['cityCode']>;
  imageUrl: FormControl<IHotel['imageUrl']>;
  testimonial: FormControl<IHotel['testimonial']>;
};

export type HotelFormGroup = FormGroup<HotelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HotelFormService {
  createHotelFormGroup(hotel: HotelFormGroupInput = { id: null }): HotelFormGroup {
    const hotelRawValue = {
      ...this.getFormDefaults(),
      ...hotel,
    };
    return new FormGroup<HotelFormGroupContent>({
      id: new FormControl(
        { value: hotelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(hotelRawValue.name, {
        validators: [Validators.required],
      }),
      address: new FormControl(hotelRawValue.address, {
        validators: [Validators.required],
      }),
      starRating: new FormControl(hotelRawValue.starRating, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      contactNumber: new FormControl(hotelRawValue.contactNumber, {
        validators: [Validators.required],
      }),
      email: new FormControl(hotelRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      countryCode: new FormControl(hotelRawValue.countryCode, {
        validators: [Validators.required],
      }),
      cityCode: new FormControl(hotelRawValue.cityCode, {
        validators: [Validators.required],
      }),
      imageUrl: new FormControl(hotelRawValue.imageUrl),
      testimonial: new FormControl(hotelRawValue.testimonial),
    });
  }

  getHotel(form: HotelFormGroup): IHotel | NewHotel {
    return form.getRawValue() as IHotel | NewHotel;
  }

  resetForm(form: HotelFormGroup, hotel: HotelFormGroupInput): void {
    const hotelRawValue = { ...this.getFormDefaults(), ...hotel };
    form.reset(
      {
        ...hotelRawValue,
        id: { value: hotelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HotelFormDefaults {
    return {
      id: null,
    };
  }
}
