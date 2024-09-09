import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 9404,
  roomNumber: 'civilize irritating the',
  type: 'FAMILY',
};

export const sampleWithPartialData: IRoom = {
  id: 9575,
  roomNumber: 'aw observation meanwhile',
  type: 'DOUBLE',
};

export const sampleWithFullData: IRoom = {
  id: 26701,
  roomNumber: 'dabble',
  type: 'SUITE',
  description: 'shocking',
  discountPercentage: 82.46,
};

export const sampleWithNewData: NewRoom = {
  roomNumber: 'bangle',
  type: 'FAMILY',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
