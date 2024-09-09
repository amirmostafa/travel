import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 15684,
  amount: 12620.98,
  paymentMethod: 'CREDIT_CARD',
  paymentDate: dayjs('2024-09-08'),
  status: 'PENDING',
};

export const sampleWithPartialData: IPayment = {
  id: 29658,
  amount: 17871.52,
  paymentMethod: 'CREDIT_CARD',
  paymentDate: dayjs('2024-09-08'),
  status: 'FAILED',
};

export const sampleWithFullData: IPayment = {
  id: 25860,
  amount: 30729.18,
  paymentMethod: 'PAYPAL',
  paymentDate: dayjs('2024-09-08'),
  status: 'COMPLETED',
};

export const sampleWithNewData: NewPayment = {
  amount: 14352.11,
  paymentMethod: 'PAYPAL',
  paymentDate: dayjs('2024-09-08'),
  status: 'COMPLETED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
