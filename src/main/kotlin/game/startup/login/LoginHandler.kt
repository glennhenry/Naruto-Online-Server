package game.startup.login

import encore.fancam.Fancam
import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import encore.time.TimeCenter
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

/**
 * Handles [LoginRequest] message.
 */
class LoginHandler : FanchantHandler<NarutoFanchant<LoginRequest>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.PT_LOGIN)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<LoginRequest>>) = with(ctx) {
        Fancam.info {
            "Login request by playerId=${fanchant.playerId} " +
                    "of serverId=${fanchant.serverId} " +
                    "with role=${fanchant.role}"
        }

        connection.write(
            response(
                payload = LoginResponse(timestamp = TimeCenter.now()),
                fanchant = fanchant,
                encrypt = false
            )
        )
    }
}
