package com.detech_digital.nim.service

import com.detech_digital.nim.dto.GameState
import com.detech_digital.nim.model.Game
import com.detech_digital.nim.model.Player
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class GameService(
    @Qualifier("randomStrategy")
    private var computerStrategy: ComputerPlayerService
) {
    companion object {
        var gameInstance: Game = Game(
            heap = 0,
        )
    }

    fun playTurn(amount: Int): Result<GameState> {
        val humanGameState = gameInstance.makeMove(amount, Player.HUMAN)
        if (humanGameState.isFailure) {
            return humanGameState
        }

        // computer turn
        val newHeap = humanGameState.map { it.heap }.getOrDefault(0)
        val computerTake = computerStrategy.getMoveAmount(heap = newHeap)
        gameInstance.makeMove(computerTake, Player.COMPUTER)
        return humanGameState
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

    /**
     * Switch computer strategy at runtime.
     *
     * @param newStrategy New ComputerPlayerService implementation
     */
    fun setComputerStrategy(newStrategy: ComputerPlayerService) {
        computerStrategy = newStrategy
    }
}
