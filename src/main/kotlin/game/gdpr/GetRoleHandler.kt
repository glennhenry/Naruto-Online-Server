package game.gdpr

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.RetInfo
import game.protocol.response.response

class GetRoleHandler: FanchantHandler<NarutoFanchant<GetRoleRequest>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_GET_ROLE)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<GetRoleRequest>>) = with(ctx) {
        // this ask legal information GDPR
        // if error code is 1106, account is expired (was scheduled to be deleted before)
        // error code 0 is success -> login to existing character
        // error code -14001 is success -> create new character
        connection.write(
            response(
                GetRoleResponse(
                    ret = RetInfo(-14001, ""),
                    gdprFlag = true, // unused
                    age = 18, // unused
                    region = 1, // unused
                    status = GDPRStatus.EM_GDPR_ALL_DONE, // unused
                    remainTime = 1_000_000 // unused
                ),
                fanchant
            )
        )
    }
}
