import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.page.html',
  styleUrl: './home.page.scss',
})
export class HomePageComponent {
  readonly modules = [
    {
      title: 'Redis 排行榜',
      route: '/leaderboard',
      description: '学习 ZSet 录分、排序、查 TopN、查个人名次。',
      port: '8081',
    },
    {
      title: 'Lua 原子事务',
      route: '/lua-transaction',
      description: '学习 Lua 脚本如何把校验、扣减、入账和幂等放进一次执行。',
      port: '8082',
    },
    {
      title: 'Redis 秒杀',
      route: '/seckill',
      description: '学习库存扣减、一人一单、订单明细写入和活动快照回查。',
      port: '8084',
    },
  ];
}
