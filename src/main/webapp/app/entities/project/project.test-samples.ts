import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 14958,
  name: 'potentially weakly',
};

export const sampleWithPartialData: IProject = {
  id: 12623,
  name: 'lovingly anxiously markup',
  description: 'ouch mysteriously',
  lastUpdatedDate: dayjs('2024-11-30T15:47'),
};

export const sampleWithFullData: IProject = {
  id: 30583,
  name: 'comb but',
  description: 'loose ultimately',
  createdDate: dayjs('2024-11-30T17:39'),
  lastUpdatedDate: dayjs('2024-12-01T01:26'),
};

export const sampleWithNewData: NewProject = {
  name: 'yahoo gadzooks psst',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
