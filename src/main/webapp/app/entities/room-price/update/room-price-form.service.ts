import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type RoomPriceFormDefaults = Pick<NewRoomPrice, 'id'>;

type RoomPriceFormGroupContent = {
  id: FormControl<IRoomPrice['id'] | NewRoomPrice['id']>;
  price: FormControl<IRoomPrice['price']>;
  fromDate: FormControl<IRoomPrice['fromDate']>;
  toDate: FormControl<IRoomPrice['toDate']>;
  room: FormControl<IRoomPrice['room']>;
};

export type RoomPriceFormGroup = FormGroup<RoomPriceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoomPriceFormService {
  createRoomPriceFormGroup(roomPrice: RoomPriceFormGroupInput = { id: null }): RoomPriceFormGroup {
    const roomPriceRawValue = {
      ...this.getFormDefaults(),
      ...roomPrice,
    };
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
    return form.getRawValue() as IRoomPrice | NewRoomPrice;
  }

  resetForm(form: RoomPriceFormGroup, roomPrice: RoomPriceFormGroupInput): void {
    const roomPriceRawValue = { ...this.getFormDefaults(), ...roomPrice };
    form.reset(
      {
        ...roomPriceRawValue,
        id: { value: roomPriceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RoomPriceFormDefaults {
    return {
      id: null,
    };
  }
}
