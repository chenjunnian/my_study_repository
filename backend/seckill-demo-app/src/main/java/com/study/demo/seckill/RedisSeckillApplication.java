package com.study.demo.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Independent entry point for the seckill learning module.
 */
@SpringBootApplication(scanBasePackages = "com.study.demo")
public class RedisSeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisSeckillApplication.class, args);
    }
}
