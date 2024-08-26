import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITourPackage } from '../tour-package.model';

@Component({
  standalone: true,
  selector: 'jhi-tour-package-detail',
  templateUrl: './tour-package-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TourPackageDetailComponent {
  tourPackage = input<ITourPackage | null>(null);

  previousState(): void {
    window.history.back();
  }
}
