package game.player.character

import game.map.MapAttackCareerType
import game.map.MapAttackColor
import game.player.honor.HonorShowTitle
import game.player.pk.FreePkPlayerStats
import game.player.pk.ShowFreePKBattle
import kotlinx.serialization.Serializable

@Serializable
data class ExtraShowInfo(
    val isRedName: Boolean,
    val redNameNeedFightTimes: Int,
    val remainderRedNameFightTimes: Int,
    val valentineNum: Int,
    val battlefieldIsDeath: Boolean?,
    val battlefieldContinuousKill: Int?,
    val battlefieldCampIndex: Int?,
    val showSummonMonsterId: Int?,
    val activitySummerRemainTime: UInt?,
    val freePkStats: FreePkPlayerStats?,
    val freePkBattle: ShowFreePKBattle?,
    val honorTitle: HonorShowTitle?,
    val guildId: ULong?,
    val guildName: String?,
    val fireFlowerRemainTime: UInt?,
    val mapAttackColor: MapAttackColor,
    val mapAttackCareer: MapAttackCareerType,
    val mapAttackCareerLvl: Boolean,
)
