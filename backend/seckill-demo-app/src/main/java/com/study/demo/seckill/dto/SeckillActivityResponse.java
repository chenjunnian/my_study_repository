package com.study.demo.seckill.dto;

/**
 * Snapshot DTO used after preparation and after order placement.
 */
public record SeckillActivityResponse(
        String activityId,
        String productName,
        long initialStock,
        long remainingStock,
        long orderCount
) {
}
