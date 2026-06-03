package com.detech_digital.nim.controller

import com.detech_digital.nim.dto.StrategyRequestDto
import com.detech_digital.nim.service.GameService
import com.detech_digital.nim.service.OptimalStrategy
import com.detech_digital.nim.service.RandomStrategy
import com.detech_digital.nim.util.logger
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.getValue

@RestController
@RequestMapping("/api/computer")
class ComputerController(private val gameService: GameService) {
    private val log by logger()

    @PutMapping("/strategy")
    fun updateStrategy(@Valid @RequestBody request: StrategyRequestDto): ResponseEntity<Any> {
        val computerStrategy = when (request.strategy) {
            "optimal" -> OptimalStrategy()
            else -> RandomStrategy()
        }
        log.info("Update strategy to ${request.strategy}")
        gameService.setComputerStrategy(computerStrategy)
        return ResponseEntity.ok(mapOf("message" to "Strategy updated to ${request.strategy}"))
    }
}