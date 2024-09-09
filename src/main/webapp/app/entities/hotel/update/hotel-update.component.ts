import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITestimonial } from 'app/entities/testimonial/testimonial.model';
import { TestimonialService } from 'app/entities/testimonial/service/testimonial.service';
import { IHotel } from '../hotel.model';
import { HotelService } from '../service/hotel.service';
import { HotelFormService, HotelFormGroup } from './hotel-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hotel-update',
  templateUrl: './hotel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HotelUpdateComponent implements OnInit {
  isSaving = false;
  hotel: IHotel | null = null;

  testimonialsSharedCollection: ITestimonial[] = [];

  protected hotelService = inject(HotelService);
  protected hotelFormService = inject(HotelFormService);
  protected testimonialService = inject(TestimonialService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HotelFormGroup = this.hotelFormService.createHotelFormGroup();

  compareTestimonial = (o1: ITestimonial | null, o2: ITestimonial | null): boolean => this.testimonialService.compareTestimonial(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hotel }) => {
      this.hotel = hotel;
      if (hotel) {
        this.updateForm(hotel);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hotel = this.hotelFormService.getHotel(this.editForm);
    if (hotel.id !== null) {
      this.subscribeToSaveResponse(this.hotelService.update(hotel));
    } else {
      this.subscribeToSaveResponse(this.hotelService.create(hotel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHotel>>): void {
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

  protected updateForm(hotel: IHotel): void {
    this.hotel = hotel;
    this.hotelFormService.resetForm(this.editForm, hotel);

    this.testimonialsSharedCollection = this.testimonialService.addTestimonialToCollectionIfMissing<ITestimonial>(
      this.testimonialsSharedCollection,
      hotel.testimonial,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.testimonialService
      .query()
      .pipe(map((res: HttpResponse<ITestimonial[]>) => res.body ?? []))
      .pipe(
        map((testimonials: ITestimonial[]) =>
          this.testimonialService.addTestimonialToCollectionIfMissing<ITestimonial>(testimonials, this.hotel?.testimonial),
        ),
      )
      .subscribe((testimonials: ITestimonial[]) => (this.testimonialsSharedCollection = testimonials));
  }
}
