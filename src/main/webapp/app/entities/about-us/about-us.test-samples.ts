import { IAboutUs, NewAboutUs } from './about-us.model';

export const sampleWithRequiredData: IAboutUs = {
  id: 24357,
  description: 'yuck or stride',
};

export const sampleWithPartialData: IAboutUs = {
  id: 12681,
  description: 'following flatline woot',
  additionalInfo: 'zany unless',
};

export const sampleWithFullData: IAboutUs = {
  id: 2532,
  description: 'upon inquisitively exactly',
  contactDetails: 'the encamp viciously',
  additionalInfo: 'meanwhile pfft',
};

export const sampleWithNewData: NewAboutUs = {
  description: 'notable',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
