package com.study.demo.leaderboard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * The request models a score change instead of an absolute value.
 * This better reflects common game or activity ranking scenarios.
 */
public record ScoreUpdateRequest(
        @NotBlank(message = "userId cannot be blank")
        String userId,
        @NotBlank(message = "userName cannot be blank")
        String userName,
        @Min(value = 1, message = "scoreDelta must be greater than 0")
        long scoreDelta
) {
}
