package game.player.pk

import kotlinx.serialization.Serializable

@Serializable
enum class FreePkPlayerStats(val id: Int) {
    PROTO_FREE_PK_PLAYER_STATS_NONE(0),
    PROTO_FREE_PK_PLAYER_STATS_PK(1),
    PROTO_FREE_PK_PLAYER_STATS_PEACE(2);

    companion object {
        private val lookup = FreePkPlayerStats.entries.associateBy { it.id }
        fun fromId(id: Int): FreePkPlayerStats? = lookup[id]
    }
}
