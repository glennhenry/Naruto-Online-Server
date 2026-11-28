package game.items

import kotlinx.serialization.Serializable

@Serializable
data class EquipInfo(
    val id: UInt,
    val num: UInt,
    val level: UInt,
    val inlay: List<JadeInfo>,
    val sequence: ULong,
    // equipment strengthen?
    val forgeAttr: List<Int>,
    val index: UInt,
    // equipment upgrade?
    val forgeQualityAttr: List<Int>,
    val forgeLevel: Int,
    val inlayStone: List<BagStoneInfo>,
    val forgeSurmount: Int,
    val protoFumoInfo: EquipFumoInfo,
) {
    companion object {
        fun dummy(id: UInt): EquipInfo {
            return EquipInfo(
                id, 1u, 5u,
                listOf(JadeInfo.dummy()),
                123U, listOf(1, 3, 5, 7, 9), 0u,
                listOf(3, 5, 7, 9, 11), 12,
                listOf(BagStoneInfo.dummy()),
                1, EquipFumoInfo.dummy()
            )
        }

        fun dummy(): List<EquipInfo> {
            return listOf(
                // hokage (yellow) equipment
                dummy(30501701u),
                dummy(30502701u),
                dummy(30503701u),
                dummy(30504701u),
                dummy(30505701u)
            )
        }
    }
}
