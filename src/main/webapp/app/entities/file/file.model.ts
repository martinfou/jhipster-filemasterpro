import dayjs from 'dayjs/esm';
import { IProject } from 'app/entities/project/project.model';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { FileType } from 'app/entities/enumerations/file-type.model';

export interface IFile {
  id: number;
  name?: string | null;
  type?: keyof typeof FileType | null;
  amount?: number | null;
  fileDate?: dayjs.Dayjs | null;
  description?: string | null;
  hash?: string | null;
  fileSize?: number | null;
  path?: string | null;
  rawFile?: string | null;
  rawFileContentType?: string | null;
  uploadedDate?: dayjs.Dayjs | null;
  project?: Pick<IProject, 'id'> | null;
  vendor?: Pick<IVendor, 'id'> | null;
}

export type NewFile = Omit<IFile, 'id'> & { id: null };
