package game.items

import kotlinx.serialization.Serializable

@Serializable
enum class BaojuEvent(val id: Int) {
    PROTO_BAOJU_EVENT_INVALID(0),
    PROTO_BAOJU_EVENT_UNLOCK(1),
    PROTO_BAOJU_EVENT_UPGRADE_QUAL(2);

    companion object {
        private val lookup = BaojuEvent.entries.associateBy { it.id }
        fun fromId(id: Int): BaojuEvent? = lookup[id]
    }
}
