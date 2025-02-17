import dayjs from 'dayjs/esm';

import { IRoomPrice, NewRoomPrice } from './room-price.model';

export const sampleWithRequiredData: IRoomPrice = {
  id: 29516,
  price: 8484.14,
  fromDate: dayjs('2024-08-26'),
  toDate: dayjs('2024-08-26'),
};

export const sampleWithPartialData: IRoomPrice = {
  id: 13253,
  price: 21298.33,
  fromDate: dayjs('2024-08-26'),
  toDate: dayjs('2024-08-26'),
};

export const sampleWithFullData: IRoomPrice = {
  id: 29254,
  price: 6612.28,
  fromDate: dayjs('2024-08-25'),
  toDate: dayjs('2024-08-26'),
};

export const sampleWithNewData: NewRoomPrice = {
  price: 14633.96,
  fromDate: dayjs('2024-08-25'),
  toDate: dayjs('2024-08-25'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
