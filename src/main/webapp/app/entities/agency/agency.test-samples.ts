import { IAgency, NewAgency } from './agency.model';

export const sampleWithRequiredData: IAgency = {
  id: 29917,
  name: 'who however',
  address: 'within lest sorghum',
  contactNumber: 'afore plus unaccountably',
  email: 'C@~_dX;.t2qEop',
};

export const sampleWithPartialData: IAgency = {
  id: 21850,
  name: 'yowza publicity reclamation',
  address: 'around impossible',
  contactNumber: 'regarding officially furthermore',
  email: ':-|@!,P~.N7Wt!B',
};

export const sampleWithFullData: IAgency = {
  id: 27423,
  name: 'lovingly',
  address: 'abjure across',
  contactNumber: 'pfft viciously shop',
  email: "0@HM.XK/c'",
  website: 'hmph',
};

export const sampleWithNewData: NewAgency = {
  name: 'thoroughly',
  address: 'meaningfully',
  contactNumber: 'enthusiastically er',
  email: 'd@Cn[+r_.ou[H',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
