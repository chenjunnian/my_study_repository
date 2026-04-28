import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import {
  ApiResponse,
  SeckillActivity,
  SeckillExecution,
  SeckillOrder,
} from '../models/api-response.model';

@Injectable({ providedIn: 'root' })
export class SeckillApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8084/api/seckill/activities';

  prepare(
    activityId: string,
    payload: { productName: string; stock: number },
  ): Observable<SeckillActivity> {
    return this.http
      .post<ApiResponse<SeckillActivity>>(`${this.baseUrl}/${activityId}/prepare`, payload)
      .pipe(map((response) => response.data));
  }

  placeOrder(
    activityId: string,
    payload: { userId: string; userName: string },
  ): Observable<SeckillExecution> {
    return this.http
      .post<ApiResponse<SeckillExecution>>(`${this.baseUrl}/${activityId}/orders`, payload)
      .pipe(map((response) => response.data));
  }

  snapshot(activityId: string): Observable<SeckillActivity> {
    return this.http
      .get<ApiResponse<SeckillActivity>>(`${this.baseUrl}/${activityId}`)
      .pipe(map((response) => response.data));
  }

  listOrders(activityId: string): Observable<SeckillOrder[]> {
    return this.http
      .get<ApiResponse<SeckillOrder[]>>(`${this.baseUrl}/${activityId}/orders`)
      .pipe(map((response) => response.data));
  }

  clear(activityId: string): Observable<void> {
    return this.http
      .delete<ApiResponse<null>>(`${this.baseUrl}/${activityId}`)
      .pipe(map(() => void 0));
  }
}
