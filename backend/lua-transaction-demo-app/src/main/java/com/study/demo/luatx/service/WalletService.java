package com.study.demo.luatx.service;

import com.study.demo.common.util.RedisKeyHelper;
import com.study.demo.luatx.dto.WalletBalanceResponse;
import com.study.demo.luatx.dto.WalletSeedRequest;
import com.study.demo.luatx.dto.WalletTransferRequest;
import com.study.demo.luatx.dto.WalletTransferResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

/**
 * This module focuses on why Lua is useful:
 * multiple checks and updates run atomically inside Redis.
 */
@Service
public class WalletService {

    private static final DefaultRedisScript<Long> TRANSFER_SCRIPT = new DefaultRedisScript<>(
            """
            if redis.call('SISMEMBER', KEYS[2], ARGV[4]) == 1 then
                return 2
            end

            local fromBalance = tonumber(redis.call('HGET', KEYS[1], ARGV[1]) or '-1')
            local toBalance = tonumber(redis.call('HGET', KEYS[1], ARGV[2]) or '-1')
            local amount = tonumber(ARGV[3])

            if fromBalance < 0 or toBalance < 0 then
                return 3
            end

            if fromBalance < amount then
                return 0
            end

            redis.call('HINCRBY', KEYS[1], ARGV[1], -amount)
            redis.call('HINCRBY', KEYS[1], ARGV[2], amount)
            redis.call('SADD', KEYS[2], ARGV[4])
            return 1
            """,
            Long.class
    );

    private final StringRedisTemplate redisTemplate;

    public WalletService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public WalletBalanceResponse seedBalance(WalletSeedRequest request) {
        String balanceKey = RedisKeyHelper.walletBalanceKey();
        String userNameKey = userNameKey();

        // Names and balances are split so the Redis data structure remains explicit.
        redisTemplate.opsForHash().put(userNameKey, request.userId(), request.userName());
        redisTemplate.opsForHash().put(balanceKey, request.userId(), String.valueOf(request.balance()));

        return new WalletBalanceResponse(request.userId(), request.userName(), request.balance());
    }

    public WalletTransferResponse transfer(WalletTransferRequest request) {
        Long result = redisTemplate.execute(
                TRANSFER_SCRIPT,
                List.of(RedisKeyHelper.walletBalanceKey(), RedisKeyHelper.walletProcessedTxnKey()),
                request.fromUserId(),
                request.toUserId(),
                String.valueOf(request.amount()),
                request.bizNo()
        );

        long resultCode = result == null ? -1L : result;

        String status;
        if (resultCode == 1L) {
            status = "SUCCESS";
        } else if (resultCode == 0L) {
            status = "INSUFFICIENT_BALANCE";
        } else if (resultCode == 2L) {
            status = "DUPLICATE_BIZ_NO";
        } else if (resultCode == 3L) {
            status = "USER_NOT_FOUND";
        } else {
            status = "UNKNOWN_ERROR";
        }

        return new WalletTransferResponse(status, request.bizNo(), listBalances());
    }

    public List<WalletBalanceResponse> listBalances() {
        Map<Object, Object> balances = redisTemplate.opsForHash().entries(RedisKeyHelper.walletBalanceKey());
        Map<Object, Object> names = redisTemplate.opsForHash().entries(userNameKey());

        List<WalletBalanceResponse> results = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : balances.entrySet()) {
            String userId = String.valueOf(entry.getKey());
            String userName = String.valueOf(names.getOrDefault(userId, "Unknown"));
            long balance = Long.parseLong(String.valueOf(entry.getValue()));
            results.add(new WalletBalanceResponse(userId, userName, balance));
        }

        results.sort(Comparator.comparing(WalletBalanceResponse::userId));
        return results;
    }

    public WalletBalanceResponse getBalance(String userId) {
        Object balance = redisTemplate.opsForHash().get(RedisKeyHelper.walletBalanceKey(), userId);
        Object userName = redisTemplate.opsForHash().get(userNameKey(), userId);

        if (balance == null) {
            return new WalletBalanceResponse(userId, "Unknown", 0);
        }

        return new WalletBalanceResponse(
                userId,
                userName == null ? "Unknown" : String.valueOf(userName),
                Long.parseLong(String.valueOf(balance))
        );
    }

    public void clearAll() {
        redisTemplate.delete(List.of(
                RedisKeyHelper.walletBalanceKey(),
                RedisKeyHelper.walletProcessedTxnKey(),
                userNameKey()
        ));
    }

    private String userNameKey() {
        return "study:wallet:user-names";
    }
}
