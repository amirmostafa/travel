export interface IHotel {
  id: number;
  name?: string | null;
  address?: string | null;
  starRating?: number | null;
  contactNumber?: string | null;
  email?: string | null;
}

export type NewHotel = Omit<IHotel, 'id'> & { id: null };
