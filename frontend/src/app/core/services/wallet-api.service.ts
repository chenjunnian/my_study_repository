import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse, WalletBalance, WalletTransferResult } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class WalletApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8082/api/wallets';

  seedBalance(payload: {
    userId: string;
    userName: string;
    balance: number;
  }): Observable<WalletBalance> {
    return this.http
      .post<ApiResponse<WalletBalance>>(`${this.baseUrl}/users/balance`, payload)
      .pipe(map((response) => response.data));
  }

  transfer(payload: {
    fromUserId: string;
    toUserId: string;
    amount: number;
    bizNo: string;
  }): Observable<WalletTransferResult> {
    return this.http
      .post<ApiResponse<WalletTransferResult>>(`${this.baseUrl}/transfer`, payload)
      .pipe(map((response) => response.data));
  }

  listBalances(): Observable<WalletBalance[]> {
    return this.http
      .get<ApiResponse<WalletBalance[]>>(this.baseUrl)
      .pipe(map((response) => response.data));
  }

  clear(): Observable<void> {
    return this.http.delete<ApiResponse<null>>(this.baseUrl).pipe(map(() => void 0));
  }
}
