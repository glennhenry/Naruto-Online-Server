package game.activities

import kotlinx.serialization.Serializable

@Serializable
data class DailyItemStore(
    val items: List<DailyItem>
)
