import dayjs from 'dayjs/esm';
import { IBooking } from 'app/entities/booking/booking.model';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface IPayment {
  id: number;
  amount?: number | null;
  paymentMethod?: keyof typeof PaymentMethod | null;
  paymentDate?: dayjs.Dayjs | null;
  status?: keyof typeof PaymentStatus | null;
  booking?: Pick<IBooking, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
