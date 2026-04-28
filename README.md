# Spring Boot + Angular Study Repository

这是一个面向 `Spring Boot` 学习和功能积累的单仓库项目，目标不是一次性做成完整业务系统，而是把常见能力拆成可以单独启动、单独讲解、单独复盘的小模块。

## 仓库目标

- 后端使用 `Spring Boot`，按学习主题拆成多个独立启动类。
- 前端使用 `Angular`，按学习主题拆成多个独立演示页面。
- 每完成一个功能，都在仓库中补一份 `README`，记录实现过程、关键命令和学习重点。
- 使用增量式开发方式，在根目录维护版本演进文件。
- 代码尽量写清楚，保留足够注释，便于以后回看。

## 当前版本

当前实现见 [VERSION.md](./VERSION.md)。

## 当前学习模块

### 后端模块

- `backend/leaderboard-demo-app`
  - Redis `ZSet` 排行榜示例
- `backend/lua-transaction-demo-app`
  - Redis `Lua` 原子事务示例
- `backend/seckill-demo-app`
  - Redis + Lua 秒杀示例

### 前端模块

- `frontend`
  - Angular 学习站点
  - 包含排行榜、Lua 事务、秒杀三个演示页面
  - 每个页面都带有接口演示区和讲解提纲

## 目录结构

```text
.
├── backend                     # Spring Boot 多模块工程
├── docs
│   └── features                # 每个功能单独的学习记录
├── frontend                    # Angular 演示工程
├── docker-compose.yml          # 本地 Redis 运行环境
└── VERSION.md                  # 增量式开发版本记录
```

## 快速开始

### 1. 启动 Redis

```bash
docker compose up -d redis
```

默认会把仓库内 Redis 映射到：

- `localhost:6380`

### 2. 启动后端模块

需要先设置 `JAVA_HOME`，然后按模块单独启动：

```bash
export JAVA_HOME=/usr/lib/jvm/jdk-17.0.14-oracle-x64
cd backend

# 排行榜
mvn -pl leaderboard-demo-app spring-boot:run

# Lua 原子事务
mvn -pl lua-transaction-demo-app spring-boot:run

# 秒杀
mvn -pl seckill-demo-app spring-boot:run
```

### 3. 启动前端

```bash
cd frontend
npm install
npm start
```

默认访问地址：

- `http://localhost:4200`
- `http://localhost:8081`
- `http://localhost:8082`
- `http://localhost:8084`

## 学习记录

- 排行榜实现说明：[docs/features/redis-leaderboard/README.md](./docs/features/redis-leaderboard/README.md)
- Lua 原子事务说明：[docs/features/lua-wallet-transaction/README.md](./docs/features/lua-wallet-transaction/README.md)
- 秒杀实现说明：[docs/features/redis-seckill/README.md](./docs/features/redis-seckill/README.md)
