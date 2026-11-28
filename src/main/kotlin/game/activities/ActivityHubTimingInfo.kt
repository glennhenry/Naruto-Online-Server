package game.activities

import kotlinx.serialization.Serializable

@Serializable
data class ActivityHubTimingInfo(
    val timingActivityId: Int,
    val timingActivityDesc: String,
    val canEnter: Boolean
) {
    companion object {
        fun dummy(): ActivityHubTimingInfo {
            return ActivityHubTimingInfo(
                timingActivityId = 1, // nine tails
                timingActivityDesc = "Timing activity description",
                canEnter = true
            )
        }
    }
}
