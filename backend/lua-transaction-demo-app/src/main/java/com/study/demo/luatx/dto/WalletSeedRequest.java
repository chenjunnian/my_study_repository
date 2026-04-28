package com.study.demo.luatx.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Seed operation is intentionally explicit so learners can reset and replay demos.
 */
public record WalletSeedRequest(
        @NotBlank(message = "userId cannot be blank")
        String userId,
        @NotBlank(message = "userName cannot be blank")
        String userName,
        @Min(value = 0, message = "balance must be greater than or equal to 0")
        long balance
) {
}
