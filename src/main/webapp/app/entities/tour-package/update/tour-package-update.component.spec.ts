import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAgency } from 'app/entities/agency/agency.model';
import { AgencyService } from 'app/entities/agency/service/agency.service';
import { TourPackageService } from '../service/tour-package.service';
import { ITourPackage } from '../tour-package.model';
import { TourPackageFormService } from './tour-package-form.service';

import { TourPackageUpdateComponent } from './tour-package-update.component';

describe('TourPackage Management Update Component', () => {
  let comp: TourPackageUpdateComponent;
  let fixture: ComponentFixture<TourPackageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tourPackageFormService: TourPackageFormService;
  let tourPackageService: TourPackageService;
  let agencyService: AgencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TourPackageUpdateComponent],
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
      .overrideTemplate(TourPackageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TourPackageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tourPackageFormService = TestBed.inject(TourPackageFormService);
    tourPackageService = TestBed.inject(TourPackageService);
    agencyService = TestBed.inject(AgencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Agency query and add missing value', () => {
      const tourPackage: ITourPackage = { id: 456 };
      const agency: IAgency = { id: 32313 };
      tourPackage.agency = agency;

      const agencyCollection: IAgency[] = [{ id: 3612 }];
      jest.spyOn(agencyService, 'query').mockReturnValue(of(new HttpResponse({ body: agencyCollection })));
      const additionalAgencies = [agency];
      const expectedCollection: IAgency[] = [...additionalAgencies, ...agencyCollection];
      jest.spyOn(agencyService, 'addAgencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tourPackage });
      comp.ngOnInit();

      expect(agencyService.query).toHaveBeenCalled();
      expect(agencyService.addAgencyToCollectionIfMissing).toHaveBeenCalledWith(
        agencyCollection,
        ...additionalAgencies.map(expect.objectContaining),
      );
      expect(comp.agenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tourPackage: ITourPackage = { id: 456 };
      const agency: IAgency = { id: 26251 };
      tourPackage.agency = agency;

      activatedRoute.data = of({ tourPackage });
      comp.ngOnInit();

      expect(comp.agenciesSharedCollection).toContain(agency);
      expect(comp.tourPackage).toEqual(tourPackage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITourPackage>>();
      const tourPackage = { id: 123 };
      jest.spyOn(tourPackageFormService, 'getTourPackage').mockReturnValue(tourPackage);
      jest.spyOn(tourPackageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tourPackage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tourPackage }));
      saveSubject.complete();

      // THEN
      expect(tourPackageFormService.getTourPackage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tourPackageService.update).toHaveBeenCalledWith(expect.objectContaining(tourPackage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITourPackage>>();
      const tourPackage = { id: 123 };
      jest.spyOn(tourPackageFormService, 'getTourPackage').mockReturnValue({ id: null });
      jest.spyOn(tourPackageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tourPackage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tourPackage }));
      saveSubject.complete();

      // THEN
      expect(tourPackageFormService.getTourPackage).toHaveBeenCalled();
      expect(tourPackageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITourPackage>>();
      const tourPackage = { id: 123 };
      jest.spyOn(tourPackageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tourPackage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tourPackageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAgency', () => {
      it('Should forward to agencyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(agencyService, 'compareAgency');
        comp.compareAgency(entity, entity2);
        expect(agencyService.compareAgency).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
