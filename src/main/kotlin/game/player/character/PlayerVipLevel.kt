package game.player.character

import kotlinx.serialization.Serializable

@Serializable
enum class PlayerVipLevel(val id: Int) {
    PROTO_PLAYER_VIP_LEVEL_NONE(0),
    PROTO_PLAYER_VIP_LEVEL_GENIN(1),
    PROTO_PLAYER_VIP_LEVEL_CHUNIN(2),
    PROTO_PLAYER_VIP_LEVEL_JONIN(3);

    companion object {
        private val lookup = entries.associateBy { it.id }
        fun fromId(id: Int): PlayerVipLevel? = lookup[id]
    }
}
