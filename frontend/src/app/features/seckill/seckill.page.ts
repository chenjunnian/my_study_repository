import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { SeckillActivity, SeckillOrder } from '../../core/models/api-response.model';
import { SeckillApiService } from '../../core/services/seckill-api.service';

@Component({
  selector: 'app-seckill-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './seckill.page.html',
  styleUrl: './seckill.page.scss',
})
export class SeckillPageComponent {
  private readonly seckillApi = inject(SeckillApiService);

  readonly snapshot = signal<SeckillActivity | null>(null);
  readonly orders = signal<SeckillOrder[]>([]);
  readonly status = signal('先预热一个秒杀活动，再模拟不同用户下单。');
  readonly statusIsWarn = signal(false);

  activityId = 'activity-1001';
  prepareForm = {
    productName: 'Spring Boot Learning Voucher',
    stock: 3,
  };

  orderForm = {
    userId: 'u1001',
    userName: 'Alice',
  };

  async prepare(): Promise<void> {
    try {
      const data = await firstValueFrom(this.seckillApi.prepare(this.activityId, this.prepareForm));
      this.snapshot.set(data);
      this.orders.set([]);
      this.status.set(`活动已预热：库存 ${data.initialStock}，剩余 ${data.remainingStock}`);
      this.statusIsWarn.set(false);
    } catch (error) {
      this.setError(error);
    }
  }

  async placeOrder(): Promise<void> {
    try {
      const data = await firstValueFrom(this.seckillApi.placeOrder(this.activityId, this.orderForm));
      this.snapshot.set(data.snapshot);
      this.status.set(`秒杀结果：${data.status}`);
      this.statusIsWarn.set(data.status !== 'SUCCESS');
      await this.loadOrders();
    } catch (error) {
      this.setError(error);
    }
  }

  async loadSnapshot(): Promise<void> {
    try {
      const data = await firstValueFrom(this.seckillApi.snapshot(this.activityId));
      this.snapshot.set(data);
      this.status.set('已刷新活动快照。');
      this.statusIsWarn.set(false);
    } catch (error) {
      this.setError(error);
    }
  }

  async loadOrders(): Promise<void> {
    try {
      const data = await firstValueFrom(this.seckillApi.listOrders(this.activityId));
      this.orders.set(data);
    } catch (error) {
      this.setError(error);
    }
  }

  async clearActivity(): Promise<void> {
    try {
      await firstValueFrom(this.seckillApi.clear(this.activityId));
      this.snapshot.set(null);
      this.orders.set([]);
      this.status.set(`活动 ${this.activityId} 已清空。`);
      this.statusIsWarn.set(false);
    } catch (error) {
      this.setError(error);
    }
  }

  private setError(error: unknown): void {
    const message = error instanceof Error ? error.message : '请求失败，请检查后端和 Redis 是否已启动。';
    this.status.set(message);
    this.statusIsWarn.set(true);
  }
}
