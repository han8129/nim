package com.detech_digital.nim.controller

import com.detech_digital.nim.dto.GameState
import com.detech_digital.nim.dto.MoveRequestDto
import com.detech_digital.nim.dto.NewGameRequestDto
import com.detech_digital.nim.util.logger
import com.detech_digital.nim.service.GameService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/games")
class GameController(private val gameService: GameService) {
    private val log by logger()

    @PostMapping("/move")
    fun makeMove(
        @Valid @RequestBody payload: MoveRequestDto,
    ): ResponseEntity<Any> {
        val gameState = gameService.getState()
        if (gameState.isGameOver) {
            log.warn("Making move when game is over. payload = $payload")
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Game is over")
        }

        log.info("Take ${payload.take} from heap")
        val humanMove = gameService.playTurn(
            amount = payload.take,
        )
        log.info("Game State: ${gameService.getState()}")
        return ResponseEntity.ok(humanMove)
    }

    @GetMapping("")
    fun getGame(): ResponseEntity<GameState> {
        val game = gameService.getState()
        log.info("Game State: $game")
        return ResponseEntity.ok(game)
    }

    @PostMapping("")
    fun newGame(@Valid @RequestBody payload: NewGameRequestDto): ResponseEntity<GameState> {
        val game = gameService.newGame(payload.heap)
        log.info("New game $game")
        return ResponseEntity.ok(game)
    }
}
