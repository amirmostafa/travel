export interface IAgencyService {
  id: number;
  title?: string | null;
  icon?: string | null;
  content?: string | null;
}

export type NewAgencyService = Omit<IAgencyService, 'id'> & { id: null };
