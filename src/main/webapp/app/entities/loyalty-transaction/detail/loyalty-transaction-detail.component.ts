import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILoyaltyTransaction } from '../loyalty-transaction.model';

@Component({
  standalone: true,
  selector: 'jhi-loyalty-transaction-detail',
  templateUrl: './loyalty-transaction-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LoyaltyTransactionDetailComponent {
  loyaltyTransaction = input<ILoyaltyTransaction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
