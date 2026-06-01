package com.detech_digital.nim.service

import com.detech_digital.nim.dto.GameState

/**
 * Service for determining computer player moves in Nim game.
 * Supports different strategies via implementation.
 */
interface ComputerPlayerService {
    /**
     * Calculate the optimal number of matches to take.
     *
     * @param heap Current size of heap
     * @return Number of matches to take (1-3)
     */
    fun getMoveAmount(heap: Int): Int
}
