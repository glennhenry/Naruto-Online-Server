package game.player.activities.wishingLantern

import game.player.activities.AwardPackageBox
import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

// this is probably Wishing Lantern event
@Serializable
data class WishingLightResponse(
    val ret: RetInfo = RetInfo(0, ""),
    val beginTime: UInt,
    val endTime: UInt,
    val currTime: UInt,
    val flyTime: UInt,
    val boxs: List<AwardPackageBox>,
    val praisePlayer: List<WishingLightPlayerInfo>,
    val praiseCount: UInt,
    // in real game, these are dummy lanterns owned by nobody
    val freshPlayer: List<WishingLightPlayerInfo>
)
