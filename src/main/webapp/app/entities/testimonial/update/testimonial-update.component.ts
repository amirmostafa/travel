import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITestimonial } from '../testimonial.model';
import { TestimonialService } from '../service/testimonial.service';
import { TestimonialFormService, TestimonialFormGroup } from './testimonial-form.service';

@Component({
  standalone: true,
  selector: 'jhi-testimonial-update',
  templateUrl: './testimonial-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TestimonialUpdateComponent implements OnInit {
  isSaving = false;
  testimonial: ITestimonial | null = null;

  protected testimonialService = inject(TestimonialService);
  protected testimonialFormService = inject(TestimonialFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TestimonialFormGroup = this.testimonialFormService.createTestimonialFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testimonial }) => {
      this.testimonial = testimonial;
      if (testimonial) {
        this.updateForm(testimonial);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testimonial = this.testimonialFormService.getTestimonial(this.editForm);
    if (testimonial.id !== null) {
      this.subscribeToSaveResponse(this.testimonialService.update(testimonial));
    } else {
      this.subscribeToSaveResponse(this.testimonialService.create(testimonial));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestimonial>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(testimonial: ITestimonial): void {
    this.testimonial = testimonial;
    this.testimonialFormService.resetForm(this.editForm, testimonial);
  }
}
