package game.login

import game.protocol.message.PbMessage
import kotlinx.serialization.Serializable

/**
 * Represent the `PtloginRequest` message.
 */
@Serializable
class LoginRequest(val skey: String, val seperateSkey: String) : PbMessage
