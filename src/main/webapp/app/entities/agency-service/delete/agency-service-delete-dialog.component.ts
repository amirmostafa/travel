import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAgencyService } from '../agency-service.model';
import { AgencyServiceService } from '../service/agency-service.service';

@Component({
  standalone: true,
  templateUrl: './agency-service-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AgencyServiceDeleteDialogComponent {
  agencyService?: IAgencyService;

  protected agencyServiceService = inject(AgencyServiceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agencyServiceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
