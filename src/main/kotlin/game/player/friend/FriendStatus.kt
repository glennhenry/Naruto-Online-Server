package game.player.friend

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
enum class FriendStatus(val id: Int) {
    @ProtoNumber(1) PROTO_FRIEND_STATUS_ONLINE(1),
    @ProtoNumber(2) PROTO_FRIEND_STATUS_OFFLINE(2);

    companion object {
        private val lookup = FriendStatus.entries.associateBy { it.id }
        fun fromId(id: Int): FriendStatus? = lookup[id]
    }
}
