import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { TestimonialService } from '../service/testimonial.service';
import { ITestimonial } from '../testimonial.model';
import { TestimonialFormService } from './testimonial-form.service';

import { TestimonialUpdateComponent } from './testimonial-update.component';

describe('Testimonial Management Update Component', () => {
  let comp: TestimonialUpdateComponent;
  let fixture: ComponentFixture<TestimonialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testimonialFormService: TestimonialFormService;
  let testimonialService: TestimonialService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TestimonialUpdateComponent],
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
      .overrideTemplate(TestimonialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestimonialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testimonialFormService = TestBed.inject(TestimonialFormService);
    testimonialService = TestBed.inject(TestimonialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const testimonial: ITestimonial = { id: 456 };

      activatedRoute.data = of({ testimonial });
      comp.ngOnInit();

      expect(comp.testimonial).toEqual(testimonial);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestimonial>>();
      const testimonial = { id: 123 };
      jest.spyOn(testimonialFormService, 'getTestimonial').mockReturnValue(testimonial);
      jest.spyOn(testimonialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testimonial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testimonial }));
      saveSubject.complete();

      // THEN
      expect(testimonialFormService.getTestimonial).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(testimonialService.update).toHaveBeenCalledWith(expect.objectContaining(testimonial));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestimonial>>();
      const testimonial = { id: 123 };
      jest.spyOn(testimonialFormService, 'getTestimonial').mockReturnValue({ id: null });
      jest.spyOn(testimonialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testimonial: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testimonial }));
      saveSubject.complete();

      // THEN
      expect(testimonialFormService.getTestimonial).toHaveBeenCalled();
      expect(testimonialService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestimonial>>();
      const testimonial = { id: 123 };
      jest.spyOn(testimonialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testimonial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testimonialService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
