package com.detech_digital.nim.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class StrategyRequestDto(
    @field:NotBlank(message = "Strategy is required")
    @field:Pattern(regexp = "^(random|optimal)$", message = "Strategy must be 'random' or 'optimal'")
    val strategy: String
)
