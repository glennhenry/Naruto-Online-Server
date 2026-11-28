package game.player.login

import game.player.character.PlayerInfo
import game.protocol.response.RetInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
data class GameLoginResponse(
    @ProtoNumber(1) val ret: RetInfo = RetInfo(0, ""),
    @ProtoNumber(3) val channel: UInt,
    @ProtoNumber(4) val scene: UInt,
    @ProtoNumber(5) val player: PlayerInfo,
    @ProtoNumber(6) val from: UInt,
    @ProtoNumber(7) val secondaryPasswordInfo: SecondaryPasswordInfo,
    @ProtoNumber(8) val isMergedServer: Boolean,
    @ProtoNumber(9) val hideZoneId: Int,
    @ProtoNumber(10) val canPvp: Int,
    @ProtoNumber(11) val isInnerQq: Boolean,
    @ProtoNumber(12) val guildBeastOpenFlag: Boolean,
    @ProtoNumber(13) val recordWhiteList: Boolean,
    @ProtoNumber(14) val baojuOpenFlag: Boolean,
    @ProtoNumber(15) val inheritFlag: Boolean,
    @ProtoNumber(16) val openDays: Int,
    // client-bug? proto number aren't in order
    // probably old definitions are deleted without the re-generating the proto class
    @ProtoNumber(61) val gmtOffset: Int
)
