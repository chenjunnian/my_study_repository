# Backend Modules

这个目录用于放置所有 Spring Boot 学习模块。

## 设计原则

- 每个学习主题都尽量拆成单独的启动类
- 每个模块只关注一个核心知识点
- 公共能力抽到 `study-common`
- 方便以后继续新增缓存、消息队列、数据库、分布式锁等主题

## 模块列表

- `study-common`
  - 公共返回体、CORS 配置、Redis 键名工具
- `leaderboard-demo-app`
  - 演示 Redis ZSet 排行榜
- `lua-transaction-demo-app`
  - 演示 Redis Lua 脚本实现原子事务
- `seckill-demo-app`
  - 演示 Redis Lua 秒杀扣减库存与一人一单

## 运行说明

```bash
export JAVA_HOME=/usr/lib/jvm/jdk-17.0.14-oracle-x64
cd backend

mvn -pl leaderboard-demo-app spring-boot:run
mvn -pl lua-transaction-demo-app spring-boot:run
mvn -pl seckill-demo-app spring-boot:run
```
