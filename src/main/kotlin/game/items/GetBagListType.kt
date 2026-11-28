package game.items

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
enum class GetBagListType(val id: Int) {
    @ProtoNumber(1) PROTO_GET_BAG_LIST_TYPE_ALL(1),
    @ProtoNumber(2) PROTO_GET_BAG_LIST_TYPE_ITEM(2),
    @ProtoNumber(3) PROTO_GET_BAG_LIST_TYPE_TASK(3),
    @ProtoNumber(4) PROTO_GET_BAG_LIST_TYPE_EQUIP(4),
    @ProtoNumber(5) PROTO_GET_BAG_LIST_TYPE_CARD(5),
    @ProtoNumber(6) PROTO_GET_BAG_LIST_TYPE_SUMMON_MONSTER_CARD(6),
    @ProtoNumber(7) PROTO_GET_BAG_LIST_TYPE_RUNE(7),
    @ProtoNumber(8) PROTO_GET_BAG_LIST_TYPE_NINJA_PROPS(8),
    @ProtoNumber(9) PROTO_GET_BAG_LIST_TYPE_TREASURE_MAP(9),
    @ProtoNumber(10) PROTO_GET_BAG_LIST_TYPE_JADE(10),
    @ProtoNumber(11) PROTO_GET_BAG_LIST_TYPE_STONE(11),
    @ProtoNumber(13) PROTO_GET_BAG_LIST_TYPE_FAMILY_NINJA_GIFT(13),
    @ProtoNumber(14) PROTO_GET_BAG_LIST_TYPE_SKILL_SCROLL(14),
    @ProtoNumber(15) PROTO_GET_BAG_LIST_TYPE_BAOJU(15),
    @ProtoNumber(16) PROTO_GET_BAG_LIST_TYPE_TIMER(16),
    @ProtoNumber(17) PROTO_GET_BAG_LIST_TYPE_SKILL_BOOK(17);

    companion object {
        private val lookup = GetBagListType.entries.associateBy { it.id }
        fun fromId(id: Int): GetBagListType? = lookup[id]
    }
}
