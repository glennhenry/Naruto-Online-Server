package game.player.vip

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.player.character.PlayerVipLevel
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class GetVIPInfoHandler : FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.VIP_GET_VIP_DETAIL)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                GetVIPInfoResponse(
                    vipLevel = PlayerVipLevel.PROTO_PLAYER_VIP_LEVEL_NONE,
                    vipExpiredTime = 0u,
                    vipRemainderDay = 0u,
                    geninPrivilege = VIPPrivilegeInfo.dummy(PlayerVipLevel.PROTO_PLAYER_VIP_LEVEL_GENIN),
                    chuninPrivilege = VIPPrivilegeInfo.dummy(PlayerVipLevel.PROTO_PLAYER_VIP_LEVEL_CHUNIN),
                    joninPrivilege = VIPPrivilegeInfo.dummy(PlayerVipLevel.PROTO_PLAYER_VIP_LEVEL_JONIN),
                    discountExpiredTime = 10u,
                    buyVipJoninCount = 0u
                ), fanchant
            )
        )
    }
}
