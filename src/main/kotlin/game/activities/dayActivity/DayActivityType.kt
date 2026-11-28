package game.activities.dayActivity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
enum class DayActivityType(val id: Int) {
    @ProtoNumber(1) PROTO_DAY_ACTIVITY_TYPE_1(1),
    @ProtoNumber(2) PROTO_DAY_ACTIVITY_TYPE_2(2),
    @ProtoNumber(3) PROTO_DAY_ACTIVITY_TYPE_3(3),
    @ProtoNumber(4) PROTO_DAY_ACTIVITY_TYPE_4(4),
    @ProtoNumber(5) PROTO_DAY_ACTIVITY_TYPE_5(5),
    @ProtoNumber(6) PROTO_DAY_ACTIVITY_TYPE_6(6),
    @ProtoNumber(7) PROTO_DAY_ACTIVITY_TYPE_7(7),
    @ProtoNumber(8) PROTO_DAY_ACTIVITY_TYPE_8(8),
    @ProtoNumber(9) PROTO_DAY_ACTIVITY_TYPE_9(9),
    @ProtoNumber(10) PROTO_DAY_ACTIVITY_TYPE_10(10),
    @ProtoNumber(11) PROTO_DAY_ACTIVITY_TYPE_11(11),
    @ProtoNumber(12) PROTO_DAY_ACTIVITY_TYPE_12(12),
    @ProtoNumber(13) PROTO_DAY_ACTIVITY_TYPE_13(13),
    @ProtoNumber(14) PROTO_DAY_ACTIVITY_TYPE_14(14),
    @ProtoNumber(15) PROTO_DAY_ACTIVITY_TYPE_15(15),
    @ProtoNumber(16) PROTO_DAY_ACTIVITY_TYPE_16(16),
    @ProtoNumber(17) PROTO_DAY_ACTIVITY_TYPE_17(17),
    @ProtoNumber(18) PROTO_DAY_ACTIVITY_TYPE_18(18);

    companion object {
        private val lookup = DayActivityType.entries.associateBy { it.id }
        fun fromId(id: Int): DayActivityType? = lookup[id]
    }
}
