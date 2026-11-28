package game.items

import kotlinx.serialization.Serializable

@Serializable
data class TaskItemInfo(
    val id: UInt,
    val num: UInt,
    val index: UInt,
    val expiredTime: UInt,
    val expiredRemainderSecond: Int
) {
    companion object {
        fun dummy(): TaskItemInfo {
            return TaskItemInfo(
                id = 10100146u,
                num = (1u..4u).random(),
                index = 0u,
                expiredTime = 0u,
                expiredRemainderSecond = 0
            )
        }
    }
}
