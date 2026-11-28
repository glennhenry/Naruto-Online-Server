package game.player.friend

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class GetFriendListHandler: FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.GET_FRIEND_LIST)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(GetFriendListResponse(
                friendList = emptyList(),
                disturbFlag = 0u
            ),fanchant)
        )
    }
}
