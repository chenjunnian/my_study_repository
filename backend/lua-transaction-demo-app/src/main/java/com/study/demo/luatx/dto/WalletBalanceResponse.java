package com.study.demo.luatx.dto;

/**
 * The frontend uses this DTO for both single-user and all-user displays.
 */
public record WalletBalanceResponse(
        String userId,
        String userName,
        long balance
) {
}
