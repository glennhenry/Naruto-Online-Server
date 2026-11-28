package game.player.character

import game.map.MapAttackCareerType
import game.map.MapAttackColor
import game.player.common.PlayerKey
import game.player.common.Point
import game.player.honor.HonorShowTitle
import game.player.pk.FreePkPlayerStats
import game.player.pk.ShowFreePKBattle
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
) {
    companion object {
        fun dummy(name: String, playerKey: PlayerKey): PlayerInfo {
            return PlayerInfo(
                playerKey = playerKey,
                roleId = 1u,
                name = name,
                ninjia = 10000101u,
                location = Point(
                    x = 50u,
                    y = 60u
                ),
                direction = 1u,
                money = 10_002,
                yuanbao = 12345u,
                dianquan = 3210u,
                level = 11u,
                hp = 100123u,
                maxHp = 102000u,
                katha = 101u,
                combatPower = 350_123u,
                exp = 120u,
                roleCategory = 2,
                strength = 324u,
                maxStrength = 400u,
                canReachMaxStrength = 450u,
                buyStrengthGold = 10,
                vipLevel = PlayerVipLevel.PROTO_PLAYER_VIP_LEVEL_NONE,
                vipExpiredTime = 0u,
                vipRemainderDay = 0u,
                showNinjaId = 10000101,
                chosedTitleId = 0u,
                diamondInfo = DiamondInfo(
                    blueLevel = 1,
                    blueCommon = false,
                    blueYear = false,
                    blueSuper = false,
                    yellowLevel = 1,
                    yellowCommon = false,
                    yellowYear = false,
                    yellowSuper = false,
                    qqLevel = 1,
                    qqCommon = false,
                    qqYear = false,
                    qqSuper = false
                ),
                isGm = 1,
                taskSign = TaskSign.PROTO_TASK_SIGN_SCENE_ESCORT_NONE,
                fashionNinja = 13103801,
                extraShowInfo = ExtraShowInfo(
                    isRedName = false,
                    redNameNeedFightTimes = 0,
                    remainderRedNameFightTimes = 0,
                    valentineNum = 0,
                    battlefieldIsDeath = false,
                    battlefieldContinuousKill = 0,
                    battlefieldCampIndex = 1,
                    showSummonMonsterId = 0,
                    activitySummerRemainTime = 0u,
                    freePkStats = FreePkPlayerStats.PROTO_FREE_PK_PLAYER_STATS_NONE,
                    freePkBattle = ShowFreePKBattle(
                        freePkBattleId = 0u,
                        freePkSide = 0
                    ),
                    honorTitle = HonorShowTitle(
                        cupId = 0,
                        affixId = 0,
                        effectId = 0
                    ),
                    guildId = 0u,
                    guildName = "NotAkatsuki",
                    fireFlowerRemainTime = 0u,
                    mapAttackColor = MapAttackColor.PROTO_MAP_ATTACK_COLOR_WHITE,
                    mapAttackCareer = MapAttackCareerType.PROTO_MAP_ATTACK_CAREER_TYPE_NONE,
                    mapAttackCareerLvl = false
                ),
                baojuScore = 0u,
                shadowOfSun = 1,
                shadowOfMoon = 1,
                shadowOfSunThisWeek = 1,
                shadowOfMoonThisWeek = 1,
                shadowOfSunLimit = 1,
                shadowOfMoonLimit = 1,
                shadowOfSunInitLimit = 1,
                shadowOfMoonInitLimit = 1,
                sosPowerEndTime = 0u,
                somPowerEndTime = 0u,
                starCoupon = 0u,
                fourSeasonToken = 0u,
                somExOnSign = 1,
                sosPowerRequired = 2,
                isModaoshi = 1
            )
        }
    }
}
