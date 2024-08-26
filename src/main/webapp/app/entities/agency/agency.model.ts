export interface IAgency {
  id: number;
  name?: string | null;
  address?: string | null;
  contactNumber?: string | null;
  email?: string | null;
  website?: string | null;
}

export type NewAgency = Omit<IAgency, 'id'> & { id: null };
