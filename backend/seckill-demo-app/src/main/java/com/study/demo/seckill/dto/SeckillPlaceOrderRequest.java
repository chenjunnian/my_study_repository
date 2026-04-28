package com.study.demo.seckill.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * One user can place at most one order in the current demo.
 */
public record SeckillPlaceOrderRequest(
        @NotBlank(message = "userId cannot be blank")
        String userId,
        @NotBlank(message = "userName cannot be blank")
        String userName
) {
}
