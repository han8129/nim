package com.detech_digital.nim.model

import com.detech_digital.nim.dto.GameState

class Game(
    internal var heap: Int,
    private val moves: MutableList<Move> = mutableListOf(),
) {
    /**
     * Executes a move: validates it, updates the heap,
     * logs the move, and checks the win condition.
     */
    fun makeMove(amountToTake: Int, player: Player): Result<GameState> {
        if (heap < 1) {
            return Result.failure(Error("Game is over"))
        }

        if (amountToTake > heap) {
            return Result.failure(Error("Invalid move. You cannot take $amountToTake matches from a heap of $heap."))
        }

        if (amountToTake !in 1..3) {
            return Result.failure(Error("Invalid move. You cannot take $amountToTake matches from a heap of $heap."))
        }
        // 1. Record the move
        moves.add(Move(take = amountToTake, heapBefore = heap, player = player))

        // 2. Reduce the heap
        heap -= amountToTake

        return Result.success(getState());
    }

    fun getState(): GameState {
        val isGameOver = (heap < 1)
        val turn = when {
            moves.isEmpty() -> null
            else
                -> when (moves.last().player) {
                Player.HUMAN -> Player.COMPUTER
                Player.COMPUTER -> Player.COMPUTER
            }
        }

        return GameState(heap = heap, moves = moves.toList(), isGameOver = isGameOver, turn)
    }
}
