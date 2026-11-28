package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BagFamilyNinjaGiftItemInfo(
    val id: UInt,
    val index: UInt,
    val num: UInt
) {
    companion object {
        fun dummy(): BagFamilyNinjaGiftItemInfo {
            return BagFamilyNinjaGiftItemInfo(
                id = 44300001u,
                index = 0u,
                num = 2u
            )
        }
    }
}
