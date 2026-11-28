package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BaojuSkilleInlayData(
    val index: UInt,
    val skillScrollInfo: BagSkillScrollInfo
) {
    companion object {
        fun dummy(): BaojuSkilleInlayData {
            return BaojuSkilleInlayData(
                index = 0u,
                skillScrollInfo = BagSkillScrollInfo.dummy()
            )
        }
    }
}
