package game.player.login

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.player.character.PlayerInfo
import game.player.common.PlayerKey
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
class GameLoginHandler : FanchantHandler<NarutoFanchant<GameLoginRequest>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.LOGIN)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<GameLoginRequest>>) = with(ctx) {
        connection.write(
            response(createLogin(), fanchant)
        )
    }

    // playerinfo is requested here, but only some are used
    // some others are used in GetPlayerInfoRequest
    fun createLogin(): GameLoginResponse {
        return GameLoginResponse(
            channel = 1u,
            scene = 10002u, // North Konoha
            player = PlayerInfo.dummy("Kakashi", PlayerKey(1u, 1u, 10u)),
            from = 1u,
            secondaryPasswordInfo = SecondaryPasswordInfo(
                status = SecondaryPasswordStatus.PROTO_SECONDARY_PASSWORD_STATUS_NONE,
                remaindForgetTime = 1_000_000u,
                canTryTimes = 5
            ),
            isMergedServer = false,
            hideZoneId = 0,
            canPvp = 0,
            isInnerQq = false,
            guildBeastOpenFlag = false,
            recordWhiteList = false,
            baojuOpenFlag = false,
            inheritFlag = false,
            openDays = 10,
            gmtOffset = 0
        )
    }
}
