package game.player.character

import kotlinx.serialization.Serializable

// in EN version this is not used
@Serializable
data class DiamondInfo(
    val blueLevel: Int,
    val blueCommon: Boolean,
    val blueYear: Boolean,
    val blueSuper: Boolean,
    val yellowLevel: Int,
    val yellowCommon: Boolean,
    val yellowYear: Boolean,
    val yellowSuper: Boolean,
    val qqLevel: Int,
    val qqCommon: Boolean,
    val qqYear: Boolean,
    val qqSuper: Boolean
)
