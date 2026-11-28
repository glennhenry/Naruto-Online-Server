package game.activities

import game.items.ItemInfo
import kotlinx.serialization.Serializable

@Serializable
data class AwardPackageBox(
    val boxId: UInt,
    val awardStatus: UInt,
    val award: ItemInfo,
    val value: UInt,
    val boxDesc: String,
    val totalLeft: UInt,
    val totalRight: UInt,
    val beginTime: UInt,
    val endTime: UInt
) {
    companion object {
        fun dummy(): AwardPackageBox {
            return AwardPackageBox(
                boxId = (1u..1000u).random(),
                awardStatus = 1u,
                award = ItemInfo.dummy(),
                value = 1u,
                boxDesc = "Box description",
                totalLeft = 1u,
                totalRight = 1u,
                beginTime = 0u,
                endTime = 1000u
            )
        }
    }
}
