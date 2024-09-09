import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAgencyService } from '../agency-service.model';
import { AgencyServiceService } from '../service/agency-service.service';
import { AgencyServiceFormService, AgencyServiceFormGroup } from './agency-service-form.service';

@Component({
  standalone: true,
  selector: 'jhi-agency-service-update',
  templateUrl: './agency-service-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgencyServiceUpdateComponent implements OnInit {
  isSaving = false;
  agencyService: IAgencyService | null = null;

  protected agencyServiceService = inject(AgencyServiceService);
  protected agencyServiceFormService = inject(AgencyServiceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AgencyServiceFormGroup = this.agencyServiceFormService.createAgencyServiceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agencyService }) => {
      this.agencyService = agencyService;
      if (agencyService) {
        this.updateForm(agencyService);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agencyService = this.agencyServiceFormService.getAgencyService(this.editForm);
    if (agencyService.id !== null) {
      this.subscribeToSaveResponse(this.agencyServiceService.update(agencyService));
    } else {
      this.subscribeToSaveResponse(this.agencyServiceService.create(agencyService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgencyService>>): void {
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

  protected updateForm(agencyService: IAgencyService): void {
    this.agencyService = agencyService;
    this.agencyServiceFormService.resetForm(this.editForm, agencyService);
  }
}
