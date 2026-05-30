package com.detech_digital.nim.controller

import com.detech_digital.nim.model.GameState
import com.detech_digital.nim.model.Player
import com.detech_digital.nim.service.GameService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class MoveDto(
    @JsonProperty("take") var take: Int,
)

@RestController
@RequestMapping("/api/games")
class GameController(private val gameService: GameService) {

    @PutMapping("/move")
    fun makeMove(
        @RequestBody payload: MoveDto,
    ): ResponseEntity<Any> {
        return try {
            val gameState = gameService.playTurn(payload.take, player = Player.HUMAN)
            ResponseEntity.ok(gameState)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("")
    fun getGame(): ResponseEntity<GameState> {
        val game = gameService.getState()
        return ResponseEntity.ok(game)
    }
}
