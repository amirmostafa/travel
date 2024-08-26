import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TourPackageDetailComponent } from './tour-package-detail.component';

describe('TourPackage Management Detail Component', () => {
  let comp: TourPackageDetailComponent;
  let fixture: ComponentFixture<TourPackageDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourPackageDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TourPackageDetailComponent,
              resolve: { tourPackage: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TourPackageDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TourPackageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tourPackage on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TourPackageDetailComponent);

      // THEN
      expect(instance.tourPackage()).toEqual(expect.objectContaining({ id: 123 }));
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
