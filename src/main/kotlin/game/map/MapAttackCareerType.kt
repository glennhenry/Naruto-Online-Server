package game.map

import kotlinx.serialization.Serializable

@Serializable
enum class MapAttackCareerType(val id: Int) {
    PROTO_MAP_ATTACK_CAREER_TYPE_NONE(0),
    PROTO_MAP_ATTACK_CAREER_TYPE_NORMAL(1),
    PROTO_MAP_ATTACK_CAREER_TYPE_DARK_AGILE(2),
    PROTO_MAP_ATTACK_CAREER_TYPE_PIONEER(3),
    PROTO_MAP_ATTACK_CAREER_TYPE_GUARD(4),
    PROTO_MAP_ATTACK_CAREER_TYPE_SCOUTING(5),
    PROTO_MAP_ATTACK_CAREER_TYPE_GROUP(6);

    companion object {
        private val lookup = MapAttackCareerType.entries.associateBy { it.id }
        fun fromId(id: Int): MapAttackCareerType? = lookup[id]
    }
}
