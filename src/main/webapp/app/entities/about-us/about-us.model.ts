export interface IAboutUs {
  id: number;
  description?: string | null;
  contactDetails?: string | null;
  additionalInfo?: string | null;
}

export type NewAboutUs = Omit<IAboutUs, 'id'> & { id: null };
