package com.detech_digital.nim.controller

import com.detech_digital.nim.dto.GameState
import com.detech_digital.nim.dto.MoveRequestDto
import com.detech_digital.nim.dto.NewGameRequestDto
import com.detech_digital.nim.service.GameService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/games")
class GameController(private val gameService: GameService) {

    @PostMapping("/move")
    fun makeMove(
        @Valid @RequestBody payload: MoveRequestDto,
    ): ResponseEntity<Any> {
        val gameState = gameService.getState()
        if (gameState.isGameOver) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Game is over")
        }

        val humanMove = gameService.playTurn(
            amount = payload.take,
        )
        println("Game State: ${gameService.getState()}")
        return ResponseEntity.ok(humanMove)
    }

    @GetMapping("")
    fun getGame(): ResponseEntity<GameState> {
        val game = gameService.getState()
        return ResponseEntity.ok(game)
    }

    @PostMapping("")
    fun newGame(@Valid @RequestBody payload: NewGameRequestDto): ResponseEntity<GameState> {
        val game = gameService.newGame(payload.heap)
        return ResponseEntity.ok(game)
    }
}
