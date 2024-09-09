import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAboutUs } from '../about-us.model';
import { AboutUsService } from '../service/about-us.service';

@Component({
  standalone: true,
  templateUrl: './about-us-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AboutUsDeleteDialogComponent {
  aboutUs?: IAboutUs;

  protected aboutUsService = inject(AboutUsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aboutUsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
