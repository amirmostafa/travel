import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3168,
  firstName: 'Agustina',
  lastName: 'Emard',
  email: 'st)Vn@4_.6rT',
  phoneNumber: 'whoa',
  loyaltyPoints: 902,
};

export const sampleWithPartialData: ICustomer = {
  id: 21022,
  firstName: 'Kaitlyn',
  lastName: 'Wilderman',
  email: 'iV@)#QZ.W.OO',
  phoneNumber: 'cluttered in incidentally',
  address: 'painter naturally',
  loyaltyPoints: 9179,
};

export const sampleWithFullData: ICustomer = {
  id: 16031,
  firstName: 'Sheridan',
  lastName: 'Braun',
  email: '-}@8Renm2.&Z',
  phoneNumber: 'canopy',
  address: 'gee vaguely but',
  loyaltyPoints: 13876,
};

export const sampleWithNewData: NewCustomer = {
  firstName: 'Sadie',
  lastName: 'Satterfield',
  email: 'P/@q1nkb."QV-;c',
  phoneNumber: 'claw',
  loyaltyPoints: 22811,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
