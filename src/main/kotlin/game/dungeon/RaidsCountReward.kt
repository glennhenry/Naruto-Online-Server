package game.dungeon

import game.items.ItemInfo
import kotlinx.serialization.Serializable

@Serializable
data class RaidsCountReward(
    val raidsCount: UInt,
    val itemInfo: List<ItemInfo>,
    val vipItemInfo: List<ItemInfo>
) {
    companion object {
        fun dummy(): RaidsCountReward {
            return RaidsCountReward(
                raidsCount = 1u,
                itemInfo = listOf(ItemInfo.dummy()),
                vipItemInfo = listOf(ItemInfo.dummy())
            )
        }
    }
}
