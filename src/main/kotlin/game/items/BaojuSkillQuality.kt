package game.items

import kotlinx.serialization.Serializable

@Serializable
enum class BaojuSkillQuality(val id: Int) {
    PROTO_BAOJU_SKILL_QUALITY_INVALID(0),
    PROTO_BAOJU_SKILL_QUALITY_GREEN(1),
    PROTO_BAOJU_SKILL_QUALITY_BLUE(2),
    PROTO_BAOJU_SKILL_QUALITY_PURPLE(3),
    PROTO_BAOJU_SKILL_QUALITY_ORANGE(4),
    PROTO_BAOJU_SKILL_QUALITY_RED(5),
    PROTO_BAOJU_SKILL_QUALITY_BLUE_PURPLE(6);

    companion object {
        private val lookup = BaojuSkillQuality.entries.associateBy { it.id }
        fun fromId(id: Int): BaojuSkillQuality? = lookup[id]
    }
}
