package game.player.friend

import game.player.character.DiamondInfo
import game.player.character.PlayerVipLevel
import game.player.common.PlayerKey
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class FriendInfo(
    val playerKey: PlayerKey,
    val name: String,
    val level: Int,
    val roleCategory: Int,
    val status: FriendStatus,
    val teamId: ULong,
    val friendLevel: UInt,
    val friendDegree: UInt,
    val wateringStatus: UInt,
    val vipLevel: PlayerVipLevel,
    val diamondInfo: DiamondInfo,
    val nextLevelDegree: UInt,
    val logoutTime: ULong,
    val combatPower: UInt,
    val isInWhiteList: Boolean,
    val isAttention: Boolean,
    val fashionId: Int,
    val fdAddTs: UInt,
)
