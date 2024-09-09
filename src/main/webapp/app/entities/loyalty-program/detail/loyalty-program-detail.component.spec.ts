import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LoyaltyProgramDetailComponent } from './loyalty-program-detail.component';

describe('LoyaltyProgram Management Detail Component', () => {
  let comp: LoyaltyProgramDetailComponent;
  let fixture: ComponentFixture<LoyaltyProgramDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoyaltyProgramDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LoyaltyProgramDetailComponent,
              resolve: { loyaltyProgram: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LoyaltyProgramDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoyaltyProgramDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load loyaltyProgram on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LoyaltyProgramDetailComponent);

      // THEN
      expect(instance.loyaltyProgram()).toEqual(expect.objectContaining({ id: 123 }));
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
