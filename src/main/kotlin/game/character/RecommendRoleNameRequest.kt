package game.character

import kotlinx.serialization.Serializable

@Serializable
data class RecommendRoleNameRequest(
    val gender: Int
)
