package com.study.demo.common.api;

/**
 * A tiny response wrapper keeps the demo APIs consistent.
 * This makes the frontend pages easier to implement and explain.
 *
 * @param success whether the request succeeded
 * @param message human-readable message for the learner
 * @param data actual payload
 * @param <T> payload type
 */
public record ApiResponse<T>(boolean success, String message, T data) {

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "ok", data);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
