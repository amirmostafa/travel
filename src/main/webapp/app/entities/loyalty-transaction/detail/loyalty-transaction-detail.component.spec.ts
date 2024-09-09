import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LoyaltyTransactionDetailComponent } from './loyalty-transaction-detail.component';

describe('LoyaltyTransaction Management Detail Component', () => {
  let comp: LoyaltyTransactionDetailComponent;
  let fixture: ComponentFixture<LoyaltyTransactionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoyaltyTransactionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LoyaltyTransactionDetailComponent,
              resolve: { loyaltyTransaction: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LoyaltyTransactionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoyaltyTransactionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load loyaltyTransaction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LoyaltyTransactionDetailComponent);

      // THEN
      expect(instance.loyaltyTransaction()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
