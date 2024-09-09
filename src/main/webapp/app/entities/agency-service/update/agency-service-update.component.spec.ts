import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { AgencyServiceService } from '../service/agency-service.service';
import { IAgencyService } from '../agency-service.model';
import { AgencyServiceFormService } from './agency-service-form.service';

import { AgencyServiceUpdateComponent } from './agency-service-update.component';

describe('AgencyService Management Update Component', () => {
  let comp: AgencyServiceUpdateComponent;
  let fixture: ComponentFixture<AgencyServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agencyServiceFormService: AgencyServiceFormService;
  let agencyServiceService: AgencyServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AgencyServiceUpdateComponent],
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
      .overrideTemplate(AgencyServiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgencyServiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agencyServiceFormService = TestBed.inject(AgencyServiceFormService);
    agencyServiceService = TestBed.inject(AgencyServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const agencyService: IAgencyService = { id: 456 };

      activatedRoute.data = of({ agencyService });
      comp.ngOnInit();

      expect(comp.agencyService).toEqual(agencyService);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgencyService>>();
      const agencyService = { id: 123 };
      jest.spyOn(agencyServiceFormService, 'getAgencyService').mockReturnValue(agencyService);
      jest.spyOn(agencyServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agencyService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agencyService }));
      saveSubject.complete();

      // THEN
      expect(agencyServiceFormService.getAgencyService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agencyServiceService.update).toHaveBeenCalledWith(expect.objectContaining(agencyService));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgencyService>>();
      const agencyService = { id: 123 };
      jest.spyOn(agencyServiceFormService, 'getAgencyService').mockReturnValue({ id: null });
      jest.spyOn(agencyServiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agencyService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agencyService }));
      saveSubject.complete();

      // THEN
      expect(agencyServiceFormService.getAgencyService).toHaveBeenCalled();
      expect(agencyServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgencyService>>();
      const agencyService = { id: 123 };
      jest.spyOn(agencyServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agencyService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agencyServiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
