package game.player.register

import kotlinx.serialization.Serializable

@Serializable
data class RecommendRoleNameRequest(
    val gender: Int
)
