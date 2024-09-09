import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILoyaltyProgram } from '../loyalty-program.model';
import { LoyaltyProgramService } from '../service/loyalty-program.service';

@Component({
  standalone: true,
  templateUrl: './loyalty-program-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LoyaltyProgramDeleteDialogComponent {
  loyaltyProgram?: ILoyaltyProgram;

  protected loyaltyProgramService = inject(LoyaltyProgramService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loyaltyProgramService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
