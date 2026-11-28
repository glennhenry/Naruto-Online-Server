package game.activities

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class GetActivityMainUIHandler: FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_ACTIVITY_HUB_GET_MAIN_UI)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                GetActivityMainUIResponse(
                    timingActivityInfo = ActivityHubTimingInfo.dummy(),
                    dailyActivityList = DailyActivityInfo.dummy()
                ), fanchant
            )
        )
    }
}
