import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 1576,
  login: 'W',
};

export const sampleWithPartialData: IUser = {
  id: 1877,
  login: '3@aX\\SVW\\TG95xuH\\-EFm5',
};

export const sampleWithFullData: IUser = {
  id: 15776,
  login: '*pglt@VZgH\\yiTjgAH\\jmyRLl',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
