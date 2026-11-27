package game.startup.login

import encore.fancam.Fancam
import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
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
                    "with role=${fanchant.role}." +
                    "skey=${fanchant.payload.skey},"
                    "seperateSkey=${fanchant.payload.seperateSkey},"
        }

        connection.write(
            response(
                // timestamp from this response isn't used
                // so just response anything valid
                payload = LoginResponse(timestamp = 1u),
                fanchant = fanchant,
                encrypt = false
            )
        )
    }
}
