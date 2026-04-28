import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { LeaderboardEntry, UserRanking } from '../../core/models/api-response.model';
import { LeaderboardApiService } from '../../core/services/leaderboard-api.service';

@Component({
  selector: 'app-leaderboard-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './leaderboard.page.html',
  styleUrl: './leaderboard.page.scss',
})
export class LeaderboardPageComponent {
  private readonly leaderboardApi = inject(LeaderboardApiService);

  readonly entries = signal<LeaderboardEntry[]>([]);
  readonly ranking = signal<UserRanking | null>(null);
  readonly status = signal('点击“加载 Top 榜单”开始演示。');
  readonly statusIsWarn = signal(false);

  boardCode = 'spring-study';
  topLimit = 5;
  queryUserId = 'u1001';
  scoreForm = {
    userId: 'u1001',
    userName: 'Alice',
    scoreDelta: 10,
  };

  async addScore(): Promise<void> {
    try {
      const row = await firstValueFrom(
        this.leaderboardApi.addScore(this.boardCode, this.scoreForm),
      );
      this.status.set(`录分成功：${row.userName} 当前第 ${row.rank} 名，分数 ${row.score}`);
      this.statusIsWarn.set(false);
      await this.refreshTop();
      await this.lookupUser();
    } catch (error) {
      this.setError(error);
    }
  }

  async refreshTop(): Promise<void> {
    try {
      const data = await firstValueFrom(this.leaderboardApi.getTop(this.boardCode, this.topLimit));
      this.entries.set(data);
      this.status.set(`已加载 ${data.length} 条榜单数据。`);
      this.statusIsWarn.set(false);
    } catch (error) {
      this.setError(error);
    }
  }

  async lookupUser(): Promise<void> {
    try {
      const data = await firstValueFrom(
        this.leaderboardApi.getUserRanking(this.boardCode, this.queryUserId),
      );
      this.ranking.set(data);
      this.status.set(`已查询用户 ${this.queryUserId} 的名次。`);
      this.statusIsWarn.set(false);
    } catch (error) {
      this.setError(error);
    }
  }

  async resetBoard(): Promise<void> {
    try {
      await firstValueFrom(this.leaderboardApi.clear(this.boardCode));
      this.entries.set([]);
      this.ranking.set(null);
      this.status.set(`榜单 ${this.boardCode} 已清空。`);
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
