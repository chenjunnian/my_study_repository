export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface LeaderboardEntry {
  boardCode: string;
  userId: string;
  userName: string;
  rank: number;
  score: number;
}

export interface UserRanking {
  boardCode: string;
  userId: string;
  userName: string;
  rank: number | null;
  score: number | null;
}

export interface WalletBalance {
  userId: string;
  userName: string;
  balance: number;
}

export interface WalletTransferResult {
  status: string;
  bizNo: string;
  balances: WalletBalance[];
}

export interface SeckillActivity {
  activityId: string;
  productName: string;
  initialStock: number;
  remainingStock: number;
  orderCount: number;
}

export interface SeckillOrder {
  orderId: string;
  activityId: string;
  userId: string;
  userName: string;
  createdAt: string;
}

export interface SeckillExecution {
  status: string;
  order: SeckillOrder | null;
  snapshot: SeckillActivity;
}
