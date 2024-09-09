import dayjs from 'dayjs/esm';

import { ILoyaltyTransaction, NewLoyaltyTransaction } from './loyalty-transaction.model';

export const sampleWithRequiredData: ILoyaltyTransaction = {
  id: 16766,
  date: dayjs('2024-09-08'),
  points: 27022,
  transactionType: 'REDEEMED',
};

export const sampleWithPartialData: ILoyaltyTransaction = {
  id: 3976,
  date: dayjs('2024-09-08'),
  points: 9813,
  transactionType: 'REDEEMED',
  description: 'publishing gee',
};

export const sampleWithFullData: ILoyaltyTransaction = {
  id: 31792,
  date: dayjs('2024-09-08'),
  points: 31304,
  transactionType: 'REDEEMED',
  description: 'especially nor',
};

export const sampleWithNewData: NewLoyaltyTransaction = {
  date: dayjs('2024-09-07'),
  points: 11484,
  transactionType: 'EARNED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
