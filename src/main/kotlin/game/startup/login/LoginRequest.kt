package game.startup.login

import kotlinx.serialization.Serializable

/**
 * Represent the `PtloginRequest` message.
 */
@Serializable
class LoginRequest(val skey: String, val seperateSkey: String)
