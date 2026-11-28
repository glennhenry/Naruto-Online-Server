package game.activities

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

@Serializable
data class GetActivityMainUIResponse(
    val retInfo: RetInfo = RetInfo(0, ""),
    val timingActivityInfo: ActivityHubTimingInfo,
    val dailyActivityList: DailyActivityInfo
)
