package game.items

import kotlinx.serialization.protobuf.ProtoNumber

enum class BagCardRecruitStatus(val id: Int) {
    @ProtoNumber(1)
    PROTO_BAG_CARD_RECRUIT_STATUS_CANNT(1),

    @ProtoNumber(2)
    PROTO_BAG_CARD_RECRUIT_STATUS_CAN(2),

    @ProtoNumber(3)
    PROTO_BAG_CARD_RECRUIT_STATUS_ALREADY(3);

    companion object {
        private val lookup = BagCardRecruitStatus.entries.associateBy { it.id }
        fun fromId(id: Int): BagCardRecruitStatus? = lookup[id]
    }
}
