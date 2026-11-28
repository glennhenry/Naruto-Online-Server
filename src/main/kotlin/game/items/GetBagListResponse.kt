package game.items

import game.protocol.response.RetInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class GetBagListResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val ret: RetInfo = RetInfo(0, ""),
    val bagInfo: BagInfo,
    val typeList: GetBagListType,
)
