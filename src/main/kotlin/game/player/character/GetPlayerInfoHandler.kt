package game.player.character

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.player.common.PlayerKey
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class GetPlayerInfoHandler : FanchantHandler<NarutoFanchant<GetPlayerInfoRequest>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.GET_USER_INFO)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<GetPlayerInfoRequest>>) = with(ctx) {
        connection.write(
            response(GetPlayerInfoResponse(player = PlayerInfo.dummy("Kakashi", PlayerKey(1u, 1u, 10u))), fanchant)
        )
    }
}
