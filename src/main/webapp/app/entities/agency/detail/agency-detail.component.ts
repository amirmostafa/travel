import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAgency } from '../agency.model';

@Component({
  standalone: true,
  selector: 'jhi-agency-detail',
  templateUrl: './agency-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AgencyDetailComponent {
  agency = input<IAgency | null>(null);

  previousState(): void {
    window.history.back();
  }
}
