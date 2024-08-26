import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRoomPrice, NewRoomPrice } from '../room-price.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoomPrice for edit and NewRoomPriceFormGroupInput for create.
 */
type RoomPriceFormGroupInput = IRoomPrice | PartialWithRequiredKeyOf<NewRoomPrice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRoomPrice | NewRoomPrice> = Omit<T, 'fromDate' | 'toDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
};

type RoomPriceFormRawValue = FormValueOf<IRoomPrice>;

type NewRoomPriceFormRawValue = FormValueOf<NewRoomPrice>;

type RoomPriceFormDefaults = Pick<NewRoomPrice, 'id' | 'fromDate' | 'toDate'>;

type RoomPriceFormGroupContent = {
  id: FormControl<RoomPriceFormRawValue['id'] | NewRoomPrice['id']>;
  price: FormControl<RoomPriceFormRawValue['price']>;
  fromDate: FormControl<RoomPriceFormRawValue['fromDate']>;
  toDate: FormControl<RoomPriceFormRawValue['toDate']>;
  room: FormControl<RoomPriceFormRawValue['room']>;
};

export type RoomPriceFormGroup = FormGroup<RoomPriceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoomPriceFormService {
  createRoomPriceFormGroup(roomPrice: RoomPriceFormGroupInput = { id: null }): RoomPriceFormGroup {
    const roomPriceRawValue = this.convertRoomPriceToRoomPriceRawValue({
      ...this.getFormDefaults(),
      ...roomPrice,
    });
    return new FormGroup<RoomPriceFormGroupContent>({
      id: new FormControl(
        { value: roomPriceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      price: new FormControl(roomPriceRawValue.price, {
        validators: [Validators.required, Validators.min(0)],
      }),
      fromDate: new FormControl(roomPriceRawValue.fromDate, {
        validators: [Validators.required],
      }),
      toDate: new FormControl(roomPriceRawValue.toDate, {
        validators: [Validators.required],
      }),
      room: new FormControl(roomPriceRawValue.room),
    });
  }

  getRoomPrice(form: RoomPriceFormGroup): IRoomPrice | NewRoomPrice {
    return this.convertRoomPriceRawValueToRoomPrice(form.getRawValue() as RoomPriceFormRawValue | NewRoomPriceFormRawValue);
  }

  resetForm(form: RoomPriceFormGroup, roomPrice: RoomPriceFormGroupInput): void {
    const roomPriceRawValue = this.convertRoomPriceToRoomPriceRawValue({ ...this.getFormDefaults(), ...roomPrice });
    form.reset(
      {
        ...roomPriceRawValue,
        id: { value: roomPriceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RoomPriceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      toDate: currentTime,
    };
  }

  private convertRoomPriceRawValueToRoomPrice(rawRoomPrice: RoomPriceFormRawValue | NewRoomPriceFormRawValue): IRoomPrice | NewRoomPrice {
    return {
      ...rawRoomPrice,
      fromDate: dayjs(rawRoomPrice.fromDate, DATE_TIME_FORMAT),
      toDate: dayjs(rawRoomPrice.toDate, DATE_TIME_FORMAT),
    };
  }

  private convertRoomPriceToRoomPriceRawValue(
    roomPrice: IRoomPrice | (Partial<NewRoomPrice> & RoomPriceFormDefaults),
  ): RoomPriceFormRawValue | PartialWithRequiredKeyOf<NewRoomPriceFormRawValue> {
    return {
      ...roomPrice,
      fromDate: roomPrice.fromDate ? roomPrice.fromDate.format(DATE_TIME_FORMAT) : undefined,
      toDate: roomPrice.toDate ? roomPrice.toDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
