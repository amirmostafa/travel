import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITestimonial } from '../testimonial.model';
import { TestimonialService } from '../service/testimonial.service';

@Component({
  standalone: true,
  templateUrl: './testimonial-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TestimonialDeleteDialogComponent {
  testimonial?: ITestimonial;

  protected testimonialService = inject(TestimonialService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testimonialService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
