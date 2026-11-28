package game.items

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response
import kotlinx.serialization.ExperimentalSerializationApi

class GetItemListHandler : FanchantHandler<NarutoFanchant<*>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.GET_ITEM_LIST)

    // response can be ProtoGetBagListRsp (from GET_ITEM_LIST)
    // or ProtoBagChangeNotify
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<*>>) = with(ctx) {
        // must response non-null values
        // sending error != sending empty inventory
        connection.write(
            response(
                GetBagListResponse(
                    bagInfo = BagInfo.dummy(),
                    typeList = GetBagListType.PROTO_GET_BAG_LIST_TYPE_ALL
                ), fanchant
            )
        )
    }
}
