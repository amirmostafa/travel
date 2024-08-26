import { ITourPackage, NewTourPackage } from './tour-package.model';

export const sampleWithRequiredData: ITourPackage = {
  id: 758,
  name: 'homely',
  description: 'ouch',
  price: 960.01,
  durationDays: 20245,
  available: true,
};

export const sampleWithPartialData: ITourPackage = {
  id: 27777,
  name: 'flower',
  description: 'clueless motel',
  price: 16387.3,
  durationDays: 23346,
  available: false,
};

export const sampleWithFullData: ITourPackage = {
  id: 23871,
  name: 'even gently',
  description: 'athwart yuck',
  price: 27163.16,
  durationDays: 16791,
  available: false,
};

export const sampleWithNewData: NewTourPackage = {
  name: 'boo',
  description: 'aw try oh',
  price: 24456.25,
  durationDays: 29376,
  available: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
