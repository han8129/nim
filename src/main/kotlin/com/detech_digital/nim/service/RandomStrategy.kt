package com.detech_digital.nim.service

import com.detech_digital.nim.dto.GameState
import org.springframework.stereotype.Service
import kotlin.random.Random

/**
 * Random strategy for computer player.
 * Takes a random amount from 1 to 3 matches.
 */
@Service("randomStrategy")
class RandomStrategy : ComputerPlayerService {
    override fun getMoveAmount(heap: Int): Int {
        val maxTake = minOf(3, heap)
        return Random.nextInt(1, maxTake + 1)
    }
}
