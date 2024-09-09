import dayjs from 'dayjs/esm';

import { ITestimonial, NewTestimonial } from './testimonial.model';

export const sampleWithRequiredData: ITestimonial = {
  id: 1270,
  authorName: 'or when on',
  content: 'gah',
  date: dayjs('2024-09-08'),
};

export const sampleWithPartialData: ITestimonial = {
  id: 26995,
  authorName: 'obnoxiously',
  content: 'amid thief',
  rating: 1,
  date: dayjs('2024-09-08'),
};

export const sampleWithFullData: ITestimonial = {
  id: 20543,
  authorName: 'yahoo',
  content: 'wean close pfft',
  rating: 3,
  date: dayjs('2024-09-07'),
};

export const sampleWithNewData: NewTestimonial = {
  authorName: 'excluding',
  content: 'cautious consequently harmless',
  date: dayjs('2024-09-08'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
