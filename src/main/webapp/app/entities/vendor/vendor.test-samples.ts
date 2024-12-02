import { IVendor, NewVendor } from './vendor.model';

export const sampleWithRequiredData: IVendor = {
  id: 10473,
  name: 'twine failing black',
};

export const sampleWithPartialData: IVendor = {
  id: 31700,
  name: 'fathom sleepy',
  contactPhone: 'meanwhile',
  address: 'sonar how',
};

export const sampleWithFullData: IVendor = {
  id: 12555,
  name: 'past thoroughly',
  contactEmail: 'alongside idealistic yippee',
  contactPhone: 'supposing exotic',
  address: 'terribly',
};

export const sampleWithNewData: NewVendor = {
  name: 'likewise supposing',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
