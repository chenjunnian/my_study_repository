package com.study.demo.leaderboard.dto;

/**
 * A normalized leaderboard row keeps the frontend table simple.
 */
public record LeaderboardEntryResponse(
        String boardCode,
        String userId,
        String userName,
        long rank,
        double score
) {
}
