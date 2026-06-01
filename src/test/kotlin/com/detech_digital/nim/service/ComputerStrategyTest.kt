package com.detech_digital.nim.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ComputerStrategyTest {

    @Nested
    inner class RandomStrategyTests {
        private val strategy = RandomStrategy()

        @Test
        fun `should return value between 1 and 3 for normal heap`() {
            repeat(100) {
                val move = strategy.getMoveAmount(10)
                assertTrue(move in 1..3, "Move should be 1-3, got $move")
            }
        }

        @Test
        fun `should return 1 for heap size 1`() {
            val move = strategy.getMoveAmount(1)
            assertEquals(1, move)
        }

        @Test
        fun `should return at most 2 for heap size 2`() {
            repeat(50) {
                val move = strategy.getMoveAmount(2)
                assertTrue(move in 1..2, "Move should be 1-2, got $move")
            }
        }

        @Test
        fun `should return at most 3 for heap size 3`() {
            repeat(50) {
                val move = strategy.getMoveAmount(3)
                assertTrue(move in 1..3, "Move should be 1-3, got $move")
            }
        }

        @Test
        fun `should distribute moves fairly across 1-3 range`() {
            val moves = mutableMapOf(1 to 0, 2 to 0, 3 to 0)
            repeat(1000) {
                val move = strategy.getMoveAmount(10)
                moves[move] = moves[move]!! + 1
            }
            
            // Each move should occur roughly 333 times (roughly equal distribution)
            moves.values.forEach { count ->
                assertTrue(count > 200, "Distribution too skewed: $moves")
            }
        }
    }

    @Nested
    inner class OptimalStrategyTests {
        private val strategy = OptimalStrategy()

        @Test
        fun `should return valid move for heap 1`() {
            val move = strategy.getMoveAmount(1)
            assertEquals(1, move)
        }

        @Test
        fun `should return valid move for heap 2`() {
            val move = strategy.getMoveAmount(2)
            assertEquals(1, move, "Move should be 1, got $move")
        }

        @Test
        fun `should return valid move for heap 3`() {
            val move = strategy.getMoveAmount(3)
            assertEquals(2, move, "Move should be 2, got $move")
        }

        @Test
        fun `should return valid move for heap 4`() {
            val move = strategy.getMoveAmount(4)
            assertTrue(move in 1..3, "Move should be 3, got $move")
        }

        @Test
        fun `should return valid move for heap 10`() {
            val move = strategy.getMoveAmount(10)
            assertTrue(move in 1..3, "Move should be 1-3, got $move")
        }

        @Test
        fun `should apply winning strategy for heap 4 (take 1 to leave 3)`() {
            // Heap of 4 is a losing position in misere nim with single heap
            // Computer should make any valid move
            val move = strategy.getMoveAmount(4)
            assertEquals(3, move , "Move should be 3, got $move")
        }

        @Test
        fun `should not exceed heap size`() {
            val move = strategy.getMoveAmount(2)
            assertTrue(move <= 2, "Cannot take $move from heap of 2")
        }
    }
}
