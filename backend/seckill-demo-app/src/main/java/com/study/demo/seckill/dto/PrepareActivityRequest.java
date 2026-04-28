package com.study.demo.seckill.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Activity preparation is exposed as an API so the frontend can drive the demo.
 */
public record PrepareActivityRequest(
        @NotBlank(message = "productName cannot be blank")
        String productName,
        @Min(value = 1, message = "stock must be greater than 0")
        int stock
) {
}
