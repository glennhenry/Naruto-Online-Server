package game.player.character

import game.protocol.response.RetInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class GetPlayerInfoResponse(
    val ret: RetInfo = RetInfo(0, ""),
    val player: PlayerInfo
)
