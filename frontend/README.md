# Frontend Study Lab

这个前端是为了给后端学习模块提供一个“可演示、可讲解、可回放”的实验台，而不是单纯做一个页面壳子。

## 当前页面

- `/`
  - 学习仓库总览
- `/leaderboard`
  - Redis 排行榜演示
- `/lua-transaction`
  - Redis Lua 原子事务演示
- `/seckill`
  - Redis 秒杀演示

## 启动方式

```bash
cd frontend
npm install
npm start
```

默认地址：

- `http://localhost:4200`

## 对应后端地址

- 排行榜：`http://localhost:8081`
- Lua 事务：`http://localhost:8082`
- 秒杀：`http://localhost:8084`

## 对应 Redis 地址

- `localhost:6380`

## 页面设计原则

- 每个页面都要有操作面板
- 每个页面都要有结果展示区
- 每个页面都要带讲解提纲，方便演示时组织语言
- 页面之间彼此隔离，便于后面持续增加新模块
