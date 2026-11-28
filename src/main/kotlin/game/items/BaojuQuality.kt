package game.items

import kotlinx.serialization.Serializable

@Serializable
enum class BaojuQuality(val id: Int) {
    PROTO_BAOJU_QUALITY_INVALID(0),
    PROTO_BAOJU_QUALITY_B(1),
    PROTO_BAOJU_QUALITY_A(2),
    PROTO_BAOJU_QUALITY_S(3),
    PROTO_BAOJU_QUALITY_SS(4);

    companion object {
        private val lookup = BaojuQuality.entries.associateBy { it.id }
        fun fromId(id: Int): BaojuQuality? = lookup[id]
    }
}
