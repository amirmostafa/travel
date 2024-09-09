import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { LoyaltyTransactionService } from '../service/loyalty-transaction.service';
import { ILoyaltyTransaction } from '../loyalty-transaction.model';
import { LoyaltyTransactionFormService } from './loyalty-transaction-form.service';

import { LoyaltyTransactionUpdateComponent } from './loyalty-transaction-update.component';

describe('LoyaltyTransaction Management Update Component', () => {
  let comp: LoyaltyTransactionUpdateComponent;
  let fixture: ComponentFixture<LoyaltyTransactionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loyaltyTransactionFormService: LoyaltyTransactionFormService;
  let loyaltyTransactionService: LoyaltyTransactionService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LoyaltyTransactionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LoyaltyTransactionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoyaltyTransactionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loyaltyTransactionFormService = TestBed.inject(LoyaltyTransactionFormService);
    loyaltyTransactionService = TestBed.inject(LoyaltyTransactionService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const loyaltyTransaction: ILoyaltyTransaction = { id: 456 };
      const customer: ICustomer = { id: 3665 };
      loyaltyTransaction.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 7133 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ loyaltyTransaction });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const loyaltyTransaction: ILoyaltyTransaction = { id: 456 };
      const customer: ICustomer = { id: 16253 };
      loyaltyTransaction.customer = customer;

      activatedRoute.data = of({ loyaltyTransaction });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.loyaltyTransaction).toEqual(loyaltyTransaction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyTransaction>>();
      const loyaltyTransaction = { id: 123 };
      jest.spyOn(loyaltyTransactionFormService, 'getLoyaltyTransaction').mockReturnValue(loyaltyTransaction);
      jest.spyOn(loyaltyTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loyaltyTransaction }));
      saveSubject.complete();

      // THEN
      expect(loyaltyTransactionFormService.getLoyaltyTransaction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(loyaltyTransactionService.update).toHaveBeenCalledWith(expect.objectContaining(loyaltyTransaction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyTransaction>>();
      const loyaltyTransaction = { id: 123 };
      jest.spyOn(loyaltyTransactionFormService, 'getLoyaltyTransaction').mockReturnValue({ id: null });
      jest.spyOn(loyaltyTransactionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyTransaction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loyaltyTransaction }));
      saveSubject.complete();

      // THEN
      expect(loyaltyTransactionFormService.getLoyaltyTransaction).toHaveBeenCalled();
      expect(loyaltyTransactionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyTransaction>>();
      const loyaltyTransaction = { id: 123 };
      jest.spyOn(loyaltyTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loyaltyTransactionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
