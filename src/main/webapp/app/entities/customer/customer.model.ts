export interface ICustomer {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  address?: string | null;
  loyaltyPoints?: number | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
