package game.activities.dayActivity

import game.protocol.response.RetInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class GetDayActivityInfoResponse(
    val ret: RetInfo = RetInfo(0, ""),
    val dayActivityInfo: DayActivityInfo
)
