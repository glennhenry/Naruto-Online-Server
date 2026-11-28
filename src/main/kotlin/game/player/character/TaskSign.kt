package game.player.character

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
enum class TaskSign(val id: Int) {
    @ProtoNumber(1)
    PROTO_TASK_SIGN_SCENE_ESCORT_NONE(1),

    @ProtoNumber(2)
    PROTO_TASK_SIGN_SCENE_ESCORT_S(2),

    @ProtoNumber(3)
    PROTO_TASK_SIGN_SCENE_ESCORT_A(3),

    @ProtoNumber(4)
    PROTO_TASK_SIGN_SCENE_ESCORT_B(4);

    companion object {
        private val lookup = entries.associateBy { it.id }
        fun fromId(id: Int): TaskSign? = lookup[id]
    }
}
