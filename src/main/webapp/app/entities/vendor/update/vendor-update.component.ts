import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVendor } from '../vendor.model';
import { VendorService } from '../service/vendor.service';
import { VendorFormGroup, VendorFormService } from './vendor-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vendor-update',
  templateUrl: './vendor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VendorUpdateComponent implements OnInit {
  isSaving = false;
  vendor: IVendor | null = null;

  protected vendorService = inject(VendorService);
  protected vendorFormService = inject(VendorFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VendorFormGroup = this.vendorFormService.createVendorFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendor }) => {
      this.vendor = vendor;
      if (vendor) {
        this.updateForm(vendor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendor = this.vendorFormService.getVendor(this.editForm);
    if (vendor.id !== null) {
      this.subscribeToSaveResponse(this.vendorService.update(vendor));
    } else {
      this.subscribeToSaveResponse(this.vendorService.create(vendor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vendor: IVendor): void {
    this.vendor = vendor;
    this.vendorFormService.resetForm(this.editForm, vendor);
  }
}
