import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFile, NewFile } from '../file.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFile for edit and NewFileFormGroupInput for create.
 */
type FileFormGroupInput = IFile | PartialWithRequiredKeyOf<NewFile>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFile | NewFile> = Omit<T, 'uploadedDate'> & {
  uploadedDate?: string | null;
};

type FileFormRawValue = FormValueOf<IFile>;

type NewFileFormRawValue = FormValueOf<NewFile>;

type FileFormDefaults = Pick<NewFile, 'id' | 'uploadedDate'>;

type FileFormGroupContent = {
  id: FormControl<FileFormRawValue['id'] | NewFile['id']>;
  name: FormControl<FileFormRawValue['name']>;
  type: FormControl<FileFormRawValue['type']>;
  amount: FormControl<FileFormRawValue['amount']>;
  fileDate: FormControl<FileFormRawValue['fileDate']>;
  description: FormControl<FileFormRawValue['description']>;
  hash: FormControl<FileFormRawValue['hash']>;
  fileSize: FormControl<FileFormRawValue['fileSize']>;
  path: FormControl<FileFormRawValue['path']>;
  rawFile: FormControl<FileFormRawValue['rawFile']>;
  rawFileContentType: FormControl<FileFormRawValue['rawFileContentType']>;
  uploadedDate: FormControl<FileFormRawValue['uploadedDate']>;
  project: FormControl<FileFormRawValue['project']>;
  vendor: FormControl<FileFormRawValue['vendor']>;
};

export type FileFormGroup = FormGroup<FileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FileFormService {
  createFileFormGroup(file: FileFormGroupInput = { id: null }): FileFormGroup {
    const fileRawValue = this.convertFileToFileRawValue({
      ...this.getFormDefaults(),
      ...file,
    });
    return new FormGroup<FileFormGroupContent>({
      id: new FormControl(
        { value: fileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(fileRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(fileRawValue.type, {
        validators: [Validators.required],
      }),
      amount: new FormControl(fileRawValue.amount),
      fileDate: new FormControl(fileRawValue.fileDate),
      description: new FormControl(fileRawValue.description),
      hash: new FormControl(fileRawValue.hash),
      fileSize: new FormControl(fileRawValue.fileSize),
      path: new FormControl(fileRawValue.path),
      rawFile: new FormControl(fileRawValue.rawFile),
      rawFileContentType: new FormControl(fileRawValue.rawFileContentType),
      uploadedDate: new FormControl(fileRawValue.uploadedDate),
      project: new FormControl(fileRawValue.project),
      vendor: new FormControl(fileRawValue.vendor),
    });
  }

  getFile(form: FileFormGroup): IFile | NewFile {
    return this.convertFileRawValueToFile(form.getRawValue() as FileFormRawValue | NewFileFormRawValue);
  }

  resetForm(form: FileFormGroup, file: FileFormGroupInput): void {
    const fileRawValue = this.convertFileToFileRawValue({ ...this.getFormDefaults(), ...file });
    form.reset(
      {
        ...fileRawValue,
        id: { value: fileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FileFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadedDate: currentTime,
    };
  }

  private convertFileRawValueToFile(rawFile: FileFormRawValue | NewFileFormRawValue): IFile | NewFile {
    return {
      ...rawFile,
      uploadedDate: dayjs(rawFile.uploadedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFileToFileRawValue(
    file: IFile | (Partial<NewFile> & FileFormDefaults),
  ): FileFormRawValue | PartialWithRequiredKeyOf<NewFileFormRawValue> {
    return {
      ...file,
      uploadedDate: file.uploadedDate ? file.uploadedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
