package com.study.demo.luatx.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * bizNo is required to demonstrate idempotency in the Lua script.
 */
public record WalletTransferRequest(
        @NotBlank(message = "fromUserId cannot be blank")
        String fromUserId,
        @NotBlank(message = "toUserId cannot be blank")
        String toUserId,
        @Min(value = 1, message = "amount must be greater than 0")
        long amount,
        @NotBlank(message = "bizNo cannot be blank")
        String bizNo
) {
}
