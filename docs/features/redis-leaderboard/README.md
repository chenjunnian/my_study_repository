# Redis 排行榜学习记录

## 目标

用 Redis `ZSet` 实现一个最小可运行的排行榜能力，支持：

- 录入用户分数
- 查询 TopN
- 查询某个用户当前名次
- 清空榜单，方便重复演示

## 为什么选 ZSet

- `score` 天然就是排序分值
- Redis 内部已经维护好有序结构
- `ZINCRBY` 适合积分累计
- `ZREVRANK` 适合查名次
- `ZREVRANGE` 适合查 TopN

## Redis Key 设计

- `study:leaderboard:{boardCode}:scores`
  - 类型：`ZSet`
  - 成员：`userId`
  - 分值：`score`
- `study:leaderboard:{boardCode}:profiles`
  - 类型：`Hash`
  - 字段：`userId`
  - 值：`userName`

## 关键实现过程

1. 后端收到加分请求后，先把用户名写入 `Hash`
2. 然后对 `ZSet` 执行 `ZINCRBY`
3. 再调用 `ZREVRANK` 获取当前名次
4. 查询榜单时，直接对 `ZSet` 倒序遍历

## 接口

- `POST /api/leaderboards/{boardCode}/scores`
- `GET /api/leaderboards/{boardCode}/top?limit=10`
- `GET /api/leaderboards/{boardCode}/users/{userId}`
- `DELETE /api/leaderboards/{boardCode}`

## 前端演示建议

1. 先给同一个榜单录入 3 到 5 个用户的分数
2. 观察 Top 榜单变化
3. 单独查某个用户名次
4. 说明为什么榜单适合用 `ZSet` 而不是 `List`

## 本次学习收获

- 理解了 Redis 排行榜的经典结构
- 了解了“展示名”和“排序值”分离存储的思路
- 为后续扩展实时榜单、周榜、日榜打下基础
