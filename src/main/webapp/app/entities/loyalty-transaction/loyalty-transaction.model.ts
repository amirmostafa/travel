import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

export interface ILoyaltyTransaction {
  id: number;
  date?: dayjs.Dayjs | null;
  points?: number | null;
  transactionType?: keyof typeof TransactionType | null;
  description?: string | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewLoyaltyTransaction = Omit<ILoyaltyTransaction, 'id'> & { id: null };
