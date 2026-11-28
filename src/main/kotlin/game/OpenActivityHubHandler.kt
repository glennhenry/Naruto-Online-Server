package game

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.activities.ActivityHubNodeInfo
import game.activities.DailyItem
import game.activities.OpenActivityHubResponse
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class OpenActivityHubHandler : FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_ACTIVITY_HUB_OPEN)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                OpenActivityHubResponse(
                    todayActivityList = listOf(ActivityHubNodeInfo.dummy()),
                    otherActivityList = listOf(ActivityHubNodeInfo.dummy()),
                    activePoint = 1,
                    storeInfo = DailyItemStore(listOf(DailyItem.dummy()))
                ), fanchant
            )
        )
    }
}
