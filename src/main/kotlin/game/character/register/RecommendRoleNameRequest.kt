package game.character.register

import kotlinx.serialization.Serializable

@Serializable
data class RecommendRoleNameRequest(
    val gender: Int
)
