import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 26687,
  firstName: 'Ava',
  lastName: 'Lowe',
  email: 'ks@)Vn04_.6rT',
  phoneNumber: 'whoa',
};

export const sampleWithPartialData: ICustomer = {
  id: 14269,
  firstName: 'Lucious',
  lastName: 'Schulist',
  email: '<iV[)#@ZYW..OyV',
  phoneNumber: 'midst scope yowza',
  address: 'pattern',
};

export const sampleWithFullData: ICustomer = {
  id: 20417,
  firstName: 'Raphael',
  lastName: 'Cummings',
  email: ';Ot\\@9.}',
  phoneNumber: 'establish how unlike',
  address: 'reproachfully',
};

export const sampleWithNewData: NewCustomer = {
  firstName: 'Zelma',
  lastName: 'Upton',
  email: "d'BQ;@I.!j4P/f",
  phoneNumber: 'manhandle eaves like',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
