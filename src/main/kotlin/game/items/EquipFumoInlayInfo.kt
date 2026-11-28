package game.items

import kotlinx.serialization.Serializable

@Serializable
data class EquipFumoInlayInfo(
    val protoJadeInfo: JadeInfo,
    val protoStoneInfo: BagStoneInfo,
    val isOpen: Boolean
) {
    companion object {
        fun dummy(): EquipFumoInlayInfo {
            return EquipFumoInlayInfo(
                protoJadeInfo = JadeInfo.dummy(),
                protoStoneInfo = BagStoneInfo.dummy(),
                isOpen = true
            )
        }
    }
}
