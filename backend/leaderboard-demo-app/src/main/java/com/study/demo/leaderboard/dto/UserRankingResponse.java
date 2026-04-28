package com.study.demo.leaderboard.dto;

/**
 * When the user has not scored yet, rank and score can be null.
 */
public record UserRankingResponse(
        String boardCode,
        String userId,
        String userName,
        Long rank,
        Double score
) {
}
