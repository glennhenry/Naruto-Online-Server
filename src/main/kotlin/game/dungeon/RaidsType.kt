package game.dungeon

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
enum class RaidsType(val id: Int) {
    @ProtoNumber(1) PROTO_RAIDS_NONE_STATE(1),
    @ProtoNumber(2) PROTO_EXAM_RAIDS_STATE(2),
    @ProtoNumber(3) PROTO_DUNGEON_RAIDS_STATE(3);

    companion object {
        private val lookup = RaidsType.entries.associateBy { it.id }
        fun fromId(id: Int): RaidsType? = lookup[id]
    }
}
