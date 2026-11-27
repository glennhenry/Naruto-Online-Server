package game.character.register

import kotlinx.serialization.Serializable

@Serializable
data class RecommendRoleNameResponse(
    val nameList: List<String>
)
