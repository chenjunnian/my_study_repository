package com.study.demo.luatx.controller;

import com.study.demo.common.api.ApiResponse;
import com.study.demo.luatx.dto.WalletBalanceResponse;
import com.study.demo.luatx.dto.WalletSeedRequest;
import com.study.demo.luatx.dto.WalletTransferRequest;
import com.study.demo.luatx.dto.WalletTransferResponse;
import com.study.demo.luatx.service.WalletService;
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
 * The API is intentionally minimal: seed, query, transfer, reset.
 */
@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/users/balance")
    public ApiResponse<WalletBalanceResponse> seedBalance(@Valid @RequestBody WalletSeedRequest request) {
        return ApiResponse.success("wallet seeded", walletService.seedBalance(request));
    }

    @PostMapping("/transfer")
    public ApiResponse<WalletTransferResponse> transfer(@Valid @RequestBody WalletTransferRequest request) {
        return ApiResponse.success("transfer executed", walletService.transfer(request));
    }

    @GetMapping
    public ApiResponse<List<WalletBalanceResponse>> listBalances() {
        return ApiResponse.success(walletService.listBalances());
    }

    @GetMapping("/users/{userId}")
    public ApiResponse<WalletBalanceResponse> getBalance(@PathVariable String userId) {
        return ApiResponse.success(walletService.getBalance(userId));
    }

    @DeleteMapping
    public ApiResponse<Void> clearAll() {
        walletService.clearAll();
        return ApiResponse.success("wallet data cleared", null);
    }
}
