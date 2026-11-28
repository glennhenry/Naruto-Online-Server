package game.health

import kotlinx.serialization.Serializable

@Serializable
data class PlayerHeartBeatResponse(
    val curServerTime: UInt
)
