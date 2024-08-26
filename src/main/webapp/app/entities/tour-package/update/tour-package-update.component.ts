import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAgency } from 'app/entities/agency/agency.model';
import { AgencyService } from 'app/entities/agency/service/agency.service';
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

  agenciesSharedCollection: IAgency[] = [];

  protected tourPackageService = inject(TourPackageService);
  protected tourPackageFormService = inject(TourPackageFormService);
  protected agencyService = inject(AgencyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TourPackageFormGroup = this.tourPackageFormService.createTourPackageFormGroup();

  compareAgency = (o1: IAgency | null, o2: IAgency | null): boolean => this.agencyService.compareAgency(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tourPackage }) => {
      this.tourPackage = tourPackage;
      if (tourPackage) {
        this.updateForm(tourPackage);
      }

      this.loadRelationshipsOptions();
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

    this.agenciesSharedCollection = this.agencyService.addAgencyToCollectionIfMissing<IAgency>(
      this.agenciesSharedCollection,
      tourPackage.agency,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.agencyService
      .query()
      .pipe(map((res: HttpResponse<IAgency[]>) => res.body ?? []))
      .pipe(map((agencies: IAgency[]) => this.agencyService.addAgencyToCollectionIfMissing<IAgency>(agencies, this.tourPackage?.agency)))
      .subscribe((agencies: IAgency[]) => (this.agenciesSharedCollection = agencies));
  }
}
