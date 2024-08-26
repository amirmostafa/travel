import dayjs from 'dayjs/esm';

import { IBooking, NewBooking } from './booking.model';

export const sampleWithRequiredData: IBooking = {
  id: 30726,
  bookingDate: dayjs('2024-08-26T04:26'),
  startDate: dayjs('2024-08-26T18:36'),
  endDate: dayjs('2024-08-26T08:06'),
  status: 'CANCELLED',
  totalPrice: 26386.76,
};

export const sampleWithPartialData: IBooking = {
  id: 14201,
  bookingDate: dayjs('2024-08-26T10:17'),
  startDate: dayjs('2024-08-26T05:18'),
  endDate: dayjs('2024-08-26T19:20'),
  status: 'CONFIRMED',
  totalPrice: 28527.39,
};

export const sampleWithFullData: IBooking = {
  id: 29873,
  bookingDate: dayjs('2024-08-25T20:57'),
  startDate: dayjs('2024-08-26T08:44'),
  endDate: dayjs('2024-08-26T16:39'),
  status: 'CONFIRMED',
  totalPrice: 5371.48,
};

export const sampleWithNewData: NewBooking = {
  bookingDate: dayjs('2024-08-26T11:02'),
  startDate: dayjs('2024-08-26T13:42'),
  endDate: dayjs('2024-08-26T14:17'),
  status: 'CONFIRMED',
  totalPrice: 3245.05,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
