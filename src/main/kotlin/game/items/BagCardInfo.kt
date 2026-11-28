package game.items

import kotlinx.serialization.Serializable

@Serializable
data class BagCardInfo(
    val id: UInt,
    val num: UInt,
    val combat: UInt,
    val index: UInt,
    val recruitStatus: BagCardRecruitStatus
) {
    companion object {
        fun dummy(): BagCardInfo {
            return BagCardInfo(
                // 10-tails jinchuriki madara
                id = 20400290u,
                num = 2u,
                combat = 1u,
                index = 0u,
                recruitStatus = BagCardRecruitStatus.PROTO_BAG_CARD_RECRUIT_STATUS_ALREADY
            )
        }
    }
}
