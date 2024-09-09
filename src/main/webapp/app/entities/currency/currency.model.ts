export interface ICurrency {
  id: number;
  code?: string | null;
  name?: string | null;
  symbol?: string | null;
  exchangeRate?: number | null;
  isDefault?: boolean | null;
}

export type NewCurrency = Omit<ICurrency, 'id'> & { id: null };
