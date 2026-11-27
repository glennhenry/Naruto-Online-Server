package game.others

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class ReportGDPRCheckHandler : FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.REPORT_GDPR_CHECK)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        connection.write(
            response(
                InnerGetGDPRInfoRsp(
                    deleteFlag = 0,
                    eeaFlag = 1, // unused
                    parentalConsentFlag = 1, // unused
                    privacyPolicyFlag = 1, // unused
                    remainTime = 1_000_000
                ), fanchant
            )
        )
    }
}
