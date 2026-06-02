package com.detech_digital.nim.service

import com.detech_digital.nim.model.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GameServiceTest {

    private lateinit var computerStrategy: ComputerPlayerService
    private lateinit var gameService: GameService

    @BeforeEach
    fun setup() {
        computerStrategy = mock(ComputerPlayerService::class.java)
        gameService = GameService(computerStrategy)
    }

    @Test
    fun `playTurn should return gameState and trigger computer turn`() {
        val initialHeap = 10
        val humanTake = 2
        val computerTake = 3
        gameService.newGame(initialHeap)

        `when`(computerStrategy.getMoveAmount(8)).thenReturn(computerTake)

        val result = gameService.playTurn(humanTake)

        val stateAfterHuman = result.getOrThrow()
        assertEquals(8, stateAfterHuman.heap)
        assertEquals(Player.COMPUTER, stateAfterHuman.turn)
        assertEquals(1, stateAfterHuman.moves.size)
        assertEquals(humanTake, stateAfterHuman.moves[0].take)
        assertEquals(Player.HUMAN, stateAfterHuman.moves[0].player)

        val finalState = gameService.getState()
        assertEquals(5, finalState.heap)
        assertEquals(Player.HUMAN, finalState.turn)
        assertEquals(2, finalState.moves.size)
        assertEquals(computerTake, finalState.moves[1].take)
        assertEquals(Player.COMPUTER, finalState.moves[1].player)
    }
}
