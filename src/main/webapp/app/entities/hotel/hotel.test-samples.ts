import { IHotel, NewHotel } from './hotel.model';

export const sampleWithRequiredData: IHotel = {
  id: 29567,
  name: 'smoothly madly nor',
  address: 'conjugate sunbeam vastly',
  starRating: 5,
  contactNumber: 'regarding',
  email: 'R@?>.y."~rj2',
};

export const sampleWithPartialData: IHotel = {
  id: 9898,
  name: 'than discolour duh',
  address: 'yippee lest lyre',
  starRating: 4,
  contactNumber: 'excepting unconscious badly',
  email: '%6I*@tm?MI.szQ',
};

export const sampleWithFullData: IHotel = {
  id: 10211,
  name: 'vicinity',
  address: 'rapidly',
  starRating: 1,
  contactNumber: 'administration',
  email: '|n{4LU@6.T',
};

export const sampleWithNewData: NewHotel = {
  name: 'salute opposite grill',
  address: 'strictly boohoo',
  starRating: 5,
  contactNumber: 'indeed modulo',
  email: '_@xu+1c.f/b)4m',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
