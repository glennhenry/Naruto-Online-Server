package game.tasks

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class GetTaskInfoHandler: FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.TASK_GET_ALL_TASK_INFO)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                TaskInfoListResponse(
                    tasks = emptyList(),
                    taskCountInfo = TaskCountInfo.dummy()
                ), fanchant
            )
        )
    }
}
