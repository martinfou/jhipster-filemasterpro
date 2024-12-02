import dayjs from 'dayjs/esm';

export interface IProject {
  id: number;
  name?: string | null;
  description?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastUpdatedDate?: dayjs.Dayjs | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
