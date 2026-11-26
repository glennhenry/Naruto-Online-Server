package game.login

import game.protocol.NarutoFanchant
import game.protocol.ProtocolCmdDef
import game.protocol.autoToString
import kotlinx.serialization.Serializable

/**
 * Represent the `PtloginRequest` message.
 */
@Serializable
class LoginRequest(
    val skey: String,
    val seperateSkey: String
) : NarutoFanchant {
    override val type: String = ProtocolCmdDef.PT_LOGIN.toString()

    override fun toString(): String {
        return autoToString()
    }
}
