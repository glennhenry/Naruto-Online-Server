package game.player.common

import kotlinx.serialization.Serializable

@Serializable
data class PlayerKey(
    val uin: UInt,
    val roleId: UInt,
    val svrId: UInt
)
