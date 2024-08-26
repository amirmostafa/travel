import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRoomPrice } from '../room-price.model';
import { RoomPriceService } from '../service/room-price.service';

@Component({
  standalone: true,
  templateUrl: './room-price-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RoomPriceDeleteDialogComponent {
  roomPrice?: IRoomPrice;

  protected roomPriceService = inject(RoomPriceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roomPriceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
