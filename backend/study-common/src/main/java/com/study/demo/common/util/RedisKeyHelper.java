package com.study.demo.common.util;

/**
 * Centralized key naming makes the Redis structure easier to review.
 * When learning, being able to predict key names is very useful.
 */
public final class RedisKeyHelper {

    private RedisKeyHelper() {
    }

    public static String leaderboardScoreKey(String boardCode) {
        return "study:leaderboard:" + boardCode + ":scores";
    }

    public static String leaderboardProfileKey(String boardCode) {
        return "study:leaderboard:" + boardCode + ":profiles";
    }

    public static String walletBalanceKey() {
        return "study:wallet:balances";
    }

    public static String walletProcessedTxnKey() {
        return "study:wallet:processed-transactions";
    }

    public static String seckillActivityInfoKey(String activityId) {
        return "study:seckill:" + activityId + ":info";
    }

    public static String seckillStockKey(String activityId) {
        return "study:seckill:" + activityId + ":stock";
    }

    public static String seckillUsersKey(String activityId) {
        return "study:seckill:" + activityId + ":users";
    }

    public static String seckillOrdersKey(String activityId) {
        return "study:seckill:" + activityId + ":orders";
    }
}
