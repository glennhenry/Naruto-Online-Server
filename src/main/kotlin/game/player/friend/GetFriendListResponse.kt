package game.player.friend

import game.protocol.response.RetInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class GetFriendListResponse(
    val retInfo: RetInfo = RetInfo(0, ""),
    val friendList: List<FriendInfo>,
    val disturbFlag: UInt
)
