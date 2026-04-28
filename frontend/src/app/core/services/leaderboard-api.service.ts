import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse, LeaderboardEntry, UserRanking } from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class LeaderboardApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8081/api/leaderboards';

  addScore(
    boardCode: string,
    payload: { userId: string; userName: string; scoreDelta: number },
  ): Observable<LeaderboardEntry> {
    return this.http
      .post<ApiResponse<LeaderboardEntry>>(`${this.baseUrl}/${boardCode}/scores`, payload)
      .pipe(map((response) => response.data));
  }

  getTop(boardCode: string, limit: number): Observable<LeaderboardEntry[]> {
    return this.http
      .get<ApiResponse<LeaderboardEntry[]>>(`${this.baseUrl}/${boardCode}/top`, {
        params: { limit },
      })
      .pipe(map((response) => response.data));
  }

  getUserRanking(boardCode: string, userId: string): Observable<UserRanking> {
    return this.http
      .get<ApiResponse<UserRanking>>(`${this.baseUrl}/${boardCode}/users/${userId}`)
      .pipe(map((response) => response.data));
  }

  clear(boardCode: string): Observable<void> {
    return this.http
      .delete<ApiResponse<null>>(`${this.baseUrl}/${boardCode}`)
      .pipe(map(() => void 0));
  }
}
