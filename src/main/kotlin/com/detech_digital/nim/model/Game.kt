package com.detech_digital.nim.model

import com.detech_digital.nim.dto.GameState

/**
 * Represents single Nim game session with fixed heap of matches.
 *
 * Two players, [Player.HUMAN] and [Player.COMPUTER], alternate taking 1 to 3 matches from heap.
 * Player who takes last match loses.
 *
 * @param heap Initial match count in heap
 * @param moves Ordered log of all moves made during session.
 */
class Game(
    private var heap: Int,
    private val moves: MutableList<Move> = mutableListOf(),
) {
    /**
     * Executes a move: validates it, updates the heap,
     * logs the move
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


    /**
     * Returns current game state snapshot.
     *
     * Captures heap size, move history, game-over status, and whose turn it is.
     * Turn determined by last move player.
     * If no moves made yet or COMPUTER moved last, next turn is COMPUTER.
     * Game over when heap reaches 0 (last match taken).
     *
     * @return referred GameState containing current heap, moves list, over flag, and next player
     */
    fun getState(): GameState {
        val isGameOver = (heap < 1)
        val turn = when {
            moves.isEmpty() -> null
            else
                -> when (moves.last().player) {
                Player.HUMAN -> Player.COMPUTER
                Player.COMPUTER -> Player.HUMAN
            }
        }

        return GameState(heap = heap, moves = moves.toList(), isGameOver = isGameOver, turn)
    }
}
