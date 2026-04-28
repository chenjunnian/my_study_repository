# Redis 秒杀学习记录

## 目标

实现一个适合学习的秒杀最小版本，重点讲清楚：

- 库存扣减
- 一人一单
- Lua 原子执行
- 活动预热和结果回查

## Redis Key 设计

- `study:seckill:{activityId}:info`
  - 类型：`Hash`
  - 内容：商品名、初始库存、订单数、预热时间
- `study:seckill:{activityId}:stock`
  - 类型：`String`
  - 内容：剩余库存
- `study:seckill:{activityId}:users`
  - 类型：`Set`
  - 内容：已下单用户 ID
- `study:seckill:{activityId}:orders`
  - 类型：`Hash`
  - 内容：订单 ID -> 订单 JSON

## Lua 脚本做了什么

1. 校验活动是否存在
2. 校验用户是否已下单
3. 校验库存是否大于 0
4. 扣减库存
5. 记录用户下单状态
6. 写入订单明细
7. 订单数加 1

## 接口

- `POST /api/seckill/activities/{activityId}/prepare`
- `POST /api/seckill/activities/{activityId}/orders`
- `GET /api/seckill/activities/{activityId}`
- `GET /api/seckill/activities/{activityId}/orders`
- `DELETE /api/seckill/activities/{activityId}`

## 前端演示建议

1. 先预热一个库存为 3 的活动
2. 用 3 个不同用户依次下单
3. 再让第 4 个用户下单，观察库存不足
4. 再让已下单用户重复下单，观察一人一单拦截
5. 最后讲生产环境里为什么还要接数据库和消息队列

## 当前演示地址

- 秒杀服务：`http://localhost:8084`

## 当前版本边界

- 订单只落 Redis，没有持久化数据库
- 没有引入异步削峰队列
- 没有做分布式锁和流量控制

## 下一步可迭代方向

- 接入 MySQL 保存订单
- 接入消息队列异步落单
- 加入限流和令牌桶
- 为活动增加开始时间和结束时间
