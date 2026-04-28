# Redis Lua 原子事务学习记录

## 目标

用一个“钱包转账”场景演示 Lua 脚本在 Redis 中实现原子事务的方式。

## 为什么不用普通多条命令

如果转账流程拆成多条 Redis 命令：

- 先查余额
- 再扣减余额
- 再增加对方余额
- 再记录业务单号

那么并发下就会出现中间状态暴露的问题。Lua 的价值在于把这些动作放进一次脚本执行中。

## Redis Key 设计

- `study:wallet:balances`
  - 类型：`Hash`
  - 字段：`userId`
  - 值：余额
- `study:wallet:processed-transactions`
  - 类型：`Set`
  - 成员：`bizNo`
- `study:wallet:user-names`
  - 类型：`Hash`
  - 字段：`userId`
  - 值：用户名称

## Lua 脚本做了什么

1. 校验 `bizNo` 是否已经处理过
2. 读取转出方和转入方余额
3. 校验账户是否存在
4. 校验余额是否充足
5. 扣减转出方余额
6. 增加转入方余额
7. 记录 `bizNo`

## 接口

- `POST /api/wallets/users/balance`
- `POST /api/wallets/transfer`
- `GET /api/wallets`
- `GET /api/wallets/users/{userId}`
- `DELETE /api/wallets`

## 前端演示建议

1. 先初始化两个用户余额
2. 执行一次成功转账
3. 重复提交同一个 `bizNo`
4. 再测试余额不足场景
5. 对比讲解为什么 Lua 比“Java + 多次 Redis 调用”更安全

## 本次学习收获

- 明白了 Redis Lua 的原子执行本质
- 理解了幂等校验为什么要进脚本
- 学会了用最少的数据结构做出可讲解的事务示例
