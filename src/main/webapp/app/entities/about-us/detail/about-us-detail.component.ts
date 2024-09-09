import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAboutUs } from '../about-us.model';

@Component({
  standalone: true,
  selector: 'jhi-about-us-detail',
  templateUrl: './about-us-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AboutUsDetailComponent {
  aboutUs = input<IAboutUs | null>(null);

  previousState(): void {
    window.history.back();
  }
}
