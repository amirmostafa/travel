import { IHotel, NewHotel } from './hotel.model';

export const sampleWithRequiredData: IHotel = {
  id: 25193,
  name: 'where even',
  address: 'elm',
  starRating: 4,
  contactNumber: 'save',
  email: 'r@)\\!f.w|2',
  countryCode: 'CM',
  cityCode: 'marginalize allay',
};

export const sampleWithPartialData: IHotel = {
  id: 28398,
  name: 'whose instead cant',
  address: 'keenly around hat',
  starRating: 5,
  contactNumber: 'until round',
  email: 'mtGf@}0H%[.6',
  countryCode: 'IQ',
  cityCode: 'lest',
};

export const sampleWithFullData: IHotel = {
  id: 28621,
  name: 'which gadzooks kindly',
  address: 'water furthermore',
  starRating: 1,
  contactNumber: 'bowl fatally',
  email: 'XKbZ@aF.rnf`',
  countryCode: 'AU',
  cityCode: 'but',
  imageUrl: 'aha',
};

export const sampleWithNewData: NewHotel = {
  name: 'gah',
  address: 'dimly even',
  starRating: 5,
  contactNumber: 'duh stagger whoop',
  email: '*[_U@f2"A.R*O)UI',
  countryCode: 'CF',
  cityCode: 'sentimental',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
