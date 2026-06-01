package com.detech_digital.nim.service

import com.detech_digital.nim.dto.GameState
import org.springframework.stereotype.Service

/**
 * Optimal strategy for computer player using Misere Nim analysis.
 * Applies game theory to determine winning moves in Misere Nim variant
 * (player who takes last match loses).
 */
@Service("optimalStrategy")
class OptimalStrategy : ComputerPlayerService {
    override fun getMoveAmount(heap: Int): Int {
        return when {
            heap > 3 -> 3
            heap > 1 -> heap - 1 // Leave exactly 1 to force opponent to lose
            else -> 1           // Forced to take the last one and lose
        }
    }
}
