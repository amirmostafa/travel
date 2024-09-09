import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAgencyService } from '../agency-service.model';

@Component({
  standalone: true,
  selector: 'jhi-agency-service-detail',
  templateUrl: './agency-service-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AgencyServiceDetailComponent {
  agencyService = input<IAgencyService | null>(null);

  previousState(): void {
    window.history.back();
  }
}
