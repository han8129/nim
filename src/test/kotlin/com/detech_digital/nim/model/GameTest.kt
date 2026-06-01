package com.detech_digital.nim.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull

class GameTest {

    @Nested
    inner class MakeMoveTests {

        @Test
        fun `should successfully take 1 match from heap`() {
            val game = Game(heap = 10)
            val result = game.makeMove(amountToTake = 1, player = Player.HUMAN)

            assertTrue(result.isSuccess)
            val state = result.getOrNull()!!
            assertEquals(9, state.heap)
            assertEquals(1, state.moves.size)
            assertEquals(1, state.moves[0].take)
            assertEquals(10, state.moves[0].heapBefore)
            assertEquals(Player.HUMAN, state.moves[0].player)
        }

        @Test
        fun `should successfully take 2 matches from heap`() {
            val game = Game(heap = 10)
            val result = game.makeMove(amountToTake = 2, player = Player.HUMAN)

            assertTrue(result.isSuccess)
            val state = result.getOrNull()!!
            assertEquals(8, state.heap)
        }

        @Test
        fun `should successfully take 3 matches from heap`() {
            val game = Game(heap = 10)
            val result = game.makeMove(amountToTake = 3, player = Player.HUMAN)

            assertTrue(result.isSuccess)
            val state = result.getOrNull()!!
            assertEquals(7, state.heap)
        }

        @Test
        fun `should fail when taking more than 3 matches`() {
            val game = Game(heap = 10)
            val result = game.makeMove(amountToTake = 4, player = Player.HUMAN)

            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()!!
            assertEquals("Invalid move. You cannot take 4 matches from a heap of 10.", error.message)
        }

        @Test
        fun `should fail when taking 0 matches`() {
            val game = Game(heap = 10)
            val result = game.makeMove(amountToTake = 0, player = Player.HUMAN)

            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()!!
            assertEquals("Invalid move. You cannot take 0 matches from a heap of 10.", error.message)
        }

        @Test
        fun `should fail when taking negative matches`() {
            val game = Game(heap = 10)
            val result = game.makeMove(amountToTake = -1, player = Player.HUMAN)

            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()!!
            assertEquals("Invalid move. You cannot take -1 matches from a heap of 10.", error.message)
        }

        @Test
        fun `should fail when taking more than available in heap`() {
            val game = Game(heap = 2)
            val result = game.makeMove(amountToTake = 3, player = Player.HUMAN)

            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()!!
            assertEquals("Invalid move. You cannot take 3 matches from a heap of 2.", error.message)
        }

        @Test
        fun `should fail when game is over`() {
            val game = Game(heap = 0)
            val result = game.makeMove(amountToTake = 1, player = Player.HUMAN)

            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()!!
            assertEquals("Game is over", error.message)
        }

        @Test
        fun `should record multiple moves in order`() {
            val game = Game(heap = 10)
            game.makeMove(1, Player.HUMAN)
            game.makeMove(2, Player.COMPUTER)
            val result = game.makeMove(3, Player.HUMAN)

            val state = result.getOrNull()!!
            assertEquals(3, state.moves.size)
            assertEquals(1, state.moves[0].take)
            assertEquals(2, state.moves[1].take)
            assertEquals(3, state.moves[2].take)
        }

        @Test
        fun `should track heap before for each move`() {
            val game = Game(heap = 10)
            game.makeMove(1, Player.HUMAN)
            game.makeMove(2, Player.COMPUTER)

            val state = game.getState()
            assertEquals(10, state.moves[0].heapBefore)
            assertEquals(9, state.moves[1].heapBefore)
        }
    }

    @Nested
    inner class GetStateTests {

        @Test
        fun `should return initial state with no moves`() {
            val game = Game(heap = 10)
            val state = game.getState()

            assertEquals(10, state.heap)
            assertEquals(0, state.moves.size)
            assertFalse(state.isGameOver)
            assertNull(state.turn)
        }

        @Test
        fun `should mark game as over when heap reaches 0`() {
            val game = Game(heap = 1)
            game.makeMove(1, Player.HUMAN)
            val state = game.getState()

            assertEquals(0, state.heap)
            assertTrue(state.isGameOver)
        }

        @Test
        fun `should mark game as over when heap is negative`() {
            val game = Game(heap = -1)
            val state = game.getState()

            assertTrue(state.isGameOver)
        }

        @Test
        fun `should have next turn as computer after human move`() {
            val game = Game(heap = 10)
            game.makeMove(1, Player.HUMAN)
            val state = game.getState()

            assertEquals(Player.COMPUTER, state.turn)
        }

        @Test
        fun `should have next turn as computer after computer move`() {
            val game = Game(heap = 10)
            game.makeMove(1, Player.HUMAN)
            game.makeMove(1, Player.COMPUTER)
            val state = game.getState()

            assertEquals(Player.HUMAN, state.turn)
        }

        @Test
        fun `should return copy of moves list`() {
            val game = Game(heap = 10)
            game.makeMove(1, Player.HUMAN)
            val state1 = game.getState()
            game.makeMove(2, Player.COMPUTER)
            val state2 = game.getState()

            assertEquals(1, state1.moves.size)
            assertEquals(2, state2.moves.size)
        }

        @Test
        fun `should reflect correct heap after each move`() {
            val game = Game(heap = 10)
            game.makeMove(1, Player.HUMAN)
            var state = game.getState()
            assertEquals(9, state.heap)

            game.makeMove(2, Player.COMPUTER)
            state = game.getState()
            assertEquals(7, state.heap)

            game.makeMove(3, Player.HUMAN)
            state = game.getState()
            assertEquals(4, state.heap)
        }

        @Test
        fun `should return null turn when no moves made yet`() {
            val game = Game(heap = 10)
            val state = game.getState()

            assertNull(state.turn)
        }
    }

    @Nested
    inner class EdgeCaseTests {

        @Test
        fun `should handle single match heap`() {
            val game = Game(heap = 1)
            val result = game.makeMove(1, Player.HUMAN)

            assertTrue(result.isSuccess)
            val state = result.getOrNull()!!
            assertEquals(0, state.heap)
            assertTrue(state.isGameOver)
        }

        @Test
        fun `should handle exactly matching heap size`() {
            val game = Game(heap = 3)
            val result = game.makeMove(3, Player.HUMAN)

            assertTrue(result.isSuccess)
            val state = result.getOrNull()!!
            assertEquals(0, state.heap)
        }

        @Test
        fun `should handle large heap`() {
            val game = Game(heap = 1000)
            val result = game.makeMove(3, Player.HUMAN)

            assertTrue(result.isSuccess)
            val state = result.getOrNull()!!
            assertEquals(997, state.heap)
        }

        @Test
        fun `should persist moves across multiple get state calls`() {
            val game = Game(heap = 10)
            game.makeMove(1, Player.HUMAN)
            
            val state1 = game.getState()
            val state2 = game.getState()

            assertEquals(state1.moves, state2.moves)
            assertEquals(1, state1.moves.size)
            assertEquals(1, state2.moves.size)
        }
    }
}
