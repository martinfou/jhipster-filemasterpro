import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'f241c660-c889-4d67-802c-38d79eda5cc5',
};

export const sampleWithPartialData: IAuthority = {
  name: '68675b7d-b37a-4200-999d-587812b3338f',
};

export const sampleWithFullData: IAuthority = {
  name: 'e74f3e8b-1f31-4a58-956f-79bb941b4e6a',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
