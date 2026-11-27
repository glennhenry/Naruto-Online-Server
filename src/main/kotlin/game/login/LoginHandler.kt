package game.login

import encore.fancam.Fancam
import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.NarutoFanchant
import game.protocol.ProtocolCmd
import game.protocol.cmdtype

/**
 * Handles [LoginRequest] message.
 */
class LoginHandler : FanchantHandler<NarutoFanchant<LoginRequest>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.PT_LOGIN)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<LoginRequest>>) = with(ctx) {
        Fancam.info { "Got login: $fanchant" }
        // todo
        // respond to message (reverse the SocketProtocol)
        // decrypt client incoming message
        // encrypt outgoing message
    }
}
