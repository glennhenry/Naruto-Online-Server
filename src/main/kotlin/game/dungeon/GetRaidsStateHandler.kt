package game.dungeon

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.ninjaExam.NinjaExamInfo
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
class GetRaidsStateHandler : FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.GET_WIPE_OUT_STATE)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                GetRaidsStateResponse(
                    raidsState = RaidsType.PROTO_RAIDS_NONE_STATE,
                    dungeonRaidsInfo = DungeonRaidsInfo.dummy(),
                    raidsReward = listOf(RaidsCountReward.dummy()),
                    examRaidsInfo = NinjaExamInfo.dummy(),
                    stageItemInfo = emptyList()
                ), fanchant
            )
        )
    }
}
