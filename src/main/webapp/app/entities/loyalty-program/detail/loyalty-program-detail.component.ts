import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILoyaltyProgram } from '../loyalty-program.model';

@Component({
  standalone: true,
  selector: 'jhi-loyalty-program-detail',
  templateUrl: './loyalty-program-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LoyaltyProgramDetailComponent {
  loyaltyProgram = input<ILoyaltyProgram | null>(null);

  previousState(): void {
    window.history.back();
  }
}
