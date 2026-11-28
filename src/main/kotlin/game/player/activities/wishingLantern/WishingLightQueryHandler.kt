package game.player.activities.wishingLantern

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.player.activities.AwardPackageBox
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class WishingLightQueryHandler: FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_ACTIVITY_WISHING_LIGHT_QUERY)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                WishingLightResponse(
                    beginTime = 0u,
                    endTime = 100u,
                    currTime = 20u,
                    flyTime = 10u,
                    boxs = listOf(AwardPackageBox.dummy()),
                    praisePlayer = emptyList(),
                    praiseCount = 1u,
                    freshPlayer = emptyList()
                ), fanchant
            )
        )
    }
}
