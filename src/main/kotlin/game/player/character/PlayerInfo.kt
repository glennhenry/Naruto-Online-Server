package game.player.character

import game.player.common.PlayerKey
import game.player.common.Point
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
data class PlayerInfo(
    @ProtoNumber(1) val playerKey: PlayerKey,
    @ProtoNumber(2) val roleId: UInt,
    @ProtoNumber(3) val name: String,
    @ProtoNumber(4) val ninjia: UInt,
    @ProtoNumber(5) val location: Point,
    @ProtoNumber(6) val direction: UInt,
    @ProtoNumber(7) val money: Long,
    @ProtoNumber(8) val yuanbao: UInt,
    @ProtoNumber(9) val dianquan: UInt,
    @ProtoNumber(10) val level: UInt,
    @ProtoNumber(11) val hp: UInt,
    @ProtoNumber(12) val maxHp: UInt,
    @ProtoNumber(13) val katha: UInt,
    @ProtoNumber(14) val combatPower: UInt,
    @ProtoNumber(15) val exp: UInt,
    @ProtoNumber(16) val roleCategory: Int,
    @ProtoNumber(17) val strength: UInt,
    @ProtoNumber(18) val maxStrength: UInt,
    @ProtoNumber(19) val canReachMaxStrength: UInt,
    @ProtoNumber(20) val buyStrengthGold: Int,
    @ProtoNumber(21) val vipLevel: PlayerVipLevel,
    @ProtoNumber(22) val vipExpiredTime: UInt,
    @ProtoNumber(23) val vipRemainderDay: UInt,
    @ProtoNumber(24) val showNinjaId: Int,
    @ProtoNumber(25) val chosedTitleId: UInt,
    @ProtoNumber(26) val diamondInfo: DiamondInfo,
    @ProtoNumber(27) val isGm: Int,
    @ProtoNumber(28) val taskSign: TaskSign,
    @ProtoNumber(29) val fashionNinja: Int,
    @ProtoNumber(30) val extraShowInfo: ExtraShowInfo?,
    @ProtoNumber(31) val baojuScore: UInt,
    @ProtoNumber(32) val shadowOfSun: Int,
    @ProtoNumber(33) val shadowOfMoon: Int,
    @ProtoNumber(34) val shadowOfSunThisWeek: Int,
    @ProtoNumber(35) val shadowOfMoonThisWeek: Int,
    @ProtoNumber(36) val shadowOfSunLimit: Int,
    @ProtoNumber(37) val shadowOfMoonLimit: Int,
    @ProtoNumber(38) val shadowOfSunInitLimit: Int,
    @ProtoNumber(39) val shadowOfMoonInitLimit: Int,
    @ProtoNumber(40) val sosPowerEndTime: UInt,
    @ProtoNumber(41) val somPowerEndTime: UInt,
    @ProtoNumber(47) val starCoupon: UInt,
    @ProtoNumber(48) val fourSeasonToken: UInt,
    @ProtoNumber(42) val somExOnSign: Int,
    @ProtoNumber(43) val sosPowerRequired: Int,
    @ProtoNumber(100) val isModaoshi: Int,
)
