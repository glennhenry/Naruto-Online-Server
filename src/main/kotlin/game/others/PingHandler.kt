package game.others

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

// I don't know which plugin made PING request
class PingHandler: FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_PING)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(PingResponse(), fanchant)
        )
    }
}
