import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';
import { FileType } from 'app/entities/enumerations/file-type.model';
import { FileService } from '../service/file.service';
import { IFile } from '../file.model';
import { FileFormGroup, FileFormService } from './file-form.service';

const SUFFIX_UNDERSCORE = '_';
const CURRENCY = '$';
@Component({
  standalone: true,
  selector: 'jhi-file-update',
  templateUrl: './file-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FileUpdateComponent implements OnInit {
  isSaving = false;
  file: IFile | null = null;
  fileTypeValues = Object.keys(FileType);
  formState = {
    fileType: '',
    projectType: '',
    date: '',
    vendor: '',
    amount: '',
    description: '',
  };

  projectsSharedCollection: IProject[] = [];
  vendorsSharedCollection: IVendor[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected fileService = inject(FileService);
  protected fileFormService = inject(FileFormService);
  protected projectService = inject(ProjectService);
  protected vendorService = inject(VendorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FileFormGroup = this.fileFormService.createFileFormGroup();

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  compareVendor = (o1: IVendor | null, o2: IVendor | null): boolean => this.vendorService.compareVendor(o1, o2);

  ngOnInit(): void {
    this.editForm.get('type')?.valueChanges.subscribe(value => {
      this.formState.fileType = value ?? '';
      this.updateNameField();
    });

    this.editForm.get('project')?.valueChanges.subscribe(value => {
      this.formState.projectType = (value as IProject).name ?? '';
      this.updateNameField();
    });

    this.editForm.get('fileDate')?.valueChanges.subscribe(value => {
      this.formState.date = value ? value.format('YYYY-MM-DD') : '';
      this.updateNameField();
    });

    this.editForm.get('vendor')?.valueChanges.subscribe(value => {
      this.formState.vendor = (value as IVendor).name ?? '';
      this.updateNameField();
    });

    this.editForm.get('amount')?.valueChanges.subscribe(value => {
      this.formState.amount = value?.toString() ?? '';
      this.updateNameField();
    });

    this.editForm.get('description')?.valueChanges.subscribe(value => {
      this.formState.description = value ?? '';
      this.updateNameField();
    });

    this.activatedRoute.data.subscribe(({ file }) => {
      this.file = file;
      if (file) {
        this.updateForm(file);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('filemasterproApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const file = this.fileFormService.getFile(this.editForm);
    if (file.id !== null) {
      this.subscribeToSaveResponse(this.fileService.update(file));
    } else {
      this.subscribeToSaveResponse(this.fileService.create(file));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFile>>): void {
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

  protected updateNameField(): void {
    this.editForm.patchValue({
      name: this.getFileType() + this.getProjectType() + this.getDate() + this.getVendor() + this.getAmount() + this.formState.description,
    });
  }

  protected getFileType(): string {
    if (!this.formState.fileType) {
      return '';
    }
    return this.formState.fileType.toLowerCase() + SUFFIX_UNDERSCORE;
  }

  protected getProjectType(): string {
    if (!this.formState.projectType) {
      return '';
    }
    return this.formState.projectType + SUFFIX_UNDERSCORE;
  }

  protected getDate(): string {
    if (!this.formState.date) {
      return '';
    }
    return this.formState.date + SUFFIX_UNDERSCORE;
  }

  protected getVendor(): string {
    if (!this.formState.vendor) {
      return '';
    }
    return this.formState.vendor + SUFFIX_UNDERSCORE;
  }

  protected getAmount(): string {
    if (!this.formState.amount) {
      return '';
    }
    const suffix = !this.formState.description ? '' : SUFFIX_UNDERSCORE;
    return CURRENCY + this.formState.amount + suffix;
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(file: IFile): void {
    this.file = file;
    this.fileFormService.resetForm(this.editForm, file);

    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      file.project,
    );
    this.vendorsSharedCollection = this.vendorService.addVendorToCollectionIfMissing<IVendor>(this.vendorsSharedCollection, file.vendor);
  }

  protected loadRelationshipsOptions(): void {
    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.file?.project)))
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));

    this.vendorService
      .query()
      .pipe(map((res: HttpResponse<IVendor[]>) => res.body ?? []))
      .pipe(map((vendors: IVendor[]) => this.vendorService.addVendorToCollectionIfMissing<IVendor>(vendors, this.file?.vendor)))
      .subscribe((vendors: IVendor[]) => (this.vendorsSharedCollection = vendors));
  }
}
