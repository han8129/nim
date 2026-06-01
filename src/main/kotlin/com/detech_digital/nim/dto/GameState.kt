package com.detech_digital.nim.dto

import com.detech_digital.nim.model.Move
import com.detech_digital.nim.model.Player

data class GameState(val heap: Int, val moves: List<Move>, val isGameOver: Boolean, val turn: Player?)
