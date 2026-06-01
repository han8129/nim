package com.detech_digital.nim.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class MoveRequestDto(
    @field:Min(1)
    @field:Max(3)
    @JsonProperty("take") val take: Int,
)