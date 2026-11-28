package game.player.vip

import game.player.character.PlayerVipLevel
import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

// also known as medal (genin, chunin, jonin) medal
@Serializable
data class GetVIPInfoResponse(
    val retInfo: RetInfo = RetInfo(0, ""),
    val vipLevel: PlayerVipLevel,
    val vipExpiredTime: UInt,
    val vipRemainderDay: UInt,
    val geninPrivilege: VIPPrivilegeInfo,
    val chuninPrivilege: VIPPrivilegeInfo,
    val joninPrivilege: VIPPrivilegeInfo,
    val discountExpiredTime: UInt,
    val buyVipJoninCount: UInt
)
