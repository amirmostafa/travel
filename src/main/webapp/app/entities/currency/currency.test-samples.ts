import { ICurrency, NewCurrency } from './currency.model';

export const sampleWithRequiredData: ICurrency = {
  id: 15782,
  code: 'digitalise insubstantial',
  name: 'vigilant yum manicure',
  exchangeRate: 11612.05,
  isDefault: true,
};

export const sampleWithPartialData: ICurrency = {
  id: 12360,
  code: 'freely woefully commonly',
  name: 'pluck',
  exchangeRate: 27327.96,
  isDefault: true,
};

export const sampleWithFullData: ICurrency = {
  id: 24496,
  code: 'snaggle',
  name: 'hitchhike',
  symbol: 'moist',
  exchangeRate: 9828.84,
  isDefault: true,
};

export const sampleWithNewData: NewCurrency = {
  code: 'gripe tidy',
  name: 'throughout kiddingly',
  exchangeRate: 2191.44,
  isDefault: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
