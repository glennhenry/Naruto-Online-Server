package game.player.activities.wishingLantern

import game.player.common.PlayerKey
import kotlinx.serialization.Serializable

@Serializable
data class WishingLightPlayerInfo(
    val playerId: PlayerKey,
    val name: String,
    val roleCategory: UInt,
    val praiseCount: UInt
)
