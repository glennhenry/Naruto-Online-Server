package game.items

import kotlinx.serialization.Serializable

// the 3 traits of treasure tool
@Serializable
data class BaojuSpecialityData(
    val level: UInt,
    val exp: UInt,
    val state: BaojuSkillInlayState,
    val quality: BaojuSkillQuality,
    val attr: BaojuSpecialityAttr,
    val nextAttr: BaojuSpecialityAttr,
    val unlockLevel: UInt,
    val index: UInt,
    val nextLevel: UInt,
    val upgradeNeedExp: UInt,
    val totalExp: UInt,
    val needBaojuLevel: UInt
) {
    companion object {
        fun dummy(): BaojuSpecialityData {
            return BaojuSpecialityData(
                level = 6u,
                exp = 12u,
                state = BaojuSkillInlayState.PROTO_BAOJU_SKILL_INLAY_STATE_LOCK,
                quality = BaojuSkillQuality.PROTO_BAOJU_SKILL_QUALITY_GREEN,
                attr = BaojuSpecialityAttr.dummy(),
                nextAttr = BaojuSpecialityAttr.dummy(),
                unlockLevel = 12u,
                index = 0u,
                nextLevel = 7u,
                upgradeNeedExp = 100u,
                totalExp = 1200u,
                needBaojuLevel = 15u
            )
        }
    }
}
