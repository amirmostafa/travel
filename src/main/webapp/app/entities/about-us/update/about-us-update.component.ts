import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAboutUs } from '../about-us.model';
import { AboutUsService } from '../service/about-us.service';
import { AboutUsFormService, AboutUsFormGroup } from './about-us-form.service';

@Component({
  standalone: true,
  selector: 'jhi-about-us-update',
  templateUrl: './about-us-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AboutUsUpdateComponent implements OnInit {
  isSaving = false;
  aboutUs: IAboutUs | null = null;

  protected aboutUsService = inject(AboutUsService);
  protected aboutUsFormService = inject(AboutUsFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AboutUsFormGroup = this.aboutUsFormService.createAboutUsFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aboutUs }) => {
      this.aboutUs = aboutUs;
      if (aboutUs) {
        this.updateForm(aboutUs);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aboutUs = this.aboutUsFormService.getAboutUs(this.editForm);
    if (aboutUs.id !== null) {
      this.subscribeToSaveResponse(this.aboutUsService.update(aboutUs));
    } else {
      this.subscribeToSaveResponse(this.aboutUsService.create(aboutUs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAboutUs>>): void {
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

  protected updateForm(aboutUs: IAboutUs): void {
    this.aboutUs = aboutUs;
    this.aboutUsFormService.resetForm(this.editForm, aboutUs);
  }
}
