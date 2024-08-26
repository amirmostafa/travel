import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'add71faa-88ce-4c52-9efa-68cedf3f1d05',
};

export const sampleWithPartialData: IAuthority = {
  name: '488a56d8-89c8-456e-9ce9-7ba62d56ba47',
};

export const sampleWithFullData: IAuthority = {
  name: '40974e70-1937-40cb-a54e-8574a7399872',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
