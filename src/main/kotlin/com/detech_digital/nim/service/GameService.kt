package com.detech_digital.nim.service

import com.detech_digital.nim.model.Game
import com.detech_digital.nim.model.GameState
import com.detech_digital.nim.model.Move
import com.detech_digital.nim.model.Player
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameService {
    companion object {
        val gameInstance: Game by lazy {
            Game(
                heap = 12,
            )
        }
    }

    fun playTurn(amount: Int, player: Player): Result<GameState> {
        return gameInstance.makeMove(amount, player)
    }

    fun getState(): GameState {
        return gameInstance.getState()
    }
}
