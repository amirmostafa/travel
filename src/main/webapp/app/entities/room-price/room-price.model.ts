import dayjs from 'dayjs/esm';
import { IRoom } from 'app/entities/room/room.model';

export interface IRoomPrice {
  id: number;
  price?: number | null;
  fromDate?: dayjs.Dayjs | null;
  toDate?: dayjs.Dayjs | null;
  room?: Pick<IRoom, 'id'> | null;
}

export type NewRoomPrice = Omit<IRoomPrice, 'id'> & { id: null };
