package game.items

import kotlinx.serialization.Serializable

// treasure tool
@Serializable
data class BagBaojuInfo(
    val id: UInt,
    val index: UInt,
    val num: UInt,
    val exp: UInt,
    val level: UInt,
    val sequence: ULong,
    val quality: BaojuQuality,
    val attr: BaojuAttr,
    val maxSkillQuality: BaojuSkillQuality,
    val skillInlay: BaojuSkilleInlayData,
    val nextAttr: BaojuAttr,
    val upgradeEvent: BaojuEvent,
    val upgradeNeedMaterialList: List<ItemInfo>,
    val specialityDataList: List<BaojuSpecialityData>,
    val recoverOreList: List<ItemInfo>,
    val recoverJingcuiList: List<ItemInfo>,
    val isOnFormation: Boolean,
    val eventSpecialityIndex: UInt,
    val isLock: Boolean
) {
    companion object {
        fun dummy(): BagBaojuInfo {
            return BagBaojuInfo(
                id = 10100000u,
                index = 0u,
                num = 1u,
                exp = 123u,
                level = 80u,
                sequence = 10u,
                quality = BaojuQuality.PROTO_BAOJU_QUALITY_B,
                attr = BaojuAttr.dummy(),
                maxSkillQuality = BaojuSkillQuality.PROTO_BAOJU_SKILL_QUALITY_RED,
                skillInlay = BaojuSkilleInlayData.dummy(),
                nextAttr = BaojuAttr.dummy(),
                upgradeEvent = BaojuEvent.PROTO_BAOJU_EVENT_UPGRADE_QUAL,
                upgradeNeedMaterialList = emptyList(),
                specialityDataList = emptyList(),
                recoverOreList = emptyList(),
                recoverJingcuiList = emptyList(),
                isOnFormation = false,
                eventSpecialityIndex = 0u,
                isLock = false
            )
        }
    }
}
