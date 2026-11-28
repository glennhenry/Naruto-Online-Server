package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BagItemInfo(
    val commonItemInfo: CommonItemInfo,
    val jadeInfo: JadeInfo,
    val index: UInt
) {
    companion object {
        fun dummy(): BagItemInfo {
            return BagItemInfo(
                commonItemInfo = CommonItemInfo.dummy(),
                jadeInfo = JadeInfo.dummy(),
                index = 0u
            )
        }
    }
}
