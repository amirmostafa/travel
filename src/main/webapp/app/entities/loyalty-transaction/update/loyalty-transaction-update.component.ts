import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { LoyaltyTransactionService } from '../service/loyalty-transaction.service';
import { ILoyaltyTransaction } from '../loyalty-transaction.model';
import { LoyaltyTransactionFormService, LoyaltyTransactionFormGroup } from './loyalty-transaction-form.service';

@Component({
  standalone: true,
  selector: 'jhi-loyalty-transaction-update',
  templateUrl: './loyalty-transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LoyaltyTransactionUpdateComponent implements OnInit {
  isSaving = false;
  loyaltyTransaction: ILoyaltyTransaction | null = null;
  transactionTypeValues = Object.keys(TransactionType);

  customersSharedCollection: ICustomer[] = [];

  protected loyaltyTransactionService = inject(LoyaltyTransactionService);
  protected loyaltyTransactionFormService = inject(LoyaltyTransactionFormService);
  protected customerService = inject(CustomerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LoyaltyTransactionFormGroup = this.loyaltyTransactionFormService.createLoyaltyTransactionFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loyaltyTransaction }) => {
      this.loyaltyTransaction = loyaltyTransaction;
      if (loyaltyTransaction) {
        this.updateForm(loyaltyTransaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const loyaltyTransaction = this.loyaltyTransactionFormService.getLoyaltyTransaction(this.editForm);
    if (loyaltyTransaction.id !== null) {
      this.subscribeToSaveResponse(this.loyaltyTransactionService.update(loyaltyTransaction));
    } else {
      this.subscribeToSaveResponse(this.loyaltyTransactionService.create(loyaltyTransaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILoyaltyTransaction>>): void {
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

  protected updateForm(loyaltyTransaction: ILoyaltyTransaction): void {
    this.loyaltyTransaction = loyaltyTransaction;
    this.loyaltyTransactionFormService.resetForm(this.editForm, loyaltyTransaction);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      loyaltyTransaction.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.loyaltyTransaction?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
