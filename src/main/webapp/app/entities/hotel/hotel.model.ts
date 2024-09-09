import { ITestimonial } from 'app/entities/testimonial/testimonial.model';

export interface IHotel {
  id: number;
  name?: string | null;
  address?: string | null;
  starRating?: number | null;
  contactNumber?: string | null;
  email?: string | null;
  countryCode?: string | null;
  cityCode?: string | null;
  imageUrl?: string | null;
  testimonial?: Pick<ITestimonial, 'id'> | null;
}

export type NewHotel = Omit<IHotel, 'id'> & { id: null };
