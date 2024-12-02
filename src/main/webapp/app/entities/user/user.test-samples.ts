import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 23563,
  login: '8wSx7Z',
};

export const sampleWithPartialData: IUser = {
  id: 9903,
  login: 'T@V',
};

export const sampleWithFullData: IUser = {
  id: 1301,
  login: 'Ua3V@XW\\;1a-eF\\-QaY\\RD\\AlYstd\\MX2',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
