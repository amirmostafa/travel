import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AboutUsDetailComponent } from './about-us-detail.component';

describe('AboutUs Management Detail Component', () => {
  let comp: AboutUsDetailComponent;
  let fixture: ComponentFixture<AboutUsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AboutUsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AboutUsDetailComponent,
              resolve: { aboutUs: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AboutUsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AboutUsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aboutUs on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AboutUsDetailComponent);

      // THEN
      expect(instance.aboutUs()).toEqual(expect.objectContaining({ id: 123 }));
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
