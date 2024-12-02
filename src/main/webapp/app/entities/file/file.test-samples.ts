import dayjs from 'dayjs/esm';

import { IFile, NewFile } from './file.model';

export const sampleWithRequiredData: IFile = {
  id: 18728,
  name: 'alongside birth',
  type: 'INVOICE',
};

export const sampleWithPartialData: IFile = {
  id: 21193,
  name: 'off overproduce',
  type: 'DOCUMENT',
  description: 'utilized',
  rawFile: '../fake-data/blob/hipster.png',
  rawFileContentType: 'unknown',
};

export const sampleWithFullData: IFile = {
  id: 4824,
  name: 'majestically',
  type: 'INVOICE',
  amount: 25747.67,
  fileDate: dayjs('2024-12-01'),
  description: 'overconfidently yowza coolly',
  hash: 'rich',
  fileSize: 20997,
  path: 'little',
  rawFile: '../fake-data/blob/hipster.png',
  rawFileContentType: 'unknown',
  uploadedDate: dayjs('2024-11-30T08:33'),
};

export const sampleWithNewData: NewFile = {
  name: 'under',
  type: 'DOCUMENT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
