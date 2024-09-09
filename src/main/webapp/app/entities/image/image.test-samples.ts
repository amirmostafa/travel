import { IImage, NewImage } from './image.model';

export const sampleWithRequiredData: IImage = {
  id: 29542,
  url: 'https://worried-mantel.com/',
};

export const sampleWithPartialData: IImage = {
  id: 28904,
  url: 'https://kind-significance.com',
};

export const sampleWithFullData: IImage = {
  id: 3158,
  url: 'https://unfit-shipper.com',
  description: 'sick',
};

export const sampleWithNewData: NewImage = {
  url: 'https://mellow-lady.com/',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
