package game.player.login

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.map.MapAttackCareerType
import game.map.MapAttackColor
import game.player.character.DiamondInfo
import game.player.character.ExtraShowInfo
import game.player.character.PlayerInfo
import game.player.character.PlayerVipLevel
import game.player.character.TaskSign
import game.player.common.PlayerKey
import game.player.common.Point
import game.player.honor.HonorShowTitle
import game.player.pk.FreePkPlayerStats
import game.player.pk.ShowFreePKBattle
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

    fun createLogin(): GameLoginResponse {
        return GameLoginResponse(
            channel = 1u,
            scene = 10002u, // North Konoha
            player = PlayerInfo(
                playerKey = PlayerKey(1u, 1u, 10u),
                roleId = 1u,
                name = "Kakashi",
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
            ),
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
