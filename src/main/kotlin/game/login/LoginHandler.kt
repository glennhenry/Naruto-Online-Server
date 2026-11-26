package game.login

import encore.fancam.Fancam
import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.ProtocolCmdDef

/**
 * Handles [LoginRequest] message.
 */
class LoginHandler : FanchantHandler<LoginRequest> {
    override val fanchantType: String = ProtocolCmdDef.PT_LOGIN.toString()

    override suspend fun handle(ctx: HandlerContext<LoginRequest>) = with(ctx) {
        Fancam.info { "Got login: $fanchant" }
    }
}
