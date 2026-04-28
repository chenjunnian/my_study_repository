package com.study.demo.seckill.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.demo.common.util.RedisKeyHelper;
import com.study.demo.seckill.dto.PrepareActivityRequest;
import com.study.demo.seckill.dto.SeckillActivityResponse;
import com.study.demo.seckill.dto.SeckillExecutionResponse;
import com.study.demo.seckill.dto.SeckillOrderResponse;
import com.study.demo.seckill.dto.SeckillPlaceOrderRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

/**
 * This service demonstrates the classic seckill core:
 * check stock, prevent duplicate order, and create order atomically.
 */
@Service
public class SeckillService {

    private static final DefaultRedisScript<Long> PLACE_ORDER_SCRIPT = new DefaultRedisScript<>(
            """
            if redis.call('GET', KEYS[1]) == false then
                return 3
            end

            if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
                return 2
            end

            local stock = tonumber(redis.call('GET', KEYS[1]) or '0')
            if stock <= 0 then
                return 0
            end

            redis.call('DECR', KEYS[1])
            redis.call('SADD', KEYS[2], ARGV[1])
            redis.call('HSET', KEYS[3], ARGV[2], ARGV[3])
            redis.call('HINCRBY', KEYS[4], 'orderCount', 1)
            return 1
            """,
            Long.class
    );

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public SeckillService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public SeckillActivityResponse prepareActivity(String activityId, PrepareActivityRequest request) {
        String infoKey = RedisKeyHelper.seckillActivityInfoKey(activityId);
        String stockKey = RedisKeyHelper.seckillStockKey(activityId);
        String userKey = RedisKeyHelper.seckillUsersKey(activityId);
        String orderKey = RedisKeyHelper.seckillOrdersKey(activityId);

        // Delete old data first so each demo starts from a clean and predictable state.
        redisTemplate.delete(List.of(infoKey, stockKey, userKey, orderKey));

        Map<String, String> info = new HashMap<>();
        info.put("productName", request.productName());
        info.put("initialStock", String.valueOf(request.stock()));
        info.put("orderCount", "0");
        info.put("preparedAt", Instant.now().toString());

        redisTemplate.opsForHash().putAll(infoKey, info);
        redisTemplate.opsForValue().set(stockKey, String.valueOf(request.stock()));

        return snapshot(activityId);
    }

    public SeckillExecutionResponse placeOrder(String activityId, SeckillPlaceOrderRequest request) {
        SeckillOrderResponse order = new SeckillOrderResponse(
                activityId + "-" + request.userId() + "-" + System.currentTimeMillis(),
                activityId,
                request.userId(),
                request.userName(),
                Instant.now().toString()
        );

        Long result = redisTemplate.execute(
                PLACE_ORDER_SCRIPT,
                List.of(
                        RedisKeyHelper.seckillStockKey(activityId),
                        RedisKeyHelper.seckillUsersKey(activityId),
                        RedisKeyHelper.seckillOrdersKey(activityId),
                        RedisKeyHelper.seckillActivityInfoKey(activityId)
                ),
                request.userId(),
                order.orderId(),
                toJson(order)
        );

        long resultCode = result == null ? -1L : result;

        String status;
        if (resultCode == 1L) {
            status = "SUCCESS";
        } else if (resultCode == 0L) {
            status = "OUT_OF_STOCK";
        } else if (resultCode == 2L) {
            status = "DUPLICATE_ORDER";
        } else if (resultCode == 3L) {
            status = "ACTIVITY_NOT_FOUND";
        } else {
            status = "UNKNOWN_ERROR";
        }

        return new SeckillExecutionResponse(
                status,
                "SUCCESS".equals(status) ? order : null,
                snapshot(activityId)
        );
    }

    public SeckillActivityResponse snapshot(String activityId) {
        Map<Object, Object> info = redisTemplate.opsForHash().entries(RedisKeyHelper.seckillActivityInfoKey(activityId));
        String productName = String.valueOf(info.getOrDefault("productName", "Not Prepared"));
        long initialStock = parseLong(info.get("initialStock"));
        long orderCount = parseLong(info.get("orderCount"));
        long remainingStock = parseLong(redisTemplate.opsForValue().get(RedisKeyHelper.seckillStockKey(activityId)));

        return new SeckillActivityResponse(activityId, productName, initialStock, remainingStock, orderCount);
    }

    public List<SeckillOrderResponse> listOrders(String activityId) {
        List<Object> rawOrders = redisTemplate.opsForHash().values(RedisKeyHelper.seckillOrdersKey(activityId));
        List<SeckillOrderResponse> orders = new ArrayList<>();

        for (Object rawOrder : rawOrders) {
            try {
                orders.add(objectMapper.readValue(String.valueOf(rawOrder), SeckillOrderResponse.class));
            } catch (JsonProcessingException exception) {
                throw new IllegalStateException("failed to read seckill order from redis", exception);
            }
        }

        orders.sort(Comparator.comparing(SeckillOrderResponse::createdAt));
        return orders;
    }

    public void clearActivity(String activityId) {
        redisTemplate.delete(List.of(
                RedisKeyHelper.seckillActivityInfoKey(activityId),
                RedisKeyHelper.seckillStockKey(activityId),
                RedisKeyHelper.seckillUsersKey(activityId),
                RedisKeyHelper.seckillOrdersKey(activityId)
        ));
    }

    private String toJson(SeckillOrderResponse order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("failed to serialize seckill order", exception);
        }
    }

    private long parseLong(Object value) {
        if (value == null) {
            return 0L;
        }
        return Long.parseLong(String.valueOf(value));
    }
}
