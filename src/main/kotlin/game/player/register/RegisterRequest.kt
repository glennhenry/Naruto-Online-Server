package game.player.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val roleCategory: Int,
    val name: String,
    val portrait: Int,
    val via: String,
    val adTag: String?,
    val inviterUinList: List<UInt>
)
