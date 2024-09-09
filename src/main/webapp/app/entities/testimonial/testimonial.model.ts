import dayjs from 'dayjs/esm';

export interface ITestimonial {
  id: number;
  authorName?: string | null;
  content?: string | null;
  rating?: number | null;
  date?: dayjs.Dayjs | null;
}

export type NewTestimonial = Omit<ITestimonial, 'id'> & { id: null };
