package com.study.demo.leaderboard.controller;

import com.study.demo.common.api.ApiResponse;
import com.study.demo.leaderboard.dto.LeaderboardEntryResponse;
import com.study.demo.leaderboard.dto.ScoreUpdateRequest;
import com.study.demo.leaderboard.dto.UserRankingResponse;
import com.study.demo.leaderboard.service.LeaderboardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A compact controller is easier for beginners to read end-to-end.
 */
@Validated
@RestController
@RequestMapping("/api/leaderboards")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PostMapping("/{boardCode}/scores")
    public ApiResponse<LeaderboardEntryResponse> addScore(
            @PathVariable String boardCode,
            @Valid @RequestBody ScoreUpdateRequest request
    ) {
        return ApiResponse.success("score added", leaderboardService.addScore(boardCode, request));
    }

    @GetMapping("/{boardCode}/top")
    public ApiResponse<List<LeaderboardEntryResponse>> top(
            @PathVariable String boardCode,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit
    ) {
        return ApiResponse.success(leaderboardService.getTopN(boardCode, limit));
    }

    @GetMapping("/{boardCode}/users/{userId}")
    public ApiResponse<UserRankingResponse> userRanking(
            @PathVariable String boardCode,
            @PathVariable String userId
    ) {
        return ApiResponse.success(leaderboardService.getUserRanking(boardCode, userId));
    }

    @DeleteMapping("/{boardCode}")
    public ApiResponse<Void> clearBoard(@PathVariable String boardCode) {
        leaderboardService.clearBoard(boardCode);
        return ApiResponse.success("leaderboard cleared", null);
    }
}
