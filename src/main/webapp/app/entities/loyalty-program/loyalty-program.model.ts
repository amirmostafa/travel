export interface ILoyaltyProgram {
  id: number;
  name?: string | null;
  description?: string | null;
  pointsPerDollar?: number | null;
  rewardThreshold?: number | null;
}

export type NewLoyaltyProgram = Omit<ILoyaltyProgram, 'id'> & { id: null };
