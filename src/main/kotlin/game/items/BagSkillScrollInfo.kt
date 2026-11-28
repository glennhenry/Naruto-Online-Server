package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BagSkillScrollInfo(
   val id: UInt,
   val index: UInt,
   val num: UInt,
   val exp: UInt,
   val level: UInt,
   val attr: SkillScrollAttr,
   val sequence: ULong,
   val totalExp: UInt,
   val nextAttr: SkillScrollAttr
) {
    companion object {
        fun dummy(): BagSkillScrollInfo {
            return BagSkillScrollInfo(
                id = 14500009u,
                index = 0u,
                num = 1u,
                exp = 123u,
                level = 5u,
                attr = SkillScrollAttr(1u),
                sequence = 1u,
                totalExp = 1u,
                nextAttr = SkillScrollAttr(1u)
            )
        }
    }
}
