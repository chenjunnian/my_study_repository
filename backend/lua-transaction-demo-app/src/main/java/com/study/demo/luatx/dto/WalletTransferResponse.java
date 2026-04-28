package com.study.demo.luatx.dto;

import java.util.List;

/**
 * Returning updated balances makes the Lua execution easier to verify visually.
 */
public record WalletTransferResponse(
        String status,
        String bizNo,
        List<WalletBalanceResponse> balances
) {
}
