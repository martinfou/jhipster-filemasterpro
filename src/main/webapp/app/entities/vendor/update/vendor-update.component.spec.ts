import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VendorService } from '../service/vendor.service';
import { IVendor } from '../vendor.model';
import { VendorFormService } from './vendor-form.service';

import { VendorUpdateComponent } from './vendor-update.component';

describe('Vendor Management Update Component', () => {
  let comp: VendorUpdateComponent;
  let fixture: ComponentFixture<VendorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vendorFormService: VendorFormService;
  let vendorService: VendorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VendorUpdateComponent],
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
      .overrideTemplate(VendorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VendorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vendorFormService = TestBed.inject(VendorFormService);
    vendorService = TestBed.inject(VendorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vendor: IVendor = { id: 456 };

      activatedRoute.data = of({ vendor });
      comp.ngOnInit();

      expect(comp.vendor).toEqual(vendor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVendor>>();
      const vendor = { id: 123 };
      jest.spyOn(vendorFormService, 'getVendor').mockReturnValue(vendor);
      jest.spyOn(vendorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vendor }));
      saveSubject.complete();

      // THEN
      expect(vendorFormService.getVendor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vendorService.update).toHaveBeenCalledWith(expect.objectContaining(vendor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVendor>>();
      const vendor = { id: 123 };
      jest.spyOn(vendorFormService, 'getVendor').mockReturnValue({ id: null });
      jest.spyOn(vendorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vendor }));
      saveSubject.complete();

      // THEN
      expect(vendorFormService.getVendor).toHaveBeenCalled();
      expect(vendorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVendor>>();
      const vendor = { id: 123 };
      jest.spyOn(vendorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vendorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
