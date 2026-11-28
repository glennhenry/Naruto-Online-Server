package game.player.login

import kotlinx.serialization.Serializable

@Serializable
data class SecondaryPasswordInfo(
    val status: SecondaryPasswordStatus,
    val remaindForgetTime: UInt,
    val canTryTimes: Int
)
