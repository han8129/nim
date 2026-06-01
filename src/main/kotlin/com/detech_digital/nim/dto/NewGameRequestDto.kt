package com.detech_digital.nim.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Min

data class NewGameRequestDto(
    @field:Min(1)
    @JsonProperty("heap") val heap: Int,
)
