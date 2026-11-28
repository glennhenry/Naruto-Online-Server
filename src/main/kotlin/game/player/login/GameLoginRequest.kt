package game.player.login

import game.player.common.PlayerKey
import kotlinx.serialization.Serializable

@Serializable
data class GameLoginRequest(
    val source: String,
    val roleId: UInt?,
    val channel: UInt,
    val version: String,
    val skey: String,
    val privilege: String,
    val tgpInfo: TGPInfo,
    val xyChannel: String,
    val adTag: String?,
    val loginChannel: UInt,
    val invitor: PlayerKey?
)
