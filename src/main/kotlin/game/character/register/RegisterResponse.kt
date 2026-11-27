package game.character.register

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val ret: RetInfo = RetInfo(0, "")
)
