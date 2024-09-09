import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILoyaltyTransaction } from '../loyalty-transaction.model';
import { LoyaltyTransactionService } from '../service/loyalty-transaction.service';

@Component({
  standalone: true,
  templateUrl: './loyalty-transaction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LoyaltyTransactionDeleteDialogComponent {
  loyaltyTransaction?: ILoyaltyTransaction;

  protected loyaltyTransactionService = inject(LoyaltyTransactionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loyaltyTransactionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
