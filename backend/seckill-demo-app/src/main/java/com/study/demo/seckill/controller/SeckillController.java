package com.study.demo.seckill.controller;

import com.study.demo.common.api.ApiResponse;
import com.study.demo.seckill.dto.PrepareActivityRequest;
import com.study.demo.seckill.dto.SeckillActivityResponse;
import com.study.demo.seckill.dto.SeckillExecutionResponse;
import com.study.demo.seckill.dto.SeckillOrderResponse;
import com.study.demo.seckill.dto.SeckillPlaceOrderRequest;
import com.study.demo.seckill.service.SeckillService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller exposes a replay-friendly API for the Angular demo.
 */
@RestController
@RequestMapping("/api/seckill/activities")
public class SeckillController {

    private final SeckillService seckillService;

    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @PostMapping("/{activityId}/prepare")
    public ApiResponse<SeckillActivityResponse> prepare(
            @PathVariable String activityId,
            @Valid @RequestBody PrepareActivityRequest request
    ) {
        return ApiResponse.success("activity prepared", seckillService.prepareActivity(activityId, request));
    }

    @PostMapping("/{activityId}/orders")
    public ApiResponse<SeckillExecutionResponse> placeOrder(
            @PathVariable String activityId,
            @Valid @RequestBody SeckillPlaceOrderRequest request
    ) {
        return ApiResponse.success("seckill order executed", seckillService.placeOrder(activityId, request));
    }

    @GetMapping("/{activityId}")
    public ApiResponse<SeckillActivityResponse> snapshot(@PathVariable String activityId) {
        return ApiResponse.success(seckillService.snapshot(activityId));
    }

    @GetMapping("/{activityId}/orders")
    public ApiResponse<List<SeckillOrderResponse>> listOrders(@PathVariable String activityId) {
        return ApiResponse.success(seckillService.listOrders(activityId));
    }

    @DeleteMapping("/{activityId}")
    public ApiResponse<Void> clearActivity(@PathVariable String activityId) {
        seckillService.clearActivity(activityId);
        return ApiResponse.success("activity cleared", null);
    }
}
