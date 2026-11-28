package game.character.honor

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

@Serializable
data class HonorSettingResponse(
    val ret: RetInfo = RetInfo(0, ""),
    val info: HonorSettingInfo?
)
