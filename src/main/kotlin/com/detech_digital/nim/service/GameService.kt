package com.detech_digital.nim.service

import com.detech_digital.nim.dto.GameState
import com.detech_digital.nim.model.Game
import com.detech_digital.nim.model.Player
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameService {
    companion object {
        var gameInstance: Game = Game(
            heap = 12,
        )
    }

    fun playTurn(amount: Int, player: Player): Result<GameState> {
        return gameInstance.makeMove(amount, player)
    }

    fun getState(): GameState {
        return gameInstance.getState()
    }

    fun newGame(heap: Int): GameState {
        gameInstance = Game(
            heap
        );

        return getState()
    }
}
