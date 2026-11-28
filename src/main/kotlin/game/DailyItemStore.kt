package game

import game.activities.DailyItem
import kotlinx.serialization.Serializable

@Serializable
data class DailyItemStore(
    val items: List<DailyItem>
)
