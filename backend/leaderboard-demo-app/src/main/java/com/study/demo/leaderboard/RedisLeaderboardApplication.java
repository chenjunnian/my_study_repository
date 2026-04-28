package com.study.demo.leaderboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Independent Spring Boot entry point for leaderboard learning.
 * One feature, one application, one start command.
 */
@SpringBootApplication(scanBasePackages = "com.study.demo")
public class RedisLeaderboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisLeaderboardApplication.class, args);
    }
}
