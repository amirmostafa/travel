import { IRoom } from 'app/entities/room/room.model';

export interface IImage {
  id: number;
  url?: string | null;
  description?: string | null;
  room?: Pick<IRoom, 'id'> | null;
}

export type NewImage = Omit<IImage, 'id'> & { id: null };
