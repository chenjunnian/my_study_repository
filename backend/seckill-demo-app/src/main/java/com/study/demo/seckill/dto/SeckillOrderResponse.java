package com.study.demo.seckill.dto;

/**
 * Order details stay in Redis in this starter version.
 */
public record SeckillOrderResponse(
        String orderId,
        String activityId,
        String userId,
        String userName,
        String createdAt
) {
}
