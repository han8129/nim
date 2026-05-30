package com.detech_digital.nim.model

import java.util.UUID

enum class Player {
    HUMAN, COMPUTER
}

data class Move(
    val id: UUID = UUID.randomUUID(),
    val take: Int,
    val heapBefore: Int,
    val player: Player
)


data class GameState(val heap: Int, val moves: List<Move>, val isGameOver: Boolean)

data class Game(
    var heap: Int,
    val moves: MutableList<Move> = mutableListOf(),
) {
    /**
     * Executes a move: validates it, updates the heap,
     * logs the move, and checks the win condition.
     */
    fun makeMove(amountToTake: Int, player: Player) {
        require(heap > 0) { "Game is already over." }
        require(amountToTake in 1..heap) { "Invalid move. You cannot take $amountToTake matches from a heap of $heap." }

        // 1. Record the move
        moves.add(Move(take = amountToTake, heapBefore = heap, player = player))

        // 2. Reduce the heap
        heap -= amountToTake
    }
    
    fun getState(): GameState {
        val isGameOver = (heap < 1)
        return GameState(heap = heap, moves = moves.toList(), isGameOver = isGameOver)
    }
}
