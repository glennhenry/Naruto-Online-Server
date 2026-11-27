package game.gdpr

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

@Serializable
data class GetRoleResponse(
    val ret: RetInfo = RetInfo(0, ""),
    val gdprFlag: Boolean,
    val age: Int,
    val region: Int,
    val status: GDPRStatus,
    val remainTime: Int
)
