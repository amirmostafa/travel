import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TestimonialDetailComponent } from './testimonial-detail.component';

describe('Testimonial Management Detail Component', () => {
  let comp: TestimonialDetailComponent;
  let fixture: ComponentFixture<TestimonialDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestimonialDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TestimonialDetailComponent,
              resolve: { testimonial: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TestimonialDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestimonialDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load testimonial on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TestimonialDetailComponent);

      // THEN
      expect(instance.testimonial()).toEqual(expect.objectContaining({ id: 123 }));
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
