import { ILoyaltyProgram, NewLoyaltyProgram } from './loyalty-program.model';

export const sampleWithRequiredData: ILoyaltyProgram = {
  id: 9302,
  name: 'tired er',
  pointsPerDollar: 23006,
  rewardThreshold: 31702,
};

export const sampleWithPartialData: ILoyaltyProgram = {
  id: 2853,
  name: 'but rude hm',
  pointsPerDollar: 29832,
  rewardThreshold: 10919,
};

export const sampleWithFullData: ILoyaltyProgram = {
  id: 30112,
  name: 'identical yahoo',
  description: 'which yet',
  pointsPerDollar: 8277,
  rewardThreshold: 30773,
};

export const sampleWithNewData: NewLoyaltyProgram = {
  name: 'above bathe',
  pointsPerDollar: 28899,
  rewardThreshold: 25630,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
