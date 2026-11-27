package game.gdpr

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

@Serializable
data class InnerGetGDPRInfoRsp(
    val ret: RetInfo = RetInfo(0, ""),
    val deleteFlag: Int,
    val eeaFlag: Int,
    val parentalConsentFlag: Int,
    val privacyPolicyFlag: Int,
    val remainTime: Int
)
