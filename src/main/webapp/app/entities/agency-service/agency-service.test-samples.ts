import { IAgencyService, NewAgencyService } from './agency-service.model';

export const sampleWithRequiredData: IAgencyService = {
  id: 24010,
  title: 'bunker',
  content: 'snappy mmm',
};

export const sampleWithPartialData: IAgencyService = {
  id: 10582,
  title: 'into below lean',
  icon: 'cwtch',
  content: 'concerning between',
};

export const sampleWithFullData: IAgencyService = {
  id: 24278,
  title: 'covenant bunt kindheartedly',
  icon: 'lyocell chill an',
  content: 'pfft kookily',
};

export const sampleWithNewData: NewAgencyService = {
  title: 'pamper toward out',
  content: 'now',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
