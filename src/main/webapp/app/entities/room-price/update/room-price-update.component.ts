import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { IRoomPrice } from '../room-price.model';
import { RoomPriceService } from '../service/room-price.service';
import { RoomPriceFormService, RoomPriceFormGroup } from './room-price-form.service';

@Component({
  standalone: true,
  selector: 'jhi-room-price-update',
  templateUrl: './room-price-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RoomPriceUpdateComponent implements OnInit {
  isSaving = false;
  roomPrice: IRoomPrice | null = null;

  roomsSharedCollection: IRoom[] = [];

  protected roomPriceService = inject(RoomPriceService);
  protected roomPriceFormService = inject(RoomPriceFormService);
  protected roomService = inject(RoomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RoomPriceFormGroup = this.roomPriceFormService.createRoomPriceFormGroup();

  compareRoom = (o1: IRoom | null, o2: IRoom | null): boolean => this.roomService.compareRoom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomPrice }) => {
      this.roomPrice = roomPrice;
      if (roomPrice) {
        this.updateForm(roomPrice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roomPrice = this.roomPriceFormService.getRoomPrice(this.editForm);
    if (roomPrice.id !== null) {
      this.subscribeToSaveResponse(this.roomPriceService.update(roomPrice));
    } else {
      this.subscribeToSaveResponse(this.roomPriceService.create(roomPrice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomPrice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(roomPrice: IRoomPrice): void {
    this.roomPrice = roomPrice;
    this.roomPriceFormService.resetForm(this.editForm, roomPrice);

    this.roomsSharedCollection = this.roomService.addRoomToCollectionIfMissing<IRoom>(this.roomsSharedCollection, roomPrice.room);
  }

  protected loadRelationshipsOptions(): void {
    this.roomService
      .query()
      .pipe(map((res: HttpResponse<IRoom[]>) => res.body ?? []))
      .pipe(map((rooms: IRoom[]) => this.roomService.addRoomToCollectionIfMissing<IRoom>(rooms, this.roomPrice?.room)))
      .subscribe((rooms: IRoom[]) => (this.roomsSharedCollection = rooms));
  }
}
