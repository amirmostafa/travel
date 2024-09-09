import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { AboutUsService } from '../service/about-us.service';
import { IAboutUs } from '../about-us.model';
import { AboutUsFormService } from './about-us-form.service';

import { AboutUsUpdateComponent } from './about-us-update.component';

describe('AboutUs Management Update Component', () => {
  let comp: AboutUsUpdateComponent;
  let fixture: ComponentFixture<AboutUsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aboutUsFormService: AboutUsFormService;
  let aboutUsService: AboutUsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AboutUsUpdateComponent],
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
      .overrideTemplate(AboutUsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AboutUsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aboutUsFormService = TestBed.inject(AboutUsFormService);
    aboutUsService = TestBed.inject(AboutUsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aboutUs: IAboutUs = { id: 456 };

      activatedRoute.data = of({ aboutUs });
      comp.ngOnInit();

      expect(comp.aboutUs).toEqual(aboutUs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutUs>>();
      const aboutUs = { id: 123 };
      jest.spyOn(aboutUsFormService, 'getAboutUs').mockReturnValue(aboutUs);
      jest.spyOn(aboutUsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutUs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aboutUs }));
      saveSubject.complete();

      // THEN
      expect(aboutUsFormService.getAboutUs).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aboutUsService.update).toHaveBeenCalledWith(expect.objectContaining(aboutUs));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutUs>>();
      const aboutUs = { id: 123 };
      jest.spyOn(aboutUsFormService, 'getAboutUs').mockReturnValue({ id: null });
      jest.spyOn(aboutUsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutUs: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aboutUs }));
      saveSubject.complete();

      // THEN
      expect(aboutUsFormService.getAboutUs).toHaveBeenCalled();
      expect(aboutUsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutUs>>();
      const aboutUs = { id: 123 };
      jest.spyOn(aboutUsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutUs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aboutUsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
