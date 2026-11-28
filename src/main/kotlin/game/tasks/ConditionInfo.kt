package game.tasks

import kotlinx.serialization.Serializable

@Serializable
data class ConditionInfo(
    val id: UInt,
    val type: UInt,
    val state: UInt,
    val data: UInt
) {
    companion object {
        fun dummy(): ConditionInfo {
            return ConditionInfo(
                id = 1u,
                type = 1u,
                state = 1u,
                data = 1u
            )
        }
    }
}
