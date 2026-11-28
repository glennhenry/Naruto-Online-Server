package game.others

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

@Serializable
data class PingResponse(
    val ret: RetInfo = RetInfo(0, "")
)
