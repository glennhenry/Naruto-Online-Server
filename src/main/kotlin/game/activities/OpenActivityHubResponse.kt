package game.activities

import game.DailyItemStore
import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

@Serializable
data class OpenActivityHubResponse(
    val retInfo: RetInfo = RetInfo(0, ""),
    val todayActivityList: List<ActivityHubNodeInfo>,
    val otherActivityList: List<ActivityHubNodeInfo>,
    val activePoint: Int,
    val storeInfo: DailyItemStore
)
