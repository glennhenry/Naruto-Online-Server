package game.telemetry

import encore.fancam.Fancam
import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype

class SysReportHandler : FanchantHandler<NarutoFanchant<ReportLog>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.SYS_REPORT)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<ReportLog>>) = with(ctx) {
        val enum = ReportLogID.fromId(fanchant.payload.logId)
        val msg = when (enum) {
            ReportLogID.PROTO_REPORT_LOG_ID_LOGIN_NODE -> {
                val id = fanchant.payload.msg.toIntOrNull()
                if (id != null) {
                    SysReportMessageDef.fromId(id)
                } else {
                    fanchant.payload.msg
                }
            }

            else -> fanchant.payload.msg
        }

        Fancam.info {
            "SysReport from playerId=${fanchant.playerId} serverId=${fanchant.serverId}. " +
                    "id=${ReportLogID.fromId(fanchant.payload.logId)}, " +
                    "msg=$msg"
        }
    }
}
