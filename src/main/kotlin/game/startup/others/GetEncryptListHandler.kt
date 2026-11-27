package game.startup.others

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class GetEncryptListHandler : FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_GETENCRYPT_LIST)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                GetEncryptListResponse(
                    isEncrypt = false,
                    exceptionCmd = emptyList()
                ),
                fanchant,
            )
        )
    }
}
