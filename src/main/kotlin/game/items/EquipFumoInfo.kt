package game.items

import kotlinx.serialization.Serializable

// equipment purification?
@Serializable
data class EquipFumoInfo(
    val fumoLevel: UInt,
    val maxLevel: UInt,
    val blessValue: UInt,
    val maxBlessValue: UInt,
    val levelAttr: EquipFumoLevelAttr,
    val inlayInfoAry: List<EquipFumoInlayInfo>,
    val tmpMaxLevel: UInt
) {
    companion object {
        fun dummy(): EquipFumoInfo {
            return EquipFumoInfo(
                fumoLevel = 1u,
                maxLevel = 5u,
                blessValue = 10u,
                maxBlessValue = 20u,
                levelAttr = EquipFumoLevelAttr.dummy(),
                inlayInfoAry = listOf(EquipFumoInlayInfo.dummy()),
                tmpMaxLevel = 120u
            )
        }
    }
}
