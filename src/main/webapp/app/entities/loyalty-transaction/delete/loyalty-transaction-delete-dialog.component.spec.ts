jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LoyaltyTransactionService } from '../service/loyalty-transaction.service';

import { LoyaltyTransactionDeleteDialogComponent } from './loyalty-transaction-delete-dialog.component';

describe('LoyaltyTransaction Management Delete Component', () => {
  let comp: LoyaltyTransactionDeleteDialogComponent;
  let fixture: ComponentFixture<LoyaltyTransactionDeleteDialogComponent>;
  let service: LoyaltyTransactionService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LoyaltyTransactionDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(LoyaltyTransactionDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LoyaltyTransactionDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LoyaltyTransactionService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
