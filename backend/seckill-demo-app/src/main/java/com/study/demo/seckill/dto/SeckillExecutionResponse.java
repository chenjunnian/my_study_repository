package com.study.demo.seckill.dto;

/**
 * Status + order + latest snapshot is enough for the learning UI.
 */
public record SeckillExecutionResponse(
        String status,
        SeckillOrderResponse order,
        SeckillActivityResponse snapshot
) {
}
