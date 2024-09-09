import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITourPackage } from '../tour-package.model';
import { TourPackageService } from '../service/tour-package.service';
import { TourPackageFormService, TourPackageFormGroup } from './tour-package-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tour-package-update',
  templateUrl: './tour-package-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TourPackageUpdateComponent implements OnInit {
  isSaving = false;
  tourPackage: ITourPackage | null = null;

  protected tourPackageService = inject(TourPackageService);
  protected tourPackageFormService = inject(TourPackageFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TourPackageFormGroup = this.tourPackageFormService.createTourPackageFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tourPackage }) => {
      this.tourPackage = tourPackage;
      if (tourPackage) {
        this.updateForm(tourPackage);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tourPackage = this.tourPackageFormService.getTourPackage(this.editForm);
    if (tourPackage.id !== null) {
      this.subscribeToSaveResponse(this.tourPackageService.update(tourPackage));
    } else {
      this.subscribeToSaveResponse(this.tourPackageService.create(tourPackage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITourPackage>>): void {
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

  protected updateForm(tourPackage: ITourPackage): void {
    this.tourPackage = tourPackage;
    this.tourPackageFormService.resetForm(this.editForm, tourPackage);
  }
}
