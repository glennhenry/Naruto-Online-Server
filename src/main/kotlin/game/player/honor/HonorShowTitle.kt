package game.player.honor

import kotlinx.serialization.Serializable

@Serializable
data class HonorShowTitle(
    val cupId: Int,
    val affixId: Int,
    val effectId: Int
)
