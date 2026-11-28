package game.player.honor

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.RetInfo
import game.protocol.response.response

class HonorSettingHandler : FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_HONOR_GET_SETTING)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        // with info=null or error code not 0, this won't call UpdateHonorSettingCommand
        connection.write(
            response(HonorSettingResponse(RetInfo(1, ""), null), fanchant)
        )
    }
}
