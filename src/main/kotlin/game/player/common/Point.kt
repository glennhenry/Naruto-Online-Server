package game.player.common

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val x: UInt,
    val y: UInt
)
