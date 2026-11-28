package game.player.login

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
enum class SecondaryPasswordStatus(val id: Int) {
    @ProtoNumber(1)
    PROTO_SECONDARY_PASSWORD_STATUS_NONE(1),

    @ProtoNumber(2)
    PROTO_SECONDARY_PASSWORD_STATUS_SET(2),

    @ProtoNumber(3)
    PROTO_SECONDARY_PASSWORD_STATUS_FORGETTING(3),

    @ProtoNumber(4)
    PROTO_SECONDARY_PASSWORD_STATUS_VALID(4),

    @ProtoNumber(5)
    PROTO_SECONDARY_PASSWORD_STATUS_PASSWD_TRY_ULTIMATE(5);

    companion object {
        private val lookup = SecondaryPasswordStatus.entries.associateBy { it.id }
        fun fromId(id: Int): SecondaryPasswordStatus? = lookup[id]
    }
}
