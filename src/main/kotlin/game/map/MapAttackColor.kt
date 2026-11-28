package game.map

import kotlinx.serialization.Serializable

@Serializable
enum class MapAttackColor(val id: Int) {
    PROTO_MAP_ATTACK_COLOR_WHITE(0),
    PROTO_MAP_ATTACK_COLOR_RED(1),
    PROTO_MAP_ATTACK_COLOR_BLUE(2),
    PROTO_MAP_ATTACK_COLOR_GREEN(3),
    PROTO_MAP_ATTACK_COLOR_YELOW(4),
    PROTO_MAP_ATTACK_COLOR_PURPLE(5),
    PROTO_MAP_ATTACK_COLOR_ORINGE(6),
    PROTO_MAP_ATTACK_COLOR_CYAN(7),
    PROTO_MAP_ATTACK_COLOR_PINK(8),
    PROTO_MAP_ATTACK_COLOR_GRAY(9);

    companion object {
        private val lookup = MapAttackColor.entries.associateBy { it.id }
        fun fromId(id: Int): MapAttackColor? = lookup[id]
    }
}
