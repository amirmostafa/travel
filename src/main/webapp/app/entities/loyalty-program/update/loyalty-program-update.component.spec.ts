import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { LoyaltyProgramService } from '../service/loyalty-program.service';
import { ILoyaltyProgram } from '../loyalty-program.model';
import { LoyaltyProgramFormService } from './loyalty-program-form.service';

import { LoyaltyProgramUpdateComponent } from './loyalty-program-update.component';

describe('LoyaltyProgram Management Update Component', () => {
  let comp: LoyaltyProgramUpdateComponent;
  let fixture: ComponentFixture<LoyaltyProgramUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loyaltyProgramFormService: LoyaltyProgramFormService;
  let loyaltyProgramService: LoyaltyProgramService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LoyaltyProgramUpdateComponent],
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
      .overrideTemplate(LoyaltyProgramUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoyaltyProgramUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loyaltyProgramFormService = TestBed.inject(LoyaltyProgramFormService);
    loyaltyProgramService = TestBed.inject(LoyaltyProgramService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loyaltyProgram: ILoyaltyProgram = { id: 456 };

      activatedRoute.data = of({ loyaltyProgram });
      comp.ngOnInit();

      expect(comp.loyaltyProgram).toEqual(loyaltyProgram);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyProgram>>();
      const loyaltyProgram = { id: 123 };
      jest.spyOn(loyaltyProgramFormService, 'getLoyaltyProgram').mockReturnValue(loyaltyProgram);
      jest.spyOn(loyaltyProgramService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyProgram });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loyaltyProgram }));
      saveSubject.complete();

      // THEN
      expect(loyaltyProgramFormService.getLoyaltyProgram).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(loyaltyProgramService.update).toHaveBeenCalledWith(expect.objectContaining(loyaltyProgram));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyProgram>>();
      const loyaltyProgram = { id: 123 };
      jest.spyOn(loyaltyProgramFormService, 'getLoyaltyProgram').mockReturnValue({ id: null });
      jest.spyOn(loyaltyProgramService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyProgram: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loyaltyProgram }));
      saveSubject.complete();

      // THEN
      expect(loyaltyProgramFormService.getLoyaltyProgram).toHaveBeenCalled();
      expect(loyaltyProgramService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyProgram>>();
      const loyaltyProgram = { id: 123 };
      jest.spyOn(loyaltyProgramService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyProgram });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loyaltyProgramService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
