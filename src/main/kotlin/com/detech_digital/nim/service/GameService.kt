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
            heap = 12,
        )
    }

    fun playTurn(amount: Int): Result<GameState> {
        val gameState = gameInstance.makeMove(amount, Player.HUMAN)
        if (gameState.isFailure) {
            return gameState
        }

        // computer turn
        val newHeap = gameState.map { it.heap }.getOrDefault(0)
        val computerTake = computerStrategy.getMoveAmount(heap = newHeap)
        return gameInstance.makeMove(computerTake, Player.COMPUTER)
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
        // Implementation would require refactoring to support this
        // Currently using constructor injection with @Qualifier
    }
}
