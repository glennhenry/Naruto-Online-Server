package game.activities

import kotlinx.serialization.Serializable

@Serializable
data class ActivityHubNodeInfo(
    val id: Int,
    val time: List<String>,
    val day: List<Int>,
    val specialDay: String,
    val allTime: Int,
    val remainderTime: Int,
    val status: ActivityStatus,
    val meetSpecialCondition: Boolean,
    val activePoint: Int,
    val activityId: Int,
    val isGetPoint: Int
) {
    companion object {
        fun dummy(): ActivityHubNodeInfo {
            return ActivityHubNodeInfo(
                id = 1,
                time = listOf("11:10", "12:10"),
                day = listOf(1, 3),
                specialDay = "Monday",
                allTime = 100,
                remainderTime = 20,
                status = ActivityStatus(1u, 1u),
                meetSpecialCondition = true,
                activePoint = 15,
                activityId = 1,
                isGetPoint = 1
            )
        }
    }
}
