package game.player.honor

import kotlinx.serialization.Serializable

@Serializable
data class HonorSettingInfo(
    val scope: Int,
    val scopeCount: Int
)
