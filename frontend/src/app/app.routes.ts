import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/home/home.page').then((module) => module.HomePageComponent),
  },
  {
    path: 'leaderboard',
    loadComponent: () =>
      import('./features/leaderboard/leaderboard.page').then(
        (module) => module.LeaderboardPageComponent,
      ),
  },
  {
    path: 'lua-transaction',
    loadComponent: () =>
      import('./features/lua-transaction/lua-transaction.page').then(
        (module) => module.LuaTransactionPageComponent,
      ),
  },
  {
    path: 'seckill',
    loadComponent: () =>
      import('./features/seckill/seckill.page').then((module) => module.SeckillPageComponent),
  },
  {
    path: '**',
    redirectTo: '',
  },
];
