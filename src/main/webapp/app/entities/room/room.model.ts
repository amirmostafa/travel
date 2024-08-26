import { IHotel } from 'app/entities/hotel/hotel.model';
import { RoomType } from 'app/entities/enumerations/room-type.model';

export interface IRoom {
  id: number;
  roomNumber?: string | null;
  type?: keyof typeof RoomType | null;
  description?: string | null;
  discountPercentage?: number | null;
  hotel?: Pick<IHotel, 'id'> | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
