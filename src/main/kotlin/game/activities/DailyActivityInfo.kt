package game.activities

import kotlinx.serialization.Serializable

@Serializable
data class DailyActivityInfo(
    val id: Int,
    val allTime: Int,
    val remainderTime: Int,
    val cdTime: Int
) {
    companion object {
        fun dummy(): DailyActivityInfo {
            return DailyActivityInfo(
                id = 1,
                allTime = 1200,
                remainderTime = 600,
                cdTime = 200
            )
        }
    }
}
