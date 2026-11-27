package game.startup.login

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

/**
 * Response for login.
 */
@Serializable
data class LoginResponse(
    val ret: RetInfo = RetInfo(0, ""),
    // although its timestamp, client enforces this as UInt
    val timestamp: UInt
)
