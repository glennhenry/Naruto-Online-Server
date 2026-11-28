package game.items

import game.protocol.definition.ProtocolCmd
import kotlinx.serialization.Serializable

@Serializable
enum class BaojuSkillInlayState(val id: Int) {
    PROTO_BAOJU_SKILL_INLAY_STATE_INVALID(0),
    PROTO_BAOJU_SKILL_INLAY_STATE_LOCK(1),
    PROTO_BAOJU_SKILL_INLAY_STATE_UNLOCK(2);

    companion object {
        private val lookup = ProtocolCmd.entries.associateBy { it.id }
        fun fromId(id: UInt): ProtocolCmd? = lookup[id]
    }
}
