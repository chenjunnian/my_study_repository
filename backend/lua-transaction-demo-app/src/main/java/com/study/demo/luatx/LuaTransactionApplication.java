package com.study.demo.luatx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Independent entry point for learning Lua-based Redis transactions.
 */
@SpringBootApplication(scanBasePackages = "com.study.demo")
public class LuaTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuaTransactionApplication.class, args);
    }
}
