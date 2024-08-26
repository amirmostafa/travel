import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 6600,
  roomNumber: 'titrate resign',
  type: 'SUITE',
};

export const sampleWithPartialData: IRoom = {
  id: 26438,
  roomNumber: 'cotton flawed sling',
  type: 'FAMILY',
  discountPercentage: 28.19,
};

export const sampleWithFullData: IRoom = {
  id: 24359,
  roomNumber: 'until protect',
  type: 'SUITE',
  description: '../fake-data/blob/hipster.txt',
  discountPercentage: 71.38,
};

export const sampleWithNewData: NewRoom = {
  roomNumber: 'ugh less',
  type: 'FAMILY',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
