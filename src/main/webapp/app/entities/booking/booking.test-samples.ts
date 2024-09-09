import dayjs from 'dayjs/esm';

import { IBooking, NewBooking } from './booking.model';

export const sampleWithRequiredData: IBooking = {
  id: 30726,
  bookingDate: dayjs('2024-08-26'),
  startDate: dayjs('2024-08-26'),
  endDate: dayjs('2024-08-26'),
  status: 'CANCELLED',
  totalPrice: 26386.76,
};

export const sampleWithPartialData: IBooking = {
  id: 14201,
  bookingDate: dayjs('2024-08-26'),
  startDate: dayjs('2024-08-26'),
  endDate: dayjs('2024-08-26'),
  status: 'CONFIRMED',
  totalPrice: 28527.39,
};

export const sampleWithFullData: IBooking = {
  id: 29873,
  bookingDate: dayjs('2024-08-25'),
  startDate: dayjs('2024-08-26'),
  endDate: dayjs('2024-08-26'),
  status: 'CONFIRMED',
  totalPrice: 5371.48,
};

export const sampleWithNewData: NewBooking = {
  bookingDate: dayjs('2024-08-26'),
  startDate: dayjs('2024-08-26'),
  endDate: dayjs('2024-08-26'),
  status: 'CONFIRMED',
  totalPrice: 3245.05,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
