package game.activities

import game.items.ItemInfo
import kotlinx.serialization.Serializable

@Serializable
data class DailyItem(
    val id: Int,
    val items: List<ItemInfo>,
    val exchangeCount: Int,
    val leftCount: Int,
    val needPoint: Int
) {
    companion object {
        fun dummy(): DailyItem {
            return DailyItem(
                id = 10801007,
                items = listOf(ItemInfo.dummy()),
                exchangeCount = 2,
                leftCount = 1,
                needPoint = 10
            )
        }
    }
}
