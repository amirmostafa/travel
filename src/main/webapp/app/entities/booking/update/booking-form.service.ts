import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBooking, NewBooking } from '../booking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooking for edit and NewBookingFormGroupInput for create.
 */
type BookingFormGroupInput = IBooking | PartialWithRequiredKeyOf<NewBooking>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBooking | NewBooking> = Omit<T, 'bookingDate' | 'startDate' | 'endDate'> & {
  bookingDate?: string | null;
  startDate?: string | null;
  endDate?: string | null;
};

type BookingFormRawValue = FormValueOf<IBooking>;

type NewBookingFormRawValue = FormValueOf<NewBooking>;

type BookingFormDefaults = Pick<NewBooking, 'id' | 'bookingDate' | 'startDate' | 'endDate'>;

type BookingFormGroupContent = {
  id: FormControl<BookingFormRawValue['id'] | NewBooking['id']>;
  bookingDate: FormControl<BookingFormRawValue['bookingDate']>;
  startDate: FormControl<BookingFormRawValue['startDate']>;
  endDate: FormControl<BookingFormRawValue['endDate']>;
  status: FormControl<BookingFormRawValue['status']>;
  totalPrice: FormControl<BookingFormRawValue['totalPrice']>;
  room: FormControl<BookingFormRawValue['room']>;
  tourPackage: FormControl<BookingFormRawValue['tourPackage']>;
  customer: FormControl<BookingFormRawValue['customer']>;
};

export type BookingFormGroup = FormGroup<BookingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookingFormService {
  createBookingFormGroup(booking: BookingFormGroupInput = { id: null }): BookingFormGroup {
    const bookingRawValue = this.convertBookingToBookingRawValue({
      ...this.getFormDefaults(),
      ...booking,
    });
    return new FormGroup<BookingFormGroupContent>({
      id: new FormControl(
        { value: bookingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      bookingDate: new FormControl(bookingRawValue.bookingDate, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(bookingRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(bookingRawValue.endDate, {
        validators: [Validators.required],
      }),
      status: new FormControl(bookingRawValue.status, {
        validators: [Validators.required],
      }),
      totalPrice: new FormControl(bookingRawValue.totalPrice, {
        validators: [Validators.required, Validators.min(0)],
      }),
      room: new FormControl(bookingRawValue.room),
      tourPackage: new FormControl(bookingRawValue.tourPackage),
      customer: new FormControl(bookingRawValue.customer),
    });
  }

  getBooking(form: BookingFormGroup): IBooking | NewBooking {
    return this.convertBookingRawValueToBooking(form.getRawValue() as BookingFormRawValue | NewBookingFormRawValue);
  }

  resetForm(form: BookingFormGroup, booking: BookingFormGroupInput): void {
    const bookingRawValue = this.convertBookingToBookingRawValue({ ...this.getFormDefaults(), ...booking });
    form.reset(
      {
        ...bookingRawValue,
        id: { value: bookingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      bookingDate: currentTime,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertBookingRawValueToBooking(rawBooking: BookingFormRawValue | NewBookingFormRawValue): IBooking | NewBooking {
    return {
      ...rawBooking,
      bookingDate: dayjs(rawBooking.bookingDate, DATE_TIME_FORMAT),
      startDate: dayjs(rawBooking.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawBooking.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertBookingToBookingRawValue(
    booking: IBooking | (Partial<NewBooking> & BookingFormDefaults),
  ): BookingFormRawValue | PartialWithRequiredKeyOf<NewBookingFormRawValue> {
    return {
      ...booking,
      bookingDate: booking.bookingDate ? booking.bookingDate.format(DATE_TIME_FORMAT) : undefined,
      startDate: booking.startDate ? booking.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: booking.endDate ? booking.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
