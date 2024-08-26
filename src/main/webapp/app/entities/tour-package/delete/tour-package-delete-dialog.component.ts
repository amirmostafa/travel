import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITourPackage } from '../tour-package.model';
import { TourPackageService } from '../service/tour-package.service';

@Component({
  standalone: true,
  templateUrl: './tour-package-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TourPackageDeleteDialogComponent {
  tourPackage?: ITourPackage;

  protected tourPackageService = inject(TourPackageService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tourPackageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
