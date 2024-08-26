import dayjs from 'dayjs/esm';
import { IRoom } from 'app/entities/room/room.model';
import { ITourPackage } from 'app/entities/tour-package/tour-package.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { BookingStatus } from 'app/entities/enumerations/booking-status.model';

export interface IBooking {
  id: number;
  bookingDate?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof BookingStatus | null;
  totalPrice?: number | null;
  room?: Pick<IRoom, 'id'> | null;
  tourPackage?: Pick<ITourPackage, 'id'> | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewBooking = Omit<IBooking, 'id'> & { id: null };
