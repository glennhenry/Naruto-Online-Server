package game.activities.dayActivity

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class GetDayActivityInfoHandler : FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.GET_DAY_ACTIVITY_INFO)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                GetDayActivityInfoResponse(
                    dayActivityInfo = DayActivityInfo.dummy()
                ), fanchant
            )
        )
    }
}
