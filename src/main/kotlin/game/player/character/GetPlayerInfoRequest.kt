package game.player.character

import game.player.common.PlayerKey
import kotlinx.serialization.Serializable

@Serializable
data class GetPlayerInfoRequest(
    val playerKey: PlayerKey
)
