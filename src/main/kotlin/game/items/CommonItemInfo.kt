package game.items

import kotlinx.serialization.Serializable

@Serializable
data class CommonItemInfo(
    val id: UInt,
    val num: UInt,
    val expiredTime: UInt,
    val expiredRemainderSecond: Int
) {
    companion object {
        fun dummy(): CommonItemInfo {
            return CommonItemInfo(
                id = 30501202u,
                num = (1u..4u).random(),
                expiredTime = 0u,
                expiredRemainderSecond = 0
            )
        }
    }
}
