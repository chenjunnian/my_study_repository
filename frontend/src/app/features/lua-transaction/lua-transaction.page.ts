import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { WalletBalance } from '../../core/models/api-response.model';
import { WalletApiService } from '../../core/services/wallet-api.service';

@Component({
  selector: 'app-lua-transaction-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lua-transaction.page.html',
  styleUrl: './lua-transaction.page.scss',
})
export class LuaTransactionPageComponent {
  private readonly walletApi = inject(WalletApiService);

  readonly balances = signal<WalletBalance[]>([]);
  readonly status = signal('先为两个用户注入余额，再执行 Lua 转账。');
  readonly statusIsWarn = signal(false);

  seedForm = {
    userId: 'u1001',
    userName: 'Alice',
    balance: 100,
  };

  transferForm = {
    fromUserId: 'u1001',
    toUserId: 'u1002',
    amount: 25,
    bizNo: `biz-${Date.now()}`,
  };

  async seedBalance(): Promise<void> {
    try {
      const data = await firstValueFrom(this.walletApi.seedBalance(this.seedForm));
      this.status.set(`余额注入成功：${data.userName} = ${data.balance}`);
      this.statusIsWarn.set(false);
      await this.loadBalances();
    } catch (error) {
      this.setError(error);
    }
  }

  async transfer(): Promise<void> {
    try {
      const data = await firstValueFrom(this.walletApi.transfer(this.transferForm));
      this.balances.set(data.balances);
      this.status.set(`Lua 执行结果：${data.status}，业务单号 ${data.bizNo}`);
      this.statusIsWarn.set(data.status !== 'SUCCESS');
      this.transferForm.bizNo = `biz-${Date.now()}`;
    } catch (error) {
      this.setError(error);
    }
  }

  async loadBalances(): Promise<void> {
    try {
      const data = await firstValueFrom(this.walletApi.listBalances());
      this.balances.set(data);
      this.status.set(`已加载 ${data.length} 个钱包。`);
      this.statusIsWarn.set(false);
    } catch (error) {
      this.setError(error);
    }
  }

  async resetWallet(): Promise<void> {
    try {
      await firstValueFrom(this.walletApi.clear());
      this.balances.set([]);
      this.status.set('钱包数据已清空。');
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
