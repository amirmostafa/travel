import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRoomPrice } from '../room-price.model';

@Component({
  standalone: true,
  selector: 'jhi-room-price-detail',
  templateUrl: './room-price-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RoomPriceDetailComponent {
  roomPrice = input<IRoomPrice | null>(null);

  previousState(): void {
    window.history.back();
  }
}
