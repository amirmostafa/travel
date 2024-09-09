import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { BookingStatus } from 'app/entities/enumerations/booking-status.model';

export interface IBooking {
  id: number;
  bookingDate?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof BookingStatus | null;
  totalPrice?: number | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewBooking = Omit<IBooking, 'id'> & { id: null };
