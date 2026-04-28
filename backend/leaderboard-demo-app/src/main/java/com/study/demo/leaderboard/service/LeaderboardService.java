package com.study.demo.leaderboard.service;

import com.study.demo.common.util.RedisKeyHelper;
import com.study.demo.leaderboard.dto.LeaderboardEntryResponse;
import com.study.demo.leaderboard.dto.ScoreUpdateRequest;
import com.study.demo.leaderboard.dto.UserRankingResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

/**
 * This service intentionally keeps every Redis operation visible.
 * For learning, explicit code is more valuable than over-abstraction.
 */
@Service
public class LeaderboardService {

    private final StringRedisTemplate redisTemplate;

    public LeaderboardService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public LeaderboardEntryResponse addScore(String boardCode, ScoreUpdateRequest request) {
        String scoreKey = RedisKeyHelper.leaderboardScoreKey(boardCode);
        String profileKey = RedisKeyHelper.leaderboardProfileKey(boardCode);

        // Save the user's display name so the leaderboard can show readable text.
        redisTemplate.opsForHash().put(profileKey, request.userId(), request.userName());

        // ZINCRBY is the core leaderboard operation: add score and keep sorting.
        Double latestScore = redisTemplate.opsForZSet()
                .incrementScore(scoreKey, request.userId(), request.scoreDelta());

        Long rank = redisTemplate.opsForZSet().reverseRank(scoreKey, request.userId());

        return new LeaderboardEntryResponse(
                boardCode,
                request.userId(),
                request.userName(),
                rank == null ? 0L : rank + 1,
                latestScore == null ? 0D : latestScore
        );
    }

    public List<LeaderboardEntryResponse> getTopN(String boardCode, int limit) {
        String scoreKey = RedisKeyHelper.leaderboardScoreKey(boardCode);
        String profileKey = RedisKeyHelper.leaderboardProfileKey(boardCode);

        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(scoreKey, 0, Math.max(limit - 1L, 0L));

        if (tuples == null || tuples.isEmpty()) {
            return Collections.emptyList();
        }

        List<LeaderboardEntryResponse> results = new ArrayList<>();
        long rank = 1;

        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String userId = Objects.requireNonNull(tuple.getValue(), "userId must exist");
            String userName = (String) redisTemplate.opsForHash().get(profileKey, userId);

            results.add(new LeaderboardEntryResponse(
                    boardCode,
                    userId,
                    userName == null ? "Unknown" : userName,
                    rank,
                    tuple.getScore() == null ? 0D : tuple.getScore()
            ));
            rank++;
        }

        return results;
    }

    public UserRankingResponse getUserRanking(String boardCode, String userId) {
        String scoreKey = RedisKeyHelper.leaderboardScoreKey(boardCode);
        String profileKey = RedisKeyHelper.leaderboardProfileKey(boardCode);

        Long rank = redisTemplate.opsForZSet().reverseRank(scoreKey, userId);
        Double score = redisTemplate.opsForZSet().score(scoreKey, userId);
        String userName = (String) redisTemplate.opsForHash().get(profileKey, userId);

        return new UserRankingResponse(
                boardCode,
                userId,
                userName == null ? "Unknown" : userName,
                rank == null ? null : rank + 1,
                score
        );
    }

    public void clearBoard(String boardCode) {
        redisTemplate.delete(List.of(
                RedisKeyHelper.leaderboardScoreKey(boardCode),
                RedisKeyHelper.leaderboardProfileKey(boardCode)
        ));
    }
}
