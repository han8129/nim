package com.detech_digital.nim.model

data class Move(
    val take: Int,
    val heapBefore: Int, // heap size before taken
    val player: Player
)

