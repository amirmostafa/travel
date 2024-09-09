export interface ITourPackage {
  id: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  durationDays?: number | null;
  available?: boolean | null;
}

export type NewTourPackage = Omit<ITourPackage, 'id'> & { id: null };
