package game.character.register

import encore.fancam.Fancam
import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class RegisterHandler: FanchantHandler<NarutoFanchant<RegisterRequest>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_REGISTER_ROLE)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<RegisterRequest>>) = with(ctx) {
        Fancam.info {
            "Register request: playerId=${fanchant.playerId}, serverId=${fanchant.serverId}, " +
                    "name=${fanchant.payload.name}, role=${fanchant.payload.roleCategory}"
        }
        connection.write(
            response(
                RegisterResponse(),
                fanchant
            )
        )
    }
}
